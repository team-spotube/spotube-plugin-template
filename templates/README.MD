# {{serviceDisplayName}} for Spotube

Spotube music service provider plugin for {{serviceDisplayName}}.

This project was scaffolded from the Spotube KMP plugin template. API adapters follow the current `plugin_interfaces` contracts and contain TODOs for provider-specific behavior.

## Build

```bash
./gradlew :{{moduleName}}:packageDevelopmentPlugin
```

Plugin metadata is declared through the `spotubePlugin {}` Gradle DSL. Add provider-specific credentials/configuration only if needed by your implementation, and keep secrets out of source control.

## License

See [LICENSE](LICENSE).
