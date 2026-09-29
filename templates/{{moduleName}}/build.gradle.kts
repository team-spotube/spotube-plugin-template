import dev.krtirtho.PluginAbility
import dev.krtirtho.PluginCapability
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.zipline.gradle.plugin)
    alias(libs.plugins.spotubeGradle)
}

spotubePlugin {
    name = "{{serviceDisplayNameKotlin}}"
    version = "0.1.0"
    apiVersion = "0.0.1"
    description = "Spotube music service provider plugin for {{serviceDisplayNameKotlin}}"
    author = "{{authorGroupKotlin}}"
    capabilities = listOf(
        PluginCapability.NETWORK_REQUESTS,
        PluginCapability.PERSISTENT_STORAGE,
        PluginCapability.WEBVIEW
    )
    abilities = listOf(
        {{#includeMetadata}}PluginAbility.METADATA,{{/includeMetadata}}
        {{#includeAudio}}PluginAbility.AUDIO,{{/includeAudio}}
        {{#includeLyrics}}PluginAbility.LYRICS,{{/includeLyrics}}
        {{#includeScrobble}}PluginAbility.SCROBBLE,{{/includeScrobble}}
    )
    license = "AGPL-3.0-or-later"
}

kotlin {
    applyDefaultHierarchyTemplate()

    js {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        binaries.executable()
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            api(libs.zipline.core)
            implementation(libs.spotube.plugin.interfaces)
            implementation(libs.semver)
        }

        jsMain.dependencies {
        }

        jvmTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.cryptography.core)
            implementation(libs.cryptography.provider.optimal)
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }

    }
}

zipline {
    mainFunction.set("{{packageName}}.main")
}

plugins.withType<YarnPlugin> {
    the<YarnRootExtension>().yarnLockAutoReplace = true
}
