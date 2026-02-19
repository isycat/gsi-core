package com.isycat.dotahalp.gsi

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

/**
 * Generic GSI (Game State Integration) Server for Source engine games
 *
 * This server receives HTTP POST requests from Source engine games (Dota 2, CS2, etc.)
 * containing JSON payloads with the current game state.
 *
 * @param T The game state type (e.g., DotaGameState, CS2GameState)
 * @param port The port to listen on
 * @param authToken The authentication token expected in requests
 * @param onGameStateReceived Callback invoked when a valid game state is received
 */
open class GsiServer<T : GsiGameState>(
    open val port: Int,
    open val authToken: String,
    open val gamestateClass: KClass<T>,
    private val onGameStateReceived: (T) -> Unit = {},
) {
    private var server: EmbeddedServer<*, *>? = null

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _lastGameState = MutableStateFlow<T?>(null)
    val lastGameState: StateFlow<T?> = _lastGameState.asStateFlow()

    private val _connectionStatus = MutableStateFlow("Not connected")
    val connectionStatus: StateFlow<String> = _connectionStatus.asStateFlow()

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    /**
     * Starts the GSI server
     */
    fun start() {
        if (_isRunning.value) {
            println("[GSI] Server is already running")
            return
        }

        server = embeddedServer(CIO, port = port) {
            install(ContentNegotiation) {
                json(json)
            }

            routing {
                route("/") {
                    post {
                        val timestamp = System.currentTimeMillis()
                        val remoteHost = call.request.local.remoteHost

                        try {
                            val gameState = call.receive(gamestateClass)

                            println("[GSI] Request from $remoteHost at $timestamp")

                            _lastGameState.value = gameState
                            _connectionStatus.value = "Connected - Last update: $timestamp"

                            // Validate auth token
                            if (gameState.auth?.token != authToken) {
                                println("[Dota GSI] Warning: Received request with invalid auth token")
                                call.respond(HttpStatusCode.Unauthorized)
                                return@post
                            }

                            println("[Dota GSI] Provider: " + gameState.provider?.friendlyName)
                            onGameStateReceived(gameState)

                            call.respond(HttpStatusCode.OK)
                        } catch (e: Exception) {
                            println("[GSI] Error processing game state from $remoteHost at $timestamp: ${e.message}")
                            e.printStackTrace()
                            call.respond(HttpStatusCode.BadRequest, "Error: ${e.message}")
                        }
                    }
                }

                get("/status") {
                    println("[GSI] Status check from ${call.request.local.remoteHost}")
                    call.respondText("GSI Server is running", ContentType.Text.Plain)
                }
            }
        }

        server?.start(wait = false)

        _isRunning.value = true
        _connectionStatus.value = "Listening on port $port"
        println("[GSI] Server started on port $port")
    }

    /**
     * Stops the GSI server
     */
    fun stop() {
        server?.stop(
            1000,
            2000,
            java.util.concurrent.TimeUnit.MILLISECONDS
        )
        server = null
        _isRunning.value = false
        _connectionStatus.value = "Server stopped"
        println("[GSI] Server stopped")
    }
}
