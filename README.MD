# Spotube KMP Plugin Generator

Scaffold a Kotlin Multiplatform provider plugin for Spotube. The generated project includes TODO-based adapters for the selected Spotube APIs; implement the provider-specific behavior after generation.

## Requirements

- [JBang](https://www.jbang.dev/)
- Java 21 or newer
- Git

## Create a plugin

From a local checkout, run the interactive wizard:

```bash
jbang GenerateKmpPlugin.kt
```

Or provide the service identity and API selection directly:

```bash
jbang GenerateKmpPlugin.kt \
  --service lastfm \
  --author cc.spotube.lastfm \
  --display-name "Last.fm" \
  --capabilities metadata,audio,lyrics,scrobble \
  --output spotube_plugin_lastfm
```

The wizard prompts for a service ID, author package group, display name, and one or more APIs: `metadata`, `audio`, `lyrics`, and `scrobble`. Press Enter at the API prompt to select Metadata and Audio. If `--output` is omitted, the project is created in `./spotube_plugin_<service-id>`. Existing output directories are not overwritten.

For a published JBang catalog, the generator is also available as:

```bash
jbang create@team-spotube/spotube-plugin-template --service lastfm --author cc.spotube.lastfm --display-name "Last.fm" --capabilities metadata,audio
```

Use `jbang GenerateKmpPlugin.kt --help` for all options. Generated projects initialize their own Git repository. Logos and provider-specific endpoints can be added or customized after generation.
