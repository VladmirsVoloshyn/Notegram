package com.uladzimirv.notegram.domain.manager

import com.uladzimirv.notegram.data.preferences.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    private val _theme = MutableStateFlow(preferencesRepository.getTheme())
    val themeFlow: StateFlow<PreferencesRepository.ThemePreference> = _theme

    fun setTheme(
        theme: PreferencesRepository.ThemePreference
    ) {
        preferencesRepository.theme.value = theme.value
        _theme.value = theme
    }

}