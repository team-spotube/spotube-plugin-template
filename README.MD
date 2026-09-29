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

The wizard asks for a service ID, author package group, display name, contact, repository URL, bugs URL, and one or more APIs. Press Enter at the API prompt to select Metadata and Audio; the bugs URL defaults to `<repository>/issues`.

You can also supply all values directly:

```bash
jbang GenerateKmpPlugin.kt \
  --service lastfm \
  --author cc.spotube.lastfm \
  --display-name "Last.fm" \
  --contact "maintainer@example.com" \
  --repository https://github.com/owner/spotube-plugin-lastfm \
  --bugs https://github.com/owner/spotube-plugin-lastfm/issues \
  --capabilities metadata,audio,lyrics,scrobble \
  --output spotube_plugin_lastfm
```

The contact can be an email address or maintainer label. Repository and bugs URLs must be HTTP or HTTPS. `--capabilities` accepts a comma-separated, non-empty combination of `metadata`, `audio`, `lyrics`, and `scrobble`; `--bugs` may be omitted to use the repository's `/issues` URL. Existing output directories are not overwritten. The default output is `./spotube_plugin_<service-id>`.

## JBang catalog

For a published catalog, run:

```bash
jbang create@team-spotube/spotube-plugin-template \
  --service lastfm \
  --author cc.spotube.lastfm \
  --display-name "Last.fm" \
  --contact "maintainer@example.com" \
  --repository https://github.com/owner/spotube-plugin-lastfm \
  --bugs https://github.com/owner/spotube-plugin-lastfm/issues \
  --capabilities metadata,audio
```

From a local checkout, use `jbang create@.` instead. Use `jbang GenerateKmpPlugin.kt --help` for all options. Generated projects initialize their own Git repository. Logos and provider-specific endpoints can be customized after generation.
