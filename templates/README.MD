# {{serviceDisplayName}} for Spotube

Spotube music service provider plugin for {{serviceDisplayName}}.

This project was scaffolded from the Spotube KMP plugin template. API adapters follow the current `plugin_interfaces` contracts and contain TODOs for provider-specific behavior.

## Build

```bash
./gradlew :{{moduleName}}:packageDevelopmentPlugin
```

Plugin metadata is declared through the `spotubePlugin {}` Gradle DSL. Add provider-specific credentials/configuration only if needed by your implementation, and keep secrets out of source control.

If the plugin logs in through the WebView, list every host it reads cookies from (`webViewAPI.getCookies`) or runs scripts on (`webViewAPI.evaluateJavaScript`) in `cookieDomains`. Spotube refuses those calls for any other host and shows the list to users when they install the plugin.

## License

See [LICENSE](LICENSE).
