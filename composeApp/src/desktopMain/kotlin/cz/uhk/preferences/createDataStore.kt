package cz.uhk.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cz.uhk.monkify.preferences.DATA_STORE_FILE_NAME
import cz.uhk.monkify.preferences.createDataStore
import java.io.File

fun createDataStore(): DataStore<Preferences> = createDataStore {
    File(System.getProperty("java.io.tmpdir"), DATA_STORE_FILE_NAME)
        .absolutePath
}
