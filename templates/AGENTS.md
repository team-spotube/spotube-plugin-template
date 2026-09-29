# {{serviceDisplayName}} Spotube Plugin

## Project

Kotlin Multiplatform Spotube plugin generated from the Spotube plugin template. API adapters follow the current `plugin_interfaces` contracts; provider-specific methods intentionally begin as TODOs.

## Build and test

```bash
./gradlew :{{moduleName}}:jsBrowserTest
./gradlew :{{moduleName}}:jsBrowserDistribution
./gradlew :{{moduleName}}:jvmTest
./gradlew :{{moduleName}}:check
```

## Structure

- `commonMain`: shared provider and Spotube API implementations
- `jsMain`: Zipline plugin entry point
- `jvmTest`: JVM integration tests
- `spotubePlugin {}` in the module Gradle build: Spotube plugin metadata

Package namespace: `{{packageName}}`
