import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("app_preferences")

object PreferencesManager {

    private fun stringPreferencesKey(name: String): Preferences.Key<String> {
        return androidx.datastore.preferences.core.stringPreferencesKey(name)
    }

    fun getCachedScreenData(context: Context, screen: String): Flow<String?> {
        val screenKey = stringPreferencesKey(screen)
        return context.dataStore.data.map { preferences ->
            preferences[screenKey]
        }
    }

    suspend fun saveScreenData(context: Context, screen: String, data: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(screen)] = data
        }
    }

    suspend fun obtenerUltimaPantalla(context: Context): String? {
        val screenKey = stringPreferencesKey("ultima_pantalla")
        return context.dataStore.data.map { preferences ->
            preferences[screenKey]
        }.firstOrNull()
    }

    suspend fun guardarUltimaPantalla(context: Context, screen: String) {
        val screenKey = stringPreferencesKey("ultima_pantalla")
        context.dataStore.edit { preferences ->
            preferences[screenKey] = screen
        }
    }

    fun getVisitedScreens(context: Context): Flow<Set<String>> {
        val visitedScreensKey = stringPreferencesKey("visited_screens")
        return context.dataStore.data.map { preferences ->
            preferences[visitedScreensKey]?.split(",")?.toSet() ?: emptySet()
        }
    }

    suspend fun saveVisitedScreen(context: Context, screen: String) {
        val visitedScreensKey = stringPreferencesKey("visited_screens")
        context.dataStore.edit { preferences ->
            val currentSet = preferences[visitedScreensKey]?.split(",")?.toMutableSet() ?: mutableSetOf()
            currentSet.add(screen)
            preferences[visitedScreensKey] = currentSet.joinToString(",")
        }
    }
}

