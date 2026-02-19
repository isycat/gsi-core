package com.isycat.dotahalp.gsi

import java.io.File

/**
 * Base interface for writing GSI configuration files to game directories
 *
 * Each Source engine game has its own config file format and location.
 * Implement this interface to provide game-specific config generation.
 */
interface GsiConfigWriter {
    /**
     * Generates the GSI configuration file content for this game
     *
     * @param port The port the GSI server is listening on
     * @param authToken The authentication token
     * @return The configuration file content as a string
     */
    fun generateConfigContent(port: Int, authToken: String): String

    /**
     * Gets the path where the configuration file should be placed
     *
     * @return The file path for the GSI config
     */
    fun getConfigFilePath(): File

    /**
     * Writes the configuration file to the appropriate location
     *
     * @param port The port the GSI server is listening on
     * @param authToken The authentication token
     * @return True if the file was written successfully
     */
    fun writeConfigFile(port: Int, authToken: String): Boolean {
        return try {
            val configFile = getConfigFilePath()
            configFile.parentFile?.mkdirs()
            configFile.writeText(generateConfigContent(port, authToken))
            println("[GSI Config Writer] Wrote config to ${configFile.absolutePath}")
            true
        } catch (e: Exception) {
            println("[GSI Config Writer] Error writing config file: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}
