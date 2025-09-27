package cz.uhk.monkify.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesManager(private val ds: DataStore<Preferences>) {

    /**
     * Read a value (maps missing → default).
     * @param key   the Preferences.Key<T> you want to read
     * @param default the default value if the key is not yet set
     */
    fun <T> getValue(key: Preferences.Key<T>, default: T): Flow<T> = ds.data.map { prefs ->
        prefs[key] ?: default
    }

    /**
     * Write or update a single key.
     * @param key   the Preferences.Key<T> you want to write
     * @param value the value to set
     */
    suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        ds.edit { prefs: MutablePreferences ->
            prefs[key] = value
        }
    }

    companion object Keys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("ONBOARDING_COMPLETED")
        val AUTHENTICATED = booleanPreferencesKey("AUTHENTICATED")
    }
}
