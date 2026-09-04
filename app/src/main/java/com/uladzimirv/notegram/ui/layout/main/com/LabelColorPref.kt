package com.uladzimirv.notegram.ui.layout.main.com


enum class LabelColorPref(val stringId: String) {
    COMMON("common"),
    ORANGE("orange"),
    BLUE("blue"),
    RED("red"),
    PINK("pink"),
    YELLOW("yellow"),
    GRAY("gray"),
    GREEN("green"),
    BROWN("brown"),
    PURPLE("purple")
}

fun String.toColorLabelPref(): LabelColorPref = when (this) {
    "common" -> LabelColorPref.COMMON
    "orange" -> LabelColorPref.ORANGE
    "blue" -> LabelColorPref.BLUE
    "red" -> LabelColorPref.RED
    "pink" -> LabelColorPref.PINK
    "yellow" -> LabelColorPref.YELLOW
    "gray" -> LabelColorPref.GRAY
    "green" -> LabelColorPref.GREEN
    "brown" -> LabelColorPref.BROWN
    "purple" -> LabelColorPref.PURPLE
    else -> LabelColorPref.COMMON
}
