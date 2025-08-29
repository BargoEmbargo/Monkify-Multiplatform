package cz.uhk.monkify.util

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter

object AppLog {
    inline fun <reified T> logger(level: Severity = Severity.Info): Logger {
        val cfg = loggerConfigInit(
            platformLogWriter(),
            minSeverity = level,
        )
        return Logger(config = cfg, tag = T::class.simpleName ?: "Unknown")
    }
}
