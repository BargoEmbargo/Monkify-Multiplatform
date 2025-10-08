package cz.uhk.monkify.connectivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_queue_create

class IOSNetworkMonitor : NetworkMonitor {
    private val monitor = nw_path_monitor_create()
    private val queue = dispatch_queue_create("NetworkMonitor", null)

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> = _isConnected

    init {
        nw_path_monitor_set_update_handler(monitor) { path ->
            val status = nw_path_get_status(path)
            _isConnected.value = (status == nw_path_status_satisfied)
        }
        nw_path_monitor_set_queue(monitor, queue)
        nw_path_monitor_start(monitor)
    }

    override suspend fun recheck() {
        // NWPathMonitor is push-based; no manual recheck needed.
    }

    override fun dispose() {
        nw_path_monitor_cancel(monitor)
    }
}

@Composable
actual fun rememberNetworkMonitor(): NetworkMonitor {
    val monitor = remember { IOSNetworkMonitor() }
    DisposableEffect(Unit) {
        onDispose { monitor.dispose() }
    }
    return monitor
}
