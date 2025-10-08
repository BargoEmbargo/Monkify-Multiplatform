package cz.uhk.monkify.connectivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import cz.uhk.monkify.util.Constants.CONNECTIVITY_CHECK_URL
import java.net.HttpURLConnection
import java.net.URI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DesktopNetworkMonitor : NetworkMonitor {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _isConnected = MutableStateFlow(checkOnce())
    override val isConnected: StateFlow<Boolean> = _isConnected

    init {
        scope.launch {
            while (isActive) {
                _isConnected.emit(checkOnce())
                delay(2_000)
            }
        }
    }

    private fun checkOnce(): Boolean = try {
        val url = URI(CONNECTIVITY_CHECK_URL).toURL()
        (url.openConnection() as HttpURLConnection).run {
            connectTimeout = 1500
            readTimeout = 1500
            requestMethod = "HEAD"
            connect()
            responseCode in 200..399
        }
    } catch (_: Exception) {
        false
    }

    override suspend fun recheck() {
        _isConnected.emit(checkOnce())
    }

    override fun dispose() {
        scope.cancel()
    }
}

@Composable
actual fun rememberNetworkMonitor(): NetworkMonitor {
    val monitor = remember { DesktopNetworkMonitor() }
    DisposableEffect(Unit) {
        onDispose { monitor.dispose() }
    }
    return monitor
}
