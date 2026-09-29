//DEPS info.picocli:picocli:4.7.6
//DEPS org.jline:jline:3.26.2
//DEPS com.samskivert:jmustache:1.16
//DEPS org.json:json:20240303
//FILES templates/

import com.samskivert.mustache.Mustache
import org.jline.reader.LineReaderBuilder
import org.json.JSONObject
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.File
import java.net.JarURLConnection
import java.nio.file.Files
import java.nio.file.Path
import java.util.Locale
import java.util.jar.JarFile
import java.util.concurrent.Callable

private val serviceIdPattern = Regex("[a-z][a-z0-9]*(?:_[a-z0-9]+)*")
private val packageSegmentPattern = Regex("[A-Za-z_][A-Za-z0-9_]*")
private val conditionalPathTokens = setOf(
    "{{includeMetadata}}",
    "{{includeAudio}}",
    "{{includeLyrics}}",
    "{{includeScrobble}}",
)
private val kotlinKeywords = setOf(
    "as", "break", "class", "continue", "do", "else", "false", "for", "fun", "if",
    "in", "interface", "is", "null", "object", "package", "return", "super", "this",
    "throw", "true", "try", "typealias", "typeof", "val", "var", "when", "while",
)

private enum class PluginFeature(val optionName: String, val promptLabel: String) {
    METADATA("metadata", "Metadata APIs"),
    AUDIO("audio", "Audio APIs"),
    LYRICS("lyrics", "Lyrics API"),
    SCROBBLE("scrobble", "Scrobble API"),
}

private data class PluginConfig(
    val serviceId: String,
    val authorGroup: String,
    val serviceDisplayName: String,
    val features: Set<PluginFeature>,
) {
    val moduleName: String = "spotube_plugin_$serviceId"
    val serviceClassName: String = serviceId.split('_').joinToString("") { part ->
        part.replaceFirstChar { character -> character.titlecase(Locale.ROOT) }
    }
    val packageName: String = "$authorGroup.$moduleName"
    val packagePath: String = packageName.replace('.', '/')
    val projectName: String = "$moduleName-project"
    val description: String = "Spotube music service provider plugin for $serviceDisplayName"

    fun templateModel(): MutableMap<String, Any> = mutableMapOf(
        "serviceId" to serviceId,
        "serviceDisplayName" to serviceDisplayName,
        "serviceClassName" to serviceClassName,
        "authorGroup" to authorGroup,
        "moduleName" to moduleName,
        "packageName" to packageName,
        "packagePath" to packagePath,
        "projectName" to projectName,
        "description" to description,
        "capabilities" to features.map { it.optionName },
        "includeMetadata" to (PluginFeature.METADATA in features),
        "includeAudio" to (PluginFeature.AUDIO in features),
        "includeLyrics" to (PluginFeature.LYRICS in features),
        "includeScrobble" to (PluginFeature.SCROBBLE in features),
        "versions" to mapOf(
            "kotlin" to "2.3.21",
            "ktor" to "3.4.3",
            "serialization" to "1.10.0",
        ),
        "serviceDisplayNameKotlin" to jsonStringContent(serviceDisplayName),
        "authorGroupKotlin" to jsonStringContent(authorGroup),
    )
}

@Command(name = "create", description = ["Scaffold a Spotube Kotlin Multiplatform provider plugin."])
class GenerateKmpPluginCommand : Callable<Int> {
    @field:Option(names = ["-s", "--service"], paramLabel = "ID", description = ["Lowercase service ID, for example soundcloud."])
    var service: String? = null

    @field:Option(names = ["-a", "--author"], paramLabel = "GROUP", description = ["Author package group, for example io.github.myname."])
    var author: String? = null

    @field:Option(names = ["-d", "--display-name"], paramLabel = "NAME", description = ["User-facing service name."])
    var displayName: String? = null

    @field:Option(
        names = ["-c", "--capabilities"],
        paramLabel = "LIST",
        split = ",",
        description = ["Comma-separated plugin APIs: metadata,audio,lyrics,scrobble (default: metadata,audio)."],
    )
    var capabilities: MutableList<String> = mutableListOf()

    @field:Option(names = ["-o", "--output"], paramLabel = "DIR", description = ["Output project directory (default: ./<moduleName>)."])
    var output: Path? = null

    @field:Option(names = ["-h", "--help"], usageHelp = true, description = ["Show this help message and exit."])
    var helpRequested: Boolean = false

    override fun call(): Int = try {
        val config = collectConfig()
        validate(config)

        val destination = (output ?: Path.of(config.moduleName)).toAbsolutePath().normalize()
        require(!Files.exists(destination)) { "output directory already exists: $destination" }

        val templates = locateTemplateDirectory()
        val generatedFileCount = try {
            val templateRoot = templates.root.toPath().toAbsolutePath().normalize()
            require(destination != templateRoot && !destination.startsWith(templateRoot)) {
                "output directory cannot be the templates directory or a child of it."
            }
            require(!templateRoot.startsWith(destination)) { "output directory cannot contain the templates directory." }

            try {
                val count = TemplateEngine(config.templateModel()).processTemplateDirectory(templates.root, destination.toFile())
                validateGeneratedProject(destination.toFile(), config)
                initializeGit(destination.toFile())
                count
            } catch (error: Exception) {
                destination.toFile().deleteRecursively()
                throw error
            }
        } finally {
            templates.cleanupRoot?.deleteRecursively()
        }

        println("✅ Generated Spotube Plugin Scaffold: '${config.moduleName}'")
        println("📁 Directory: $destination")
        println("🔌 APIs: ${config.features.joinToString { it.optionName }}")
        println("📄 Files: $generatedFileCount")
        println()
        println("Next steps:")
        println("  1. cd ${destination.fileName}")
        println("  2. Add provider-specific credentials/configuration if needed by your implementation")
        println("  3. ./gradlew :${config.moduleName}:jsBrowserDistribution")
        println("  4. ./gradlew :${config.moduleName}:jvmTest")
        0
    } catch (error: IllegalArgumentException) {
        System.err.println("Error: ${error.message}")
        2
    } catch (error: Exception) {
        System.err.println("Generation failed: ${error.message}")
        1
    }

    private fun collectConfig(): PluginConfig {
        val reader by lazy { LineReaderBuilder.builder().build() }
        fun prompt(value: String?, promptText: String): String {
            if (value != null) return value.trim()
            return reader.readLine("$promptText ").trim()
        }

        val serviceId = prompt(service, "Enter Service ID (e.g., soundcloud):")
        require(serviceId.isNotBlank()) { "service ID is required (use -s/--service)." }
        val authorGroup = prompt(author, "Enter Author Group (e.g., io.github.KRTirtho):")
        require(authorGroup.isNotBlank()) { "author group is required (use -a/--author)." }
        val displayName = prompt(displayName, "Enter Service Display Name (e.g., SoundCloud):")
        require(displayName.isNotBlank()) { "service display name is required (use -d/--display-name)." }

        val selectedFeatures = if (capabilities.isNotEmpty()) {
            parseFeatures(capabilities)
        } else {
            println("Select one or more plugin APIs (comma-separated names or numbers):")
            PluginFeature.values().forEachIndexed { index, feature ->
                println("  ${index + 1}. ${feature.optionName} — ${feature.promptLabel}")
            }
            val entered = reader.readLine("Capabilities [metadata,audio]: ").trim()
            parseFeatures(entered.ifBlank { "metadata,audio" }.split(','))
        }

        return PluginConfig(serviceId, authorGroup, displayName, selectedFeatures)
    }
}

class TemplateEngine(private val context: Map<String, Any>) {
    private val compiler = Mustache.compiler()
        .escapeHTML(false)
        .emptyStringIsFalse(true)
        .zeroIsFalse(true)

    fun processTemplateDirectory(templateDir: File, outputDir: File): Long {
        var generatedFiles = 0L
        require(templateDir.isDirectory) { "template directory does not exist: $templateDir" }
        require(outputDir.mkdirs() || outputDir.isDirectory) { "could not create output directory: $outputDir" }

        try {
            templateDir.walkTopDown().forEach { source ->
                if (source == templateDir || source.isDirectory) return@forEach

                // First render the relative path, including Mustache tokens in directory/file names.
                val relativePath = source.relativeTo(templateDir).path
                val pathParts = relativePath.split(File.separatorChar).toMutableList()
                val optionalGroup = pathParts.firstOrNull { it in conditionalPathTokens }
                if (optionalGroup != null) {
                    val contextKey = optionalGroup.removePrefix("{{").removeSuffix("}}")
                    if (context[contextKey] != true) return@forEach
                    pathParts.remove(optionalGroup)
                }
                val renderablePath = pathParts.joinToString(File.separator)
                val targetRelativePath = compiler.compile(renderablePath).execute(context)
                if (targetRelativePath.isBlank()) return@forEach
                val target = File(outputDir, targetRelativePath)
                require(target.parentFile.mkdirs() || target.parentFile.isDirectory) {
                    "could not create output directory for $target"
                }

                // Then render text content; binary wrapper artifacts are copied byte-for-byte.
                if (isTextFile(source)) {
                    target.writeText(compiler.compile(source.readText()).execute(context))
                } else {
                    source.copyTo(target, overwrite = false)
                }

                if (source.name == "gradlew" || source.name == "gradlew.bat") target.setExecutable(true)
                generatedFiles++
            }
        } catch (error: Exception) {
            outputDir.deleteRecursively()
            throw error
        }

        return generatedFiles
    }

    private fun isTextFile(file: File): Boolean {
        val templateName = file.name.substringBefore("{{/")
        val extension = templateName.substringAfterLast('.', "").lowercase(Locale.ROOT)
        return templateName == ".gitignore" || templateName == "LICENSE" || extension in setOf(
            "kt", "kts", "json", "toml", "properties", "md", "txt", "gradle", "xml", "groovy",
        )
    }
}

fun main(args: Array<String>) {
    val exitCode = CommandLine(GenerateKmpPluginCommand()).execute(*args)
    if (exitCode != 0) kotlin.system.exitProcess(exitCode)
}

private fun validate(config: PluginConfig) {
    require(serviceIdPattern.matches(config.serviceId)) {
        "service ID must start with a lowercase letter and contain lowercase letters, numbers, and optional underscores (for example: ci_test)."
    }
    val authorSegments = config.authorGroup.split('.')
    require(authorSegments.all { packageSegmentPattern.matches(it) && it !in kotlinKeywords }) {
        "author group must be a valid dotted Kotlin package prefix (for example: io.github.myname)."
    }
    require(config.serviceDisplayName.isNotBlank()) { "service display name cannot be blank." }
    require(config.serviceDisplayName.none { it == '\n' || it == '\r' }) { "service display name cannot contain line breaks." }
    require(config.features.isNotEmpty()) { "select at least one plugin API capability." }
}

private fun parseFeatures(values: List<String>): Set<PluginFeature> {
    val selected = values.map { value ->
        val normalized = value.trim().lowercase(Locale.ROOT)
        PluginFeature.values().firstOrNull { feature ->
            normalized == feature.optionName || normalized == (feature.ordinal + 1).toString()
        } ?: throw IllegalArgumentException(
            "unknown capability '$value'; choose metadata, audio, lyrics, or scrobble."
        )
    }.toSet()
    require(selected.isNotEmpty()) { "select at least one plugin API capability." }
    return selected
}

private data class LocatedTemplates(val root: File, val cleanupRoot: File? = null)

private fun locateTemplateDirectory(): LocatedTemplates {
    val workingDirectoryTemplates = File("templates")
    if (isTemplateDirectory(workingDirectoryTemplates) && File("GenerateKmpPlugin.kt").isFile) {
        return LocatedTemplates(workingDirectoryTemplates)
    }

    val embeddedTemplateResource = GenerateKmpPluginCommand::class.java.classLoader.getResource("templates")
    if (embeddedTemplateResource != null) {
        when (embeddedTemplateResource.protocol) {
            "file" -> {
                val resourceDirectory = File(embeddedTemplateResource.toURI())
                if (resourceDirectory.isDirectory) return LocatedTemplates(resourceDirectory)
            }
            "jar" -> {
                val connection = embeddedTemplateResource.openConnection() as JarURLConnection
                connection.useCaches = false
                val extracted = connection.jarFile.use(::extractEmbeddedTemplates)
                if (extracted != null) return extracted
            }
        }
    }

    val codeLocation = File(GenerateKmpPluginCommand::class.java.protectionDomain.codeSource.location.toURI())
    if (codeLocation.isDirectory) {
        val adjacentTemplates = File(codeLocation, "templates")
        if (isTemplateDirectory(adjacentTemplates)) return LocatedTemplates(adjacentTemplates)
    }

    if (codeLocation.isFile) JarFile(codeLocation).use(::extractEmbeddedTemplates)?.let { return it }

    if (isTemplateDirectory(workingDirectoryTemplates)) return LocatedTemplates(workingDirectoryTemplates)

    val extractionRoot = Files.createTempDirectory("spotube-plugin-template-repository-").toFile()
    val checkout = File(extractionRoot, "repository")
    val repository = System.getenv("SPOTUBE_PLUGIN_TEMPLATE_REPOSITORY")
        ?: "https://github.com/team-spotube/spotube-plugin-template.git"
    val process = ProcessBuilder("git", "clone", "--depth=1", repository, checkout.absolutePath)
        .redirectErrorStream(true)
        .start()
    val cloneOutput = process.inputStream.bufferedReader().use { it.readText() }
    if (process.waitFor() == 0) {
        val clonedTemplates = File(checkout, "templates")
        if (isTemplateDirectory(clonedTemplates)) return LocatedTemplates(clonedTemplates, extractionRoot)
    }

    extractionRoot.deleteRecursively()
    throw IllegalArgumentException(
        "templates/ was not found locally or embedded in this script, and cloning $repository failed. " +
            "Check Git/network access or set SPOTUBE_PLUGIN_TEMPLATE_REPOSITORY. $cloneOutput"
    )
}

private fun isTemplateDirectory(directory: File): Boolean =
    directory.isDirectory && File(directory, "{{moduleName}}/build.gradle.kts").isFile

private fun extractEmbeddedTemplates(jar: JarFile): LocatedTemplates? {
    val extractionRoot = Files.createTempDirectory("spotube-plugin-templates-").toFile()
    var copiedEntries = 0
    val entries = jar.entries()
    while (entries.hasMoreElements()) {
        val entry = entries.nextElement()
        if (entry.isDirectory || !entry.name.startsWith("templates/")) continue

        val target = File(extractionRoot, entry.name).normalize()
        require(target.toPath().startsWith(extractionRoot.toPath())) { "invalid embedded template path: ${entry.name}" }
        target.parentFile.mkdirs()
        jar.getInputStream(entry).use { input -> target.outputStream().use { output -> input.copyTo(output) } }
        copiedEntries++
    }

    val embeddedTemplates = File(extractionRoot, "templates")
    if (copiedEntries > 0 && embeddedTemplates.isDirectory) return LocatedTemplates(embeddedTemplates, extractionRoot)
    extractionRoot.deleteRecursively()
    return null
}

private fun validateGeneratedProject(destination: File, config: PluginConfig) {
    val moduleRoot = File(destination, config.moduleName)
    val moduleBuildFile = File(moduleRoot, "build.gradle.kts").readText()
    require("spotubePlugin {" in moduleBuildFile) { "generated module is missing the current Spotube Gradle plugin DSL." }
    require("name = \"${jsonStringContent(config.serviceDisplayName)}\"" in moduleBuildFile) {
        "generated Spotube plugin name does not match the requested display name."
    }
    require("author = \"${jsonStringContent(config.authorGroup)}\"" in moduleBuildFile) {
        "generated Spotube plugin author does not match the requested author group."
    }

    val classFile = File(moduleRoot, "src/commonMain/kotlin/${config.packagePath}/core/${config.serviceClassName}.kt")
    require(classFile.isFile) { "generated service class is missing from the expected package path." }
    require("include(\":${config.moduleName}\")" in File(destination, "settings.gradle.kts").readText()) {
        "generated Gradle settings do not include the generated module."
    }
    require("mainFunction.set(\"${config.packageName}.main\")" in moduleBuildFile) {
        "generated Zipline entry point does not match the requested package."
    }

    destination.walkTopDown().filter { it.isFile && isTextFileForValidation(it) }.forEach { file ->
        val generatedText = file.readText()
        require("{{" !in generatedText) {
            "unrendered Mustache tag remains in ${file.relativeTo(destination)}: ${generatedText.take(120)}"
        }
    }
}

private fun isTextFileForValidation(file: File): Boolean = file.name == ".gitignore" || file.name == "LICENSE" ||
    file.extension.lowercase(Locale.ROOT) in setOf(
        "kt", "kts", "json", "toml", "properties", "md", "txt", "gradle", "xml", "groovy",
    )

private fun initializeGit(destination: File) {
    val process = ProcessBuilder("git", "init")
        .directory(destination)
        .inheritIO()
        .start()
    require(process.waitFor() == 0) { "git init failed in $destination." }
}

private fun jsonStringContent(value: String): String = JSONObject.quote(value).removeSurrounding("\"")
