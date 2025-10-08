package cz.uhk.monkify.connectivity

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow

interface NetworkMonitor {
    val isConnected: StateFlow<Boolean>
    suspend fun recheck()
    fun dispose()
}

@Composable
expect fun rememberNetworkMonitor(): NetworkMonitor
