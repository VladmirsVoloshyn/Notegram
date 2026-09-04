package com.uladzimirv.notegram.ui.elements.bottom_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.theme.AppTheme.borderPrimary
import com.uladzimirv.notegram.ui.theme.NoteColorSchema
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple

@Composable
fun NoteBottomBar(
    pin: () -> Unit,
    showLabels: () -> Unit,
    pinned: Boolean,
    colorSchema: NoteColorSchema,
    palette: (Boolean) -> Unit,
    colorMenuOpened: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .clickableNoRipple() {}
            .background(colorSchema.background.copy(alpha = .8f))
            .padding(end = 16.dp, bottom = 24.dp, top = 12.dp)
    ) {
        val pinResId = remember(pinned) {
            if (pinned) R.drawable.ic_pin_filled
            else R.drawable.ic_pin_unfilled
        }
        Gap(24)
        Box(
            modifier = Modifier
                .size(26.dp)
                .background(colorSchema.background, CircleShape)
                .padding(3.dp)
        ) {
            Icon(
                modifier = Modifier
                    .clickableNoRipple(onClick = pin)
                    .size(24.dp),
                painter = painterResource(id = pinResId),
                contentDescription = null,
                tint = colorSchema.accent,
            )
        }

        Gap(16)
        Box(
            modifier = Modifier
                .size(26.dp)
                .background(colorSchema.background, CircleShape)
                .padding(3.dp)
        ) {
            Icon(
                modifier = Modifier
                    .clickableNoRipple(onClick = showLabels)
                    .size(24.dp),
                painter = painterResource(id = R.drawable.ic_label_thin),
                contentDescription = null,
                tint = colorSchema.accent,
            )
        }
        Gap(16)
        Box(
            modifier = Modifier
                .size(26.dp)
                .background(colorSchema.background, CircleShape)
                .padding(3.dp)
        ) {
            Icon(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            palette(!colorMenuOpened)
                        }
                    )
                    .size(24.dp),
                painter = painterResource(R.drawable.ic_brush),
                contentDescription = null,
                tint = colorSchema.accent,
            )
        }
    }
}

@Composable
fun ColorContainer(
    selected: Boolean,
    pref: ColorPref,
    onClick: () -> Unit
) {
    val schema = NoteColorSchema.fromPref(pref)
    Box(
        modifier = Modifier
            .let {
                if (selected) it.border(
                    shape = CircleShape,
                    width = 1.dp,
                    color = borderPrimary
                )
                else it
            }
            .background(schema.background, CircleShape)
            .size(64.dp)
            .clickableNoRipple(onClick = onClick)
    ) {
        Text(
            text = "Aa",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = schema.accent,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}