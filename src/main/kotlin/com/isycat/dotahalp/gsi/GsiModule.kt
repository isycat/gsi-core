package com.isycat.dotahalp.gsi

/**
 * Generic Game State Integration (GSI) base classes and utilities
 *
 * This module provides the foundation for GSI servers that can receive
 * game state updates from Source engine games. Game-specific implementations
 * should extend these base classes.
 *
 * Currently serves as a dependency marker for the dota-gsi module.
 * Generic GSI server abstractions will be extracted here as the codebase evolves.
 */
object GsiModule {
    const val VERSION = "v1.0.1"
}
