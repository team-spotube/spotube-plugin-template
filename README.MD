# Spotube KMP Plugin Generator

Create a Kotlin Multiplatform plugin scaffold for Spotube. The generator uses JBang, Picocli, JLine, and JMustache; the templates follow the current `plugin_interfaces` APIs and Spotube Gradle plugin DSL.

Generated API implementations are intentionally TODOs. The scaffold wires the selected APIs into Zipline so you can implement the provider-specific behavior yourself.

## Requirements

- [JBang](https://www.jbang.dev/)
- Java 21 or newer
- Git

JBang resolves the CLI dependencies automatically. Git is used to initialize a repository in the generated project. If the Gradle plugin or Spotube plugin-interface artifacts are not available from their configured repositories, publish or otherwise make them available before building the generated project.

## Create a plugin

From a local checkout of this repository, run the wizard:

```bash
jbang GenerateKmpPlugin.kt
```

The wizard asks for a service ID, author package group, display name, and a multi-select of plugin APIs. Press Enter at the capability prompt to accept the default **Metadata + Audio** selection.

You can also run it non-interactively:

```bash
jbang GenerateKmpPlugin.kt \
  --service lastfm \
  --author cc.spotube.lastfm \
  --display-name "Last.fm" \
  --capabilities metadata,audio,lyrics,scrobble \
  --output spotube_plugin_lastfm
```

The `--capabilities` option accepts any comma-separated, non-empty combination of `metadata`, `audio`, `lyrics`, and `scrobble`. Names are case-insensitive; the interactive prompt also accepts the displayed numbers. Existing output directories are never overwritten. If `--output` is omitted, the project is created as `./spotube_plugin_<service-id>`.

### Inputs and generated identity

| Input | Example | Used for |
| --- | --- | --- |
| Service ID | `lastfm` | Module name, service class name, and service identifier |
| Author group | `cc.spotube.lastfm` | Prefix of the Kotlin package |
| Display name | `Last.fm` | Plugin display name and generated documentation |
| API selection | `metadata,lyrics` | Generated adapters, tests, Zipline bindings, and plugin abilities |

The generator derives the remaining identity values:

- Module: `spotube_plugin_lastfm`
- Service class: `Lastfm`
- Kotlin package: `cc.spotube.lastfm.spotube_plugin_lastfm`
- Gradle root project: `spotube_plugin_lastfm-project`

Service IDs must start with a lowercase letter and contain lowercase letters, digits, and optional underscores (for example, `youtube_music`). The author group must be a valid dotted Kotlin package prefix.

### Run from the JBang catalog

From a local checkout:

```bash
jbang create@. --service lastfm --author cc.spotube.lastfm --display-name "Last.fm" --capabilities metadata,audio
```

Once the updated repository is pushed to GitHub, the remote catalog alias can be used from any directory:

```bash
jbang create@team-spotube/spotube-plugin-template \
  --service lastfm \
  --author cc.spotube.lastfm \
  --display-name "Last.fm" \
  --capabilities metadata,audio
```

The first remote run may ask you to trust the JBang source. The script uses bundled template resources when available and otherwise fetches the template repository with Git. For a fork, set `SPOTUBE_PLUGIN_TEMPLATE_REPOSITORY` to its Git clone URL. If you see `templates/ was not found`, make sure the latest generator changes have been pushed before invoking the remote alias.

Use `jbang GenerateKmpPlugin.kt --help` for the complete option list.

## What the generator creates

Every generated project includes the common service/client boundary, Core API adapter, JS Zipline entry point, Gradle wrapper, current `spotubePlugin {}` metadata, and a Git repository. Selected APIs add their own adapters and JVM test placeholders:

| API | Generated pieces |
| --- | --- |
| Metadata | Album, Artist, Browse, Playlist, Search, Track, and User adapters and tests |
| Audio | Audio adapter and tests; takes the host `CryptoAPI` in the Zipline entry point |
| Lyrics | Lyrics adapter and JVM test placeholder |
| Scrobble | Scrobble adapter and JVM test placeholder |

Only selected APIs are included in `spotubePlugin.abilities`, `js.kt` imports/bindings, and the generated source/test paths. The host permissions in `spotubePlugin.capabilities` (network requests, persistent storage, and WebView) remain enabled. The `RealLyricsAPI` and `RealScrobbleAPI` stubs implement the current interfaces, not the older methods in the example plugin.

All provider API methods are marked `TODO` for customization. There is no BuildKonfig plugin, generated `BuildKonfig` class, required cookie, or `local.properties` setup. A logo can be added later by editing the generated project.

## Template authoring

Templates live in `templates/`:

- `{{moduleName}}`, `{{packagePath}}`, and `{{serviceClassName}}` in paths are rendered into the generated project paths and filenames.
- `{{variable}}` tags in file contents are rendered with the plugin identity data.
- Kotlin string interpolation (`${...}`) is left unchanged by Mustache.
- `{{includeMetadata}}`, `{{includeAudio}}`, `{{includeLyrics}}`, and `{{includeScrobble}}` path segments include or omit optional source/test files. Mustache sections in `js.kt` and the module build file conditionally add API bindings and abilities.
- Text files are rendered; binary Gradle wrapper artifacts are copied directly, and `gradlew` is made executable.

## Verification

The GitHub Actions workflow exercises the local catalog alias, validates an all-API scaffold and a Lyrics/Scrobble-only scaffold, and checks the generated Gradle wrapper. It does not run a full Gradle build on GitHub; the custom Spotube Gradle plugin and `plugin_interfaces` artifacts must be available to the generated build.

For a generated project, after making any required Spotube artifacts resolvable, run:

```bash
./gradlew :spotube_plugin_<service-id>:jvmTest
./gradlew :spotube_plugin_<service-id>:jsBrowserTest
./gradlew :spotube_plugin_<service-id>:jsBrowserDistribution
```
