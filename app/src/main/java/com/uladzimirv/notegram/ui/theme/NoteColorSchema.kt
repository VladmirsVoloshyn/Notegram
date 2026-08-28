package com.uladzimirv.notegram.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref

@Immutable
class NoteColorSchema private constructor(
    val background: Color,
    val accent: Color,
    val dim: Color,
) {
    companion object Factory {

        @Composable
        fun fromPref(pref: ColorPref?): NoteColorSchema {
            val defBack = backgroundSecondary
            val defText = textPrimary
            val defDim = textSecondary
            return remember(pref) {
                when (pref) {
                    ColorPref.COMMON -> NoteColorSchema(
                        background = defBack,
                        accent = defText,
                        dim = defDim
                    )

                    ColorPref.ORANGE -> NoteColorSchema(
                        background = orange,
                        accent = orangeGray,
                        dim = orangeGrayDim
                    )

                    ColorPref.CYAN -> NoteColorSchema(
                        background = cyan,
                        accent = blackCyan,
                        dim = blackCyanDim
                    )

                    ColorPref.RED -> NoteColorSchema(
                        background = red,
                        accent = redYellow,
                        dim = redYellowDim
                    )

                    ColorPref.PINK -> NoteColorSchema(
                        background = pink,
                        accent = pinkGray,
                        dim = pinkGrayDim
                    )

                    ColorPref.YELLOW -> NoteColorSchema(
                        background = yellow,
                        accent = yellowBrown,
                        dim = yellowBrownDim
                    )

                    ColorPref.BLACK -> NoteColorSchema(
                        background = black,
                        accent = blackAcid,
                        dim = blackAcidDim
                    )

                    ColorPref.GLOW -> NoteColorSchema(
                        background = glow,
                        accent = glowBlue,
                        dim = glowBlueDim
                    )

                    else -> NoteColorSchema(
                        background = defBack,
                        accent = defText,
                        dim = defDim
                    )
                }
            }
        }
    }

}