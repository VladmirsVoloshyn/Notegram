package com.uladzimirv.notegram.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.uladzimirv.notegram.data.preferences.PreferencesRepository
import com.uladzimirv.notegram.data.preferences.PreferencesRepository.Companion.THEME_DARK
import com.uladzimirv.notegram.data.preferences.PreferencesRepository.Companion.THEME_LIGHT

enum class Theme {
    LIGHT,
    DARK,
}

object AppTheme {

    val backgroundPrimary: Color
        @Composable
        get() = when (getAppTheme()) {
            Theme.LIGHT -> backgroundMainGrayLight
            Theme.DARK -> backgroundMainDark
        }

    val backgroundSecondary: Color
        @Composable
        get() = when (getAppTheme()) {
            Theme.LIGHT -> backgroundMainLight
            Theme.DARK -> backgroundSecDark
        }

    val backgroundContainerDarker: Color
        @Composable
        get() = when (getAppTheme()) {
            Theme.LIGHT -> backgroundMainLight
            Theme.DARK -> backgroundMainLight
        }

    val textPrimary: Color
        @Composable
        get() = when (getAppTheme()) {
            Theme.LIGHT -> textPrimaryBlack
            Theme.DARK -> textPrimaryWhite
        }

    val textSecondary: Color
        @Composable
        get() = when (getAppTheme()) {
            Theme.LIGHT -> textSecondaryShadowGrey
            Theme.DARK -> textSecondaryGrey
        }


    val buttonPrimary: Color
        @Composable
        get() = when (getAppTheme()) {
            Theme.LIGHT -> buttonBlackPrimary
            Theme.DARK -> buttonLightPrimary
        }

    val buttonSecondary: Color
        @Composable
        get() = when (getAppTheme()) {
            Theme.LIGHT -> buttonLightPrimary
            Theme.DARK -> buttonBlackPrimary
        }

    val borderPrimary: Color
        @Composable
        get() = when (getAppTheme()) {
            Theme.LIGHT -> borderDarkPrimary
            Theme.DARK -> borderLightPrimary
        }


    val borderSecondary: Color
        @Composable
        get() = when (getAppTheme()) {
            Theme.LIGHT -> borderLightSecondary
            Theme.DARK -> borderDarkSecondary
        }

    val borderTertiary: Color
        @Composable
        get() = when (getAppTheme()) {
            Theme.LIGHT -> borderThinSecondary
            Theme.DARK -> borderThinSecondaryDark
        }


    private var preferencesRepository: PreferencesRepository? = null

    fun init(preferencesRepo: PreferencesRepository) {
        preferencesRepository = preferencesRepo
    }

    @Composable
    fun getAppTheme(): Theme {
        return when (preferencesRepository?.theme?.value) {
            THEME_LIGHT -> Theme.LIGHT
            THEME_DARK -> Theme.DARK
            else -> if (preferencesRepository?.getIsDarkTheme() == true) Theme.DARK else Theme.LIGHT
        }
    }
}


val backgroundMainGrayLight = Color(0xFFEFEFEF)
val backgroundMainLight = Color(0xFFFFFFFF)

val backgroundMainDark = Color(0xFF181818)
val backgroundSecDark = Color(0xFF2A2A2A)

val textPrimaryBlack = Color(0xFF181818)
val textSecondaryShadowGrey = Color(0xFFD2D2D2)

val textPrimaryWhite = Color(0xFFEFEFEF)
val textSecondaryGrey = Color(0xFFD9D9D9)

val buttonBlackPrimary = Color(0xFF2D2D2D)
val buttonLightPrimary = Color(0xFFCBCBCB)

val borderDarkPrimary = Color(0xFFC4C4C4)
val borderLightSecondary = Color(0xFFE7E7E7)
val borderThinSecondary = Color(0xFFF6F6F6)

val borderLightPrimary = Color(0xFF2C2C2C)
val borderDarkSecondary = Color(0xFF414141)
val borderThinSecondaryDark = Color(0xFF2F2F2F)


val pink = Color(0xFFFF6095)
val pinkGray = Color(0xFFEAEAEA)
val pinkGrayDim = Color(0xFFC4C4C4)

val orange = Color(0xFFFF5E0F)
val orangeGray = Color(0xFF2C2D2F)
val orangeGrayDim = Color(0xFF3E3E3F)

val yellow = Color(0xFFF1DD8D)
val yellowBrown = Color(0xFF382500)
val yellowBrownDim = Color(0xFF503408)

val black = Color(0xFF000000)
val blackAcid = Color(0xFF3BFF00)
val blackAcidDim = Color(0xFFB9F1B1)

val glow = Color(0xFFF2E9CC)
val glowBlue = Color(0xFF1600A2)
val glowBlueDim = Color(0xFF9693B4)

val red = Color(0xFFA40E0E)
val redYellow = Color(0xFFF9E07D)
val redYellowDim = Color(0xFFEFE0D4)

val cyan = Color(0xFF61D2D2)
val blackCyan = Color(0xFF000000)
val blackCyanDim = Color(0xFF818181)



