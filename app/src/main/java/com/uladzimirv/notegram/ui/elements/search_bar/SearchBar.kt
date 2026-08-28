package com.uladzimirv.notegram.ui.elements.search_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.Anchor
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.logo.AppLogo
import com.uladzimirv.notegram.ui.theme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.borderPrimary
import com.uladzimirv.notegram.ui.theme.borderSecondary
import com.uladzimirv.notegram.ui.theme.buttonPrimary
import com.uladzimirv.notegram.ui.theme.textPrimary
import com.uladzimirv.notegram.ui.theme.textSecondary
import com.uladzimirv.notegram.util.STRING_EMPTY
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TopSearchBar(
    enabled: Boolean,
    query: String,
    menu: () -> Unit,
    onTextChanged: (String) -> Unit,
    isBarEnabled: (Boolean) -> Unit
) {

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val kb = LocalSoftwareKeyboardController.current

    LaunchedEffect(enabled) {
        if (enabled) {
            delay(100.milliseconds)
            focusRequester.requestFocus()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = !enabled
        ) {
            Row() {
                Gap(8)
                AppLogo(
                    named = true,
                    showIcon = false
                )
                Gap(6)
            }
        }
        AnimatedVisibility(
            visible = enabled
        ) {
            Row {
                Icon(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                focusManager.clearFocus(force = true)
                                isBarEnabled(false)
                                kb?.hide()
                            }
                        )
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = buttonPrimary,
                )
                Gap(12)
            }
        }

        val shape = remember {
            RoundedCornerShape(24.dp)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .clip(shape)
                .background(backgroundSecondary)
                .border(
                    width = 1.dp,
                    color = borderSecondary,
                    shape = shape
                )
                .clickableNoRipple {
                    isBarEnabled(true)
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Gap(12)
                BasicTextField(
                    enabled = enabled,
                    value = query,
                    onValueChange = { tfv ->
                        onTextChanged(tfv)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    cursorBrush = SolidColor(textPrimary),
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.s_search),
                                    color = textSecondary
                                )
                                Anchor()
                                AnimatedVisibility(
                                    visible = !enabled
                                ) {
                                    Row {
                                        Gap(12)
                                        Icon(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clickableNoRipple(
                                                    onClick = {
                                                    }
                                                ),
                                            painter = painterResource(id = R.drawable.ic_sort),
                                            contentDescription = null,
                                            tint = buttonPrimary,
                                        )
                                    }
                                }
                            }

                        }
                        innerTextField()
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                        }
                    )
                )

                AnimatedVisibility(
                    visible = query.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_cross),
                            contentDescription = null,
                            tint = buttonPrimary,
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        onTextChanged(STRING_EMPTY)
                                    }
                                )
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = !enabled
        ) {
            Row {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickableNoRipple(
                            onClick = menu
                        ),
                    painter = painterResource(id = R.drawable.ic_menu_dots),
                    contentDescription = null,
                    tint = buttonPrimary,
                )
            }
        }
    }
}