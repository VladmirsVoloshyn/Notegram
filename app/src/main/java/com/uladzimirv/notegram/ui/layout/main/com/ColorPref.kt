package com.uladzimirv.notegram.ui.layout.main.com

enum class ColorPref(val stringId: String) {
    COMMON("common"),
    ORANGE("orange"),
    CYAN("cyan"),
    GLOW("glow"),
    PINK("pink")
}

fun String.toColorNotePref(): ColorPref = when (this) {
    "common" -> ColorPref.COMMON
    "orange" -> ColorPref.ORANGE
    "cyan" -> ColorPref.CYAN
    "glow" -> ColorPref.GLOW
    "pink" -> ColorPref.PINK
    else -> ColorPref.COMMON
}
