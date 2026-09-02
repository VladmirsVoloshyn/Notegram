package com.uladzimirv.notegram.ui.layout.pin_code

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.theme.AppTheme
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.buttonPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.textPrimary
import com.uladzimirv.notegram.util.vibration.tickVibrate
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private val buttonFontSize = 30.sp

@Composable
fun PinCodeButton(
    sing: Char,
    onClick: (String) -> Unit
) {
    val scale = remember {
        mutableFloatStateOf(1f)
    }
    val context = LocalContext.current
    Text(
        text = sing.toString(),
        fontSize = buttonFontSize,
        fontWeight = FontWeight.Bold,
        color = textPrimary,
        modifier = Modifier
            .scale(scale.floatValue)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        context.tickVibrate()
                        scale.floatValue = 1.5f
                        onClick(sing.toString())
                        val success = tryAwaitRelease()
                        if (success) {
                            delay(100.milliseconds)
                            scale.floatValue = 1f
                        } else {
                            delay(100.milliseconds)
                            scale.floatValue = 1f
                        }
                    }
                )

            }
    )
}

@Composable
fun PinCodeIconButton(
    iconResId: Int,
    active: Boolean = true,
    onClick: () -> Unit
) {
    val scale = remember {
        mutableFloatStateOf(1f)
    }
    val context = LocalContext.current
    Icon(
        painter = painterResource(iconResId),
        tint = if (active) buttonPrimary else Color.Transparent,
        contentDescription = null,
        modifier = Modifier
            .size(30.dp)
            .scale(scale.floatValue)
            .let {
                if (active) {
                    it.pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                context.tickVibrate()
                                scale.floatValue = 1.5f
                                onClick()
                                val success = tryAwaitRelease()
                                if (success) {
                                    delay(100.milliseconds)
                                    scale.floatValue = 1f
                                } else {
                                    delay(100.milliseconds)
                                    scale.floatValue = 1f
                                }
                            }
                        )
                    }
                } else it
            }

    )
}

@Composable
fun PinKeyboard(
    showBiometric: Boolean,
    nextChar: (String) -> Unit,
    backspace: () -> Unit
) {
    Column(
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            PinCodeButton(
                sing = '1',
                onClick = nextChar
            )
            PinCodeButton(
                sing = '2',
                onClick = nextChar
            )
            PinCodeButton(
                sing = '3',
                onClick = nextChar
            )
        }
        Gap(24)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            PinCodeButton(
                sing = '4',
                onClick = nextChar
            )
            PinCodeButton(
                sing = '5',
                onClick = nextChar
            )
            PinCodeButton(
                sing = '6',
                onClick = nextChar
            )
        }
        Gap(24)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            PinCodeButton(
                sing = '7',
                onClick = nextChar
            )
            PinCodeButton(
                sing = '8',
                onClick = nextChar
            )
            PinCodeButton(
                sing = '9',
                onClick = nextChar
            )
        }
        Gap(24)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceAround
        ) {
            PinCodeIconButton(
                iconResId = R.drawable.ic_backspace,
                onClick = backspace
            )
            PinCodeButton(
                sing = '0',
                onClick = nextChar
            )
            PinCodeIconButton(
                active = showBiometric,
                iconResId = R.drawable.ic_fingerprint,
                onClick = backspace
            )
        }
    }
}