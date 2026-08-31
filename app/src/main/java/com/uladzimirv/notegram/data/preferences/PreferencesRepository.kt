package com.uladzimirv.notegram.data.preferences

import android.app.Application
import android.app.UiModeManager
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.uladzimirv.notegram.util.PreferenceString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject constructor(
    private val app: Application
) {

    val theme = PreferenceString(key = "theme", def = THEME_SYSTEM, store = getStore())

    private fun getStore(): SharedPreferences = try {
        createEncryptedSharedPreferences()
    } catch (e: Exception) {
        app.deleteSharedPreferences(SHARED_PREFERENCE_NAME)
        createEncryptedSharedPreferences()
    }

    private fun createEncryptedSharedPreferences(): SharedPreferences {
        return EncryptedSharedPreferences.create(
            app,
            SHARED_PREFERENCE_NAME,
            getMasterKey(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getMasterKey(): MasterKey {
        return MasterKey.Builder(app)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private fun isSystemDeviceDarkMode(): Boolean {
        val uiModeManager = app.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val isDark = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
        return isDark
    }

    fun getIsDarkTheme(): Boolean {
        return when (theme.value) {
            THEME_LIGHT -> false
            THEME_DARK -> true
            else -> isSystemDeviceDarkMode()
        }
    }

    fun getTheme(): ThemePreference {
        return when (theme.value) {
            THEME_LIGHT -> ThemePreference.LIGHT
            THEME_DARK -> ThemePreference.DARK
            else -> ThemePreference.SYSTEM
        }
    }

    enum class ThemePreference(val value: String) {
        DARK(THEME_DARK),
        LIGHT(THEME_LIGHT),
        SYSTEM(THEME_SYSTEM)
    }

    companion object {
        const val SHARED_PREFERENCE_NAME = "store"

        const val THEME_SYSTEM = "system"
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
    }


}