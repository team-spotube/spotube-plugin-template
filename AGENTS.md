# Generator development

- Main CLI: `GenerateKmpPlugin.kt` (JBang script)
- JMustache source templates: `templates/`
- Catalog alias: `jbang create@.`
- CI: `.github/workflows/ci.yml`

## Generator contract

- Inputs: service ID, author package group, display name, and a non-empty selection of `metadata`, `audio`, `lyrics`, and `scrobble`.
- Default selected APIs are Metadata + Audio. The `--capabilities` option accepts a comma-separated list; interactive input accepts names or menu numbers.
- Derived values: `moduleName = spotube_plugin_<serviceId>`, service class is PascalCase, and `packageName = <authorGroup>.<moduleName>`.
- API selections map to `spotubePlugin.abilities` and conditionally control source/test paths plus imports/bindings in `js.kt`. Host permissions stay NETWORK_REQUESTS, PERSISTENT_STORAGE, and WEBVIEW.
- Generated metadata uses the `spotubePlugin {}` DSL. Do not reintroduce BuildKonfig, a generated cookie constant, or `plugin.json`.
- Keep API implementations as TODO stubs matching the sibling `spotube/plugin_interfaces` contracts. Lyrics uses `LyricsAPI.getLyrics(MetadataTrack)` and Scrobble uses `ScrobbleAPI.scrobble(ScrobbleTrack)`.

## Template and remote execution notes

- Mustache content tags use `{{variable}}`; Kotlin `${...}` interpolation must remain literal.
- Optional file paths use `{{includeMetadata}}`, `{{includeAudio}}`, `{{includeLyrics}}`, or `{{includeScrobble}}` path segments. The template engine removes the selected marker or skips the file.
- Do not add `//FILES templates/` to the JBang script: remote JBang treats the directory as an individual raw URL and fails to download it. `locateTemplateDirectory()` uses the local checkout, embedded resources when available, or clones `https://github.com/team-spotube/spotube-plugin-template.git`. Forks can set `SPOTUBE_PLUGIN_TEMPLATE_REPOSITORY`.
- Generated builds require Java 21 and may need the Spotube Gradle plugin and `plugin_interfaces` artifacts available from their configured repositories.

Use Java 21 and verify changes with:

```bash
jbang GenerateKmpPlugin.kt --help
jbang GenerateKmpPlugin.kt --service ci_test --author io.github.test --display-name CiTest --contact ci@example.com --repository https://github.com/team-spotube/spotube-plugin-template --bugs https://github.com/team-spotube/spotube-plugin-template/issues --capabilities metadata,audio,lyrics,scrobble --output /tmp/spotube-plugin-ci-test
```

Also verify a partial selection (for example, `--capabilities lyrics,scrobble`) omits unselected APIs and their test directories. Do not copy repository metadata or this generator into generated plugin projects.
