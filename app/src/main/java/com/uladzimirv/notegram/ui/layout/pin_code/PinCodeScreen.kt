package com.uladzimirv.notegram.ui.layout.pin_code

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.app_flow.main.contract.MainMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.MainViewState
import com.uladzimirv.notegram.ui.elements.BaseBottomSheet
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.AppTheme.textPrimary
import com.uladzimirv.notegram.ui.theme.completeGreen
import com.uladzimirv.notegram.ui.theme.errorRed
import com.uladzimirv.notegram.util.STRING_EMPTY
import com.uladzimirv.notegram.util.VEVO
import com.uladzimirv.notegram.util.vibration.clickVibrate
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

enum class PinConfirmation {
    CONFIRM,
    FAILED,
    AWAIT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinCodeScreen(
    show: Boolean,
    state: MainViewState.PinCodeScreenState,
    saveNew: (String) -> Unit = {},
    send: (String) -> Unit,
    drop: () -> Unit,
    onUnlock: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val pinCode = remember {
        mutableStateOf(STRING_EMPTY)
    }
    val confirmPinCode = remember {
        mutableStateOf(STRING_EMPTY)
    }

    val confirmation = remember {
        mutableStateOf(PinConfirmation.AWAIT)
    }

    LaunchedEffect(pinCode.value, confirmPinCode.value, state.attempt) {
        when (state.purpose) {
            is MainViewState.PinCodeScreenState.PinCodePurpose.CreateNew -> {
                if (pinCode.value.length == 4) {
                    if (confirmPinCode.value.isEmpty()) {
                        delay(300.milliseconds)
                        confirmPinCode.value = pinCode.value
                        pinCode.value = STRING_EMPTY
                    } else {
                        if (confirmPinCode.value == pinCode.value) {
                            confirmation.value = PinConfirmation.CONFIRM
                            saveNew(pinCode.value)
                            delay(1000.milliseconds)
                            sheetState.hide()
                            delay(50.milliseconds)
                            onDismissRequest()
                        } else {
                            confirmation.value = PinConfirmation.FAILED
                            delay(1000.milliseconds)
                            confirmation.value = PinConfirmation.AWAIT
                            pinCode.value = STRING_EMPTY
                            confirmPinCode.value = STRING_EMPTY
                        }
                    }
                }
            }

            is MainViewState.PinCodeScreenState.PinCodePurpose.DeletePinCode,
            is MainViewState.PinCodeScreenState.PinCodePurpose.Access,
            is MainViewState.PinCodeScreenState.PinCodePurpose.Unlock -> {
                if (pinCode.value.length == 4) {
                    send(pinCode.value)
                }

                when (state.attempt) {
                    MainViewState.PinCodeScreenState.Attempt.WRONG -> {
                        context.clickVibrate(10)
                        delay(1000.milliseconds)
                        pinCode.value = STRING_EMPTY
                        drop()
                    }

                    MainViewState.PinCodeScreenState.Attempt.ATTEMPT -> {}
                    MainViewState.PinCodeScreenState.Attempt.SUCCESS -> {
                        onUnlock()
                        delay(200.milliseconds)
                        sheetState.hide()
                        delay(50.milliseconds)
                        pinCode.value = STRING_EMPTY
                        onDismissRequest()
                    }
                }
            }

            else -> {}
        }
    }

    val title = when (state.purpose) {
        is MainViewState.PinCodeScreenState.PinCodePurpose.CreateNew -> if (confirmPinCode.value.isNotEmpty()) {
            R.string.s_pin_code_repeat
        } else {
            R.string.s_pin_code_create
        }

        is MainViewState.PinCodeScreenState.PinCodePurpose.Access,
        is MainViewState.PinCodeScreenState.PinCodePurpose.DeletePinCode,
        is MainViewState.PinCodeScreenState.PinCodePurpose.Unlock -> {
            when (state.attempt) {
                MainViewState.PinCodeScreenState.Attempt.ATTEMPT -> R.string.s_pin_code_enter
                MainViewState.PinCodeScreenState.Attempt.WRONG -> R.string.s_pin_code_enter_error
                MainViewState.PinCodeScreenState.Attempt.SUCCESS -> R.string.s_pin_code_enter
            }
        }

        else -> R.string.s_pin_code_enter
    }

    BaseBottomSheet(
        backgroundColor = backgroundSecondary,
        sheetState = sheetState,
        showBottomSheet = show,
        onDismissRequest = {
            pinCode.value = STRING_EMPTY
            confirmPinCode.value = STRING_EMPTY
            confirmation.value = PinConfirmation.AWAIT
            onDismissRequest()
        },
        sheetGesturesEnabled = true
    ) {
        Box {
            Column(
                modifier = Modifier
                    .background(backgroundSecondary)
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp)
            ) {
                AnimatedContent(
                    targetState = title,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(220, delayMillis = 120)))
                            .togetherWith(fadeOut(animationSpec = tween(120)))
                    }
                ) {
                    Text(
                        text = stringResource(it),
                        fontSize = 24.sp,
                        color = textPrimary
                    )
                }
                BubblesBlock(pinCode.value.length)
                PinKeyboard(
                    showBiometric = false,
                    nextChar = {
                        if (pinCode.value.length < 4) {
                            pinCode.value += it
                        }
                    },
                    backspace = {
                        if (pinCode.value.isNotEmpty()) {
                            pinCode.value = pinCode.value.substring(0, pinCode.value.length - 1)
                        }
                    }
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = confirmation.value != PinConfirmation.AWAIT,
                modifier = Modifier.matchParentSize(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Box(
                    modifier = Modifier
                        .background(backgroundSecondary)
                        .padding(horizontal = 24.dp)
                ) {
                    when (confirmation.value) {
                        PinConfirmation.CONFIRM -> {
                            PinCreationAlert(
                                iconResId = R.drawable.ic_complete,
                                textResId = R.string.s_pin_code_create_complete,
                                iconColor = completeGreen
                            )
                        }

                        else -> {
                            PinCreationAlert(
                                iconResId = R.drawable.ic_error,
                                textResId = R.string.s_pin_code_create_error,
                                iconColor = errorRed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BoxScope.PinCreationAlert(
    iconResId: Int,
    textResId: Int,
    iconColor: Color
) {
    Column(
        modifier = Modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = iconColor
        )
        Gap(18)
        Text(
            text = stringResource(textResId),
            modifier = Modifier,
            color = textPrimary,
            textAlign = TextAlign.Center,
            fontSize = 28.sp
        )
    }
}

@Composable
fun BubblesBlock(
    bubblesCount: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(bubblesCount) {
                Box(
                    modifier = Modifier
                        .size(
                            18.dp
                        )
                        .background(color = textPrimary, CircleShape),

                    )
            }
        }
    }

}