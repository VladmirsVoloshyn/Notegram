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

val backgroundMainDark = Color(0xFF212121)
val backgroundSecDark = Color(0xFF383838)

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

val completeGreen = Color(0xFF87D97F)
val errorRed = Color(0xFFDE5757)

val redL = Color(0xFFE12D2D)
val redAccent = Color(0xFF460D0D)
val greenL = Color(0xFF58EA20)
val greenAccent = Color(0xFF133A08)
val brownL = Color(0xFFA25B02)
val brownAccent = Color(0xFF2D1705)
val grayL = Color(0xFF606060)
val grayAccent = Color(0xFF1E1E1E)
val yellowL = Color(0xFFE1BA2D)
val yellowAccent = Color(0xFF251E03)
val blueL = Color(0xFF1EB7B1)
val blueAccent = Color(0xFF052A28)
val pinkL = Color(0xFFE450EA)
val pinkAccent = Color(0xFF451846)
val orangeL = Color(0xFFDA6920)
val orangeAccent = Color(0xFF41290B)
val purpleL = Color(0xFF651CD7)
val purpleAccent = Color(0xFF130834)




