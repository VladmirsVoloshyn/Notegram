package com.uladzimirv.notegram.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.uladzimirv.notegram.ui.layout.main.com.LabelColorPref
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.buttonPrimary

@Immutable
class LabelColorSchema private constructor(
    val background: Color,
    val accent: Color,
    val textColor: Color = textPrimaryWhite,
    val pref: LabelColorPref
) {


    companion object Factory {


        val redSchema = LabelColorSchema(
            background = redL,
            accent = redAccent,
            pref = LabelColorPref.RED
        )
        val greenSchema = LabelColorSchema(
            background = greenL,
            accent = greenAccent,
            pref = LabelColorPref.GREEN
        )
        val brownSchema = LabelColorSchema(
            background = brownL,
            accent = brownAccent,
            pref = LabelColorPref.BROWN
        )
        val yellowSchema = LabelColorSchema(
            background = yellowL,
            accent = yellowAccent,
            pref = LabelColorPref.YELLOW
        )
        val blueSchema = LabelColorSchema(
            background = blueL,
            accent = blueAccent,
            pref = LabelColorPref.BLUE
        )
        val pinkSchema = LabelColorSchema(
            background = pinkL,
            accent = pinkAccent,
            pref = LabelColorPref.PINK
        )
        val orangeSchema = LabelColorSchema(
            background = orangeL,
            accent = orangeAccent,
            pref = LabelColorPref.ORANGE
        )
        val purpleSchema = LabelColorSchema(
            background = purpleL,
            accent = purpleAccent,
            pref = LabelColorPref.PURPLE
        )
        val graySchema = LabelColorSchema(
            background = grayL,
            accent = grayAccent,
            pref = LabelColorPref.GRAY
        )

        val schemasList = listOf(
            redSchema,
            greenSchema,
            blueSchema,
            brownSchema,
            yellowSchema,
            pinkSchema,
            orangeSchema,
            graySchema,
            purpleSchema,
        )

        @Composable
        fun common(): LabelColorSchema {
            val defBack = buttonPrimary
            val defText = backgroundPrimary
            return LabelColorSchema(
                background = defBack,
                accent = defText,
                textColor = backgroundPrimary,
                pref = LabelColorPref.COMMON
            )
        }

        @Composable
        fun fromPref(pref: LabelColorPref?): LabelColorSchema {

            val common = common()
            return remember(pref) {
                when (pref) {
                    LabelColorPref.COMMON -> common
                    LabelColorPref.RED -> redSchema
                    LabelColorPref.GREEN -> greenSchema
                    LabelColorPref.BROWN -> brownSchema
                    LabelColorPref.YELLOW -> yellowSchema
                    LabelColorPref.BLUE -> blueSchema
                    LabelColorPref.PINK -> pinkSchema
                    LabelColorPref.ORANGE -> orangeSchema
                    LabelColorPref.PURPLE -> purpleSchema
                    LabelColorPref.GRAY -> graySchema
                    else -> common
                }
            }
        }
    }

}