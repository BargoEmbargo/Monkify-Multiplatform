package cz.uhk.monkify.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AndroidNetworkMonitor(context: Context) : NetworkMonitor {
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isConnected = MutableStateFlow(currentConnected())
    override val isConnected: StateFlow<Boolean> = _isConnected

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isConnected.tryEmit(true)
        }
        override fun onLost(network: Network) {
            _isConnected.tryEmit(currentConnected())
        }
        override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
            _isConnected.tryEmit(
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED),
            )
        }
    }

    init {
        cm.registerDefaultNetworkCallback(callback)
    }

    private fun currentConnected(): Boolean {
        val active = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(active) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    override suspend fun recheck() {
        _isConnected.emit(currentConnected())
    }

    override fun dispose() {
        runCatching { cm.unregisterNetworkCallback(callback) }
    }
}

@Composable
actual fun rememberNetworkMonitor(): NetworkMonitor {
    val context = LocalContext.current
    val monitor = remember { AndroidNetworkMonitor(context) }
    DisposableEffect(Unit) {
        onDispose { monitor.dispose() }
    }
    return monitor
}
