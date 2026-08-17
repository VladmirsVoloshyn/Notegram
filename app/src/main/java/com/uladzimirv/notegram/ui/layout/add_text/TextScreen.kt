package com.uladzimirv.notegram.ui.layout.add_text

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.app_flow.main.contract.MainIntent
import com.uladzimirv.notegram.app_flow.main.contract.MainViewState
import com.uladzimirv.notegram.ui.elements.BaseBottomSheet
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.bottom_bar.NoteBottomBar
import com.uladzimirv.notegram.ui.elements.top_bar.NoteTopBar
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.theme.backgroundPrimary
import com.uladzimirv.notegram.ui.theme.cyan
import com.uladzimirv.notegram.ui.theme.glow
import com.uladzimirv.notegram.ui.theme.orange
import com.uladzimirv.notegram.ui.theme.pink
import com.uladzimirv.notegram.ui.theme.textPrimary
import com.uladzimirv.notegram.ui.theme.textSecondary
import com.uladzimirv.notegram.util.STRING_EMPTY

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextScreen(
    state: MainViewState.TextNoteSubState,
    intent: (MainIntent) -> Unit
) {
    val background =
        when (state.note?.colorPref) {
            ColorPref.COMMON -> backgroundPrimary
            ColorPref.ORANGE -> orange
            ColorPref.CYAN -> cyan
            ColorPref.GLOW -> glow
            ColorPref.PINK -> pink
            null -> backgroundPrimary
        }

    BaseBottomSheet(
        showBottomSheet = state.show,
        backgroundColor = background,
        onDismissRequest = { intent(MainIntent.MainScreenIntent.CloseSheets) },
        sheetGesturesEnabled = false
    ) {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)

            ) {
                val scroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .background(background)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .scrollable(
                            state = scroll,
                            enabled = true,
                            orientation = Orientation.Vertical
                        )
                        .fillMaxSize()
                ) {
                    NoteTopBar(
                        back = { intent(MainIntent.MainScreenIntent.CloseSheets) },
                        delete = {
                            intent(
                                MainIntent.MainScreenIntent.Delete(
                                    state.note?.id ?: STRING_EMPTY
                                )
                            )
                            intent(MainIntent.MainScreenIntent.CloseSheets)
                        }
                    )
                    Gap(48)
                    TitleEdit(
                        title = state.note?.title.orEmpty(),
                    ) {
                        intent(MainIntent.EditNote.Title(it))
                    }
                    Gap(24)
                    TextEdit(
                        text = state.note?.text.orEmpty(),
                        modifier = Modifier.weight(1f)
                    ) {
                        intent(MainIntent.EditNote.Text(it))
                    }
                    NoteBottomBar(
                        pin = {
                            intent(
                                MainIntent.MainScreenIntent.PinOrUnpin(
                                    state.note?.id ?: STRING_EMPTY
                                )
                            )
                        },
                        palette = {
                            intent(MainIntent.MainScreenIntent.OpenColorContainer(it))
                        },
                        pinned = state.note?.pinned == true,
                        colorMenuOpened = state.colorMenuOpened,
                        changeColor = { intent(MainIntent.EditNote.ChangeColor(it)) },
                        selected = state.note?.colorPref ?: ColorPref.COMMON
                    )
                }
            }
        }
    }
}

@Composable
fun TitleEdit(
    title: String,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    BasicTextField(
        value = title,
        onValueChange = { tfv ->
            onChange(tfv)
        },
        textStyle = TextStyle(
            fontSize = 32.sp,
            color = textPrimary
        ),
        modifier = modifier
            .focusRequester(focusRequester),
        cursorBrush = SolidColor(textPrimary),
        decorationBox = { innerTextField ->
            if (title.isEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.s_title),
                        color = textSecondary,
                        fontSize = 32.sp
                    )
                }

            }
            innerTextField()
        },
        singleLine = false,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Sentences
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )
}

@Composable
fun TextEdit(
    text: String,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    BasicTextField(
        value = text,
        onValueChange = { tfv ->
            onChange(tfv)
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = textPrimary
        ),
        modifier = modifier
            .focusRequester(focusRequester),
        cursorBrush = SolidColor(textPrimary),
        decorationBox = { innerTextField ->
            if (text.isEmpty()) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = stringResource(R.string.s_text),
                        color = textSecondary,
                        fontSize = 16.sp
                    )
                }

            }
            innerTextField()
        },
        singleLine = false,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Sentences
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )
}