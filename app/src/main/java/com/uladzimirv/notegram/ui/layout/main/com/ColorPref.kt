package com.uladzimirv.notegram.ui.layout.main.com

enum class ColorPref(val stringId: String) {
    COMMON("common"),
    ORANGE("orange"),
    CYAN("cyan"),
    RED("red"),
    PINK("pink"),
    YELLOW("yellow"),
    BLACK("black"),
    GLOW("glow")
}

fun String.toColorNotePref(): ColorPref = when (this) {
    "common" -> ColorPref.COMMON
    "orange" -> ColorPref.ORANGE
    "cyan" -> ColorPref.CYAN
    "red" -> ColorPref.RED
    "pink" -> ColorPref.PINK
    "yellow" -> ColorPref.YELLOW
    "black" -> ColorPref.BLACK
    "glow" -> ColorPref.GLOW
    else -> ColorPref.COMMON
}
