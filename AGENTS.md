# Generator development

- Main CLI: `GenerateKmpPlugin.kt` (JBang script)
- JMustache source templates: `templates/`
- Catalog alias: `jbang create@.`
- CI: `.github/workflows/ci.yml`

Use Java 21 and verify changes with:

```bash
jbang GenerateKmpPlugin.kt --help
jbang GenerateKmpPlugin.kt --service ci_test --author io.github.test --display-name CiTest --capabilities metadata,audio --output /tmp/spotube-plugin-ci-test
```

Do not copy local properties, repository metadata, or this generator into generated plugin projects. Use the current `spotubePlugin {}` Gradle DSL and `plugin_interfaces` API signatures; keep provider-specific implementations as TODO stubs until users fill them in.
