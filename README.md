# GSI Core

Generic Game State Integration framework for any Source engine game (Dota 2, CS2, TF2, etc.)

## Features

- Generic GSI server implementation using Ktor
- Extensible model system for game-specific data
- Configuration file writer for GSI setup
- Coroutine-based event handling
- Type-safe game state models
- Compatible with any Source engine game that supports GSI

## Installation

### JitPack

Add JitPack repository to your `build.gradle.kts`:

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.isycat:gsi-core:v1.02")
    // GSI Core depends on steam-utils
    implementation("com.github.isycat:steam-utils:v1.0.0")
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.isycat</groupId>
        <artifactId>gsi-core</artifactId>
        <version>v1.02</version>
    </dependency>
    <dependency>
        <groupId>com.github.isycat</groupId>
        <artifactId>steam-utils</artifactId>
        <version>v1.02</version>
    </dependency>
</dependencies>
```

## Usage

```kotlin
import com.isycat.dotahalp.gsi.*
import kotlinx.coroutines.flow.collect

// Create a GSI server
val server = GsiServer(
    port = 3000,
    authToken = "my-secret-token"
)

// Start the server
server.start()

// Collect game state updates
server.gameStateFlow.collect { gameState ->
    println("Received game state: $gameState")
}

// Write GSI configuration file
val configWriter = GsiConfigWriter(
    uri = "http://localhost:3000",
    authToken = "my-secret-token"
)
configWriter.writeConfigFile(gameConfigDir)
```

## Architecture

- **GsiServer**: Ktor-based HTTP server that receives game state updates
- **GsiModule**: Dependency injection module for Spring/Koin
- **GsiConfigWriter**: Utility for generating GSI configuration files
- **Game State Models**: Extensible data classes for game state representation

## Building

```bash
./gradlew build
./gradlew test
```

## Requirements

- JDK 21+
- Kotlin 2.1.21+
- Gradle 8.5+

## Dependencies

- Ktor 3.0.0 (server framework)
- Kotlinx Serialization (JSON handling)
- Kotlinx Coroutines (async handling)
- Steam Utils (for locating game directories)

## Supported Games

While this is a generic framework, it's been tested with:
- Dota 2
- Counter-Strike 2
- Team Fortress 2

Any Source engine game with GSI support should work with minimal customization.

## License

MIT License - see LICENSE file for details

## Contributing

This module is part of the [DotaHALP](https://github.com/isycat/dota-halp) project but is maintained as a standalone library for reusability.

Contributions are welcome! Please open an issue or pull request.

## Related Projects

- [DotaHALP](https://github.com/isycat/dota-halp) - Dota 2 drafting assistant using this library
- [steam-utils](https://github.com/isycat/steam-utils) - Companion library for Steam directory location
