package com.uladzimirv.notegram.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.black
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.blackAcid
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.blackAcidDim
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.blackCyan
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.cyan
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.glow
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.glowBlue
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.glowBlueDim
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.green
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.greenOrange
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.greenOrangeDim
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.orange
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.orangeGray
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.orangeGrayDim
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.pink
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.pinkGray
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.pinkGrayDim
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.red
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.redYellow
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.redYellowDim
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.yellow
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.yellowBrown
import com.uladzimirv.notegram.ui.layout.main.com.NoteSchema.yellowBrownDim
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.AppTheme.textPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.textSecondary

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
                        dim = NoteSchema.blackCyanDim
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

//                    ColorPref.BLACK -> NoteColorSchema(
//                        background = black,
//                        accent = blackAcid,
//                        dim = blackAcidDim
//                    )

                    ColorPref.GLOW -> NoteColorSchema(
                        background = glow,
                        accent = glowBlue,
                        dim = glowBlueDim
                    )

                    ColorPref.GREEN -> NoteColorSchema(
                        background = green,
                        accent = greenOrange,
                        dim = greenOrangeDim
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