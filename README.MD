# Spotube KMP Plugin Generator

JBang-powered scaffold generator for Spotube Kotlin Multiplatform provider plugins. The JMustache templates use the current `plugin_interfaces` contracts and generate TODO-based provider implementations with parameterized service and package identity.

## Requirements

- [JBang](https://www.jbang.dev/)
- Java 21 (matches the Gradle toolchain in generated projects)
- Git (the generator initializes a repository in each generated project)

## Generate a plugin

Run from the repository root. The wizard prompts for any missing values:

```bash
jbang GenerateKmpPlugin.kt
```

Or provide values directly:

```bash
jbang GenerateKmpPlugin.kt \
  --service soundcloud \
  --author io.github.myname \
  --display-name SoundCloud \
  --capabilities metadata,audio \
  --output spotube_plugin_soundcloud
```

The wizard also offers a multi-select for Metadata, Audio, Lyrics, and Scrobble APIs (default: Metadata and Audio). These selections populate `spotubePlugin.abilities`; the network/storage/webview permissions in `spotubePlugin.capabilities` remain enabled. Use `--capabilities` with a comma-separated subset for non-interactive runs. Source files, JVM tests, Zipline bindings, and Gradle plugin abilities are generated only for selected APIs. The output defaults to `./spotube_plugin_<serviceId>`; existing output directories are not overwritten. Logo setup is intentionally left for users to add later.

Use `jbang GenerateKmpPlugin.kt --help` for all options. The repository also provides a local JBang catalog alias:

```bash
jbang create@. --service soundcloud --author io.github.myname --display-name SoundCloud
```

## Template structure

Templates live in `templates/`. Mustache tags in file paths and content (`{{moduleName}}`, `{{packagePath}}`, `{{serviceClassName}}`, and other identity variables) are rendered with the complete data model. Kotlin's `${...}` interpolation remains unchanged. Binary Gradle wrapper files are copied byte-for-byte.

Generated plugin metadata uses the current `spotubePlugin {}` Gradle DSL from the sibling Spotube Gradle plugin. Service API implementations are deliberately TODO stubs based on the current `plugin_interfaces` contracts.

The smoke-test workflow validates generated structure and the Gradle wrapper. A full Gradle build may require locally published Spotube Gradle-plugin and plugin-interface artifacts, as in the original plugin project's build setup.
