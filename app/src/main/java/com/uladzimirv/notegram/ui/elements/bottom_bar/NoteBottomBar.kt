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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.theme.backgroundContainerDarker
import com.uladzimirv.notegram.ui.theme.backgroundPrimary
import com.uladzimirv.notegram.ui.theme.borderPrimary
import com.uladzimirv.notegram.ui.theme.buttonPrimary
import com.uladzimirv.notegram.ui.theme.cyan
import com.uladzimirv.notegram.ui.theme.glow
import com.uladzimirv.notegram.ui.theme.orange
import com.uladzimirv.notegram.ui.theme.pink
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple

@Composable
fun NoteBottomBar(
    pin: () -> Unit,
    pinned: Boolean,
    selected: ColorPref,
    palette: (Boolean) -> Unit,
    colorMenuOpened: Boolean,
    changeColor: (ColorPref) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 16.dp, bottom = 24.dp)
    ) {
        val pinResId = remember(pinned) {
            if (pinned) R.drawable.ic_pin_filled
            else R.drawable.ic_pin_unfilled
        }

        AnimatedVisibility(
            visible = colorMenuOpened,
            enter = fadeIn() + expandIn(
                animationSpec = tween(
                    durationMillis = 30
                )
            ),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .background(backgroundContainerDarker, CircleShape)
                    .padding(6.dp)

            ) {
                ColorContainer(
                    selected = selected == ColorPref.CYAN,
                    background = cyan
                ) {
                    changeColor(ColorPref.CYAN)
                }
                Gap(6)
                ColorContainer(
                    selected = selected == ColorPref.PINK,
                    background = pink
                ) {
                    changeColor(ColorPref.PINK)
                }
                Gap(6)
                ColorContainer(
                    selected = selected == ColorPref.GLOW,
                    background = glow
                ) {
                    changeColor(ColorPref.GLOW)
                }
                Gap(6)
                ColorContainer(
                    selected = selected == ColorPref.ORANGE,
                    background = orange
                ) {
                    changeColor(ColorPref.ORANGE)
                }
                Gap(6)
                ColorContainer(
                    selected = selected == ColorPref.COMMON,
                    background = backgroundPrimary
                ) {
                    changeColor(ColorPref.COMMON)
                }
            }

        }
        Gap(24)
        val resource = remember(colorMenuOpened) {
            if (colorMenuOpened) R.drawable.ic_cross
            else R.drawable.ic_brush
        }
        AnimatedVisibility(
            visible = !colorMenuOpened,
            enter = fadeIn() + expandIn(
                animationSpec = tween(
                    durationMillis = 100
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 100
                )
            ) + shrinkOut(
                animationSpec = tween(
                    durationMillis = 100
                )
            )
        ) {
            Icon(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = pin
                    )
                    .size(24.dp),
                painter = painterResource(id = pinResId),
                contentDescription = null,
                tint = buttonPrimary,
            )
        }
        Gap(16)
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
            painter = painterResource(resource),
            contentDescription = null,
            tint = buttonPrimary,
        )
    }
}

@Composable
fun ColorContainer(
    selected: Boolean,
    background: Color,
    onClick: () -> Unit
) {
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
            .background(background, CircleShape)
            .size(40.dp)
            .clickableNoRipple(onClick = onClick)
    )
}