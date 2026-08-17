package com.uladzimirv.notegram.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class Theme {
    LIGHT,
//    DARK,
//    MINT,
//    WINE
}

val backgroundPrimary: Color
    @Composable
    get() = when (getAppTheme()) {
        Theme.LIGHT -> backgroundMainGrayLight
    }

val backgroundSecondary: Color
    @Composable
    get() = when (getAppTheme()) {
        Theme.LIGHT -> backgroundMainLight
    }

val backgroundContainerDarker: Color
    @Composable
    get() = when (getAppTheme()) {
        Theme.LIGHT -> backgroundMainLight
    }

val textPrimary: Color
    @Composable
    get() = when (getAppTheme()) {
        Theme.LIGHT -> textPrimaryBlack
    }

val textSecondary: Color
    @Composable
    get() = when (getAppTheme()) {
        Theme.LIGHT -> textSecondaryShadowGrey
    }


val buttonPrimary: Color
    @Composable
    get() = when (getAppTheme()) {
        Theme.LIGHT -> buttonBlackPrimary
    }

val buttonSecondary: Color
    @Composable
    get() = when (getAppTheme()) {
        Theme.LIGHT -> buttonLightPrimary
    }

val borderPrimary: Color
    @Composable
    get() = when (getAppTheme()) {
        Theme.LIGHT -> borderDarkPrimary
    }


val borderSecondary: Color
    @Composable
    get() = when (getAppTheme()) {
        Theme.LIGHT -> borderLightSecondary
    }




@Composable
fun getAppTheme(): Theme {
    return Theme.LIGHT
}

val backgroundMainGrayLight = Color(0xFFEFEFEF)
val backgroundMainLight = Color(0xFFFFFFFF)

val textPrimaryBlack = Color(0xFF181818)
val textSecondaryShadowGrey = Color(0xFFD2D2D2)

val buttonBlackPrimary = Color(0xFF2D2D2D)
val buttonLightPrimary = Color(0xFFCBCBCB)
val dark = Color(0xFFA1A1A1)

val borderDarkPrimary = Color(0xFFC4C4C4)
val borderLightSecondary = Color(0xFFEAEAEA)

val orange = Color(0xFFF59C8B)
val cyan = Color(0xFFA5E7E7)
val glow = Color(0xFF8197E8)
val pink = Color(0xFFE080A4)


