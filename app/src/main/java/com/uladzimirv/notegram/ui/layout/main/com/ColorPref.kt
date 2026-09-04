package com.uladzimirv.notegram.ui.layout.main.com

import androidx.compose.ui.graphics.Color

enum class ColorPref(val stringId: String) {
    COMMON("common"),
    ORANGE("orange"),
    CYAN("cyan"),
    RED("red"),
    PINK("pink"),
    YELLOW("yellow"),
    //BLACK("black"),
    GREEN("green"),
    GLOW("glow")
}

fun String.toColorNotePref(): ColorPref = when (this) {
    "common" -> ColorPref.COMMON
    "orange" -> ColorPref.ORANGE
    "cyan" -> ColorPref.CYAN
    "red" -> ColorPref.RED
    "pink" -> ColorPref.PINK
    "yellow" -> ColorPref.YELLOW
    //"black" -> ColorPref.BLACK
    "glow" -> ColorPref.GLOW
    "green" -> ColorPref.GREEN
    else -> ColorPref.COMMON
}

object NoteSchema {
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

    val green = Color(0xFF618D2A)
    val greenOrange = Color(0xFFFFB29D)
    val greenOrangeDim = Color(0xFFC0907F)
}
