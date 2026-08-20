package com.uladzimirv.notegram.ui.layout.note_ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import com.uladzimirv.notegram.data.database.entity.TodoListItem
import com.uladzimirv.notegram.domain.model.note.text.TextNote
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote
import com.uladzimirv.notegram.ui.elements.Anchor
import com.uladzimirv.notegram.ui.elements.BaseBottomSheet
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.bottom_bar.NoteBottomBar
import com.uladzimirv.notegram.ui.elements.layer.ActionColumn
import com.uladzimirv.notegram.ui.elements.top_bar.NoteTopBar
import com.uladzimirv.notegram.ui.layout.main.DeleteConfirmationDialog
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.MenuDestination
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.ui.theme.backgroundPrimary
import com.uladzimirv.notegram.ui.theme.buttonPrimary
import com.uladzimirv.notegram.ui.theme.buttonSecondary
import com.uladzimirv.notegram.ui.theme.cyan
import com.uladzimirv.notegram.ui.theme.glow
import com.uladzimirv.notegram.ui.theme.orange
import com.uladzimirv.notegram.ui.theme.pink
import com.uladzimirv.notegram.ui.theme.textPrimary
import com.uladzimirv.notegram.ui.theme.textSecondary
import com.uladzimirv.notegram.util.STRING_EMPTY
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple
import com.uladzimirv.notegram.util.vibration.tickVibrate
import kotlinx.collections.immutable.ImmutableList
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    state: MainViewState.NoteSubState,
    deleteState: MainViewState.DeleteState,
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
        onDismissRequest = {
            intent(MainIntent.MainScreenIntent.CloseSheets)
            intent(MainIntent.EditNote.OpenNoteTopMenu(false))
        },
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
                        .fillMaxSize().let {
                            if (state.topMenuOpened || deleteState.note != null) it.blur(
                                radius = 6.dp,
                                edgeTreatment = BlurredEdgeTreatment.Unbounded
                            ) else it
                        }
                ) {
                    NoteTopBar(
                        back = { intent(MainIntent.MainScreenIntent.CloseSheets) },
                        onClick = {
                            intent(MainIntent.EditNote.OpenNoteTopMenu(true))
                        }
//                        delete = {
//                            intent(
//                                MainIntent.MainScreenIntent.Delete(
//                                    state.note?.id ?: STRING_EMPTY
//                                )
//                            )
//                            intent(MainIntent.MainScreenIntent.CloseSheets)
//                        }
                    )
                    Gap(48)
                    TitleEdit(
                        title = state.note?.title.orEmpty(),
                    ) {
                        intent(MainIntent.EditNote.Title(it))
                    }
                    Gap(24)
                    when (state.note) {
                        is TextNote -> {
                            TextEdit(
                                text = state.note.text,
                                modifier = Modifier.weight(1f)
                            ) {
                                intent(MainIntent.EditNote.Text(it))
                            }
                        }

                        is TodoListNote -> {
                            TodoEdit(
                                modifier = Modifier.weight(1f),
                                list = state.note.todoList,
                                delete = { intent(MainIntent.EditNote.DeleteTodoItem(it)) },
                                checkClick = { intent(MainIntent.EditNote.CheckTodoItem(it)) },
                                selectedList = state.note.selectedTodoList,
                                reorder = { id, from, to ->
                                    intent(
                                        MainIntent.EditNote.Reorder(
                                            id = id,
                                            from = from,
                                            to = to
                                        )
                                    )
                                }
                            ) { text, id ->
                                intent(MainIntent.EditNote.EditTodo(text, id))
                            }
                        }
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

                NoteTopMenu(
                    isLayerVisible = state.topMenuOpened,
                    pinned = state.note?.pinned == true,
                    shareText = state.note?.toUIModel()?.shareText().orEmpty(),
                    delete = {
                        state.note?.id?.let { intent(MainIntent.MainScreenIntent.Delete(it)) }
                        intent(MainIntent.EditNote.OpenNoteTopMenu(false))
                    },
                    pin = {
                        intent(
                            MainIntent.MainScreenIntent.PinOrUnpin(
                                state.note?.id ?: STRING_EMPTY
                            )
                        )
                        intent(MainIntent.EditNote.OpenNoteTopMenu(false))
                    },
                    share = {
                        intent(MainIntent.EditNote.OpenNoteTopMenu(false))
                    },
                    closeMenu = {
                        intent(MainIntent.EditNote.OpenNoteTopMenu(false))
                    }
                )

                DeleteConfirmationDialog(
                    show = deleteState.note != null,
                    noteTitle = deleteState.note?.title.orEmpty(),
                    type = deleteState.note?.getType() ?: NoteType.TEXT,
                    cancel = { intent(MainIntent.MainScreenIntent.Delete(null)) },
                    confirm = { intent(MainIntent.MainScreenIntent.ConfirmDelete) }
                )
            }
        }
    }
}

@Composable
fun NoteTopMenu(
    isLayerVisible: Boolean,
    shareText: String,
    pinned: Boolean,
    delete: () -> Unit,
    pin: () -> Unit,
    share: () -> Unit,
    closeMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 24.dp, top = 24.dp)
            .let {
                if (isLayerVisible) it.clickableNoRipple(onClick = closeMenu) else it
            }
    ) {
        ActionColumn(
            modifier = Modifier.align(Alignment.TopEnd),
            isLayerVisible = isLayerVisible,
            pinned = pinned,
            menuDestination = MenuDestination.LEFT,
            delete = delete,
            onShareClicked = share,
            shareText = shareText,
            pin = pin
        )
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

@Composable
fun TodoEdit(
    modifier: Modifier,
    list: ImmutableList<TodoListItem>,
    selectedList: ImmutableList<TodoListItem>,
    delete: (id: String) -> Unit,
    checkClick: (id: String) -> Unit,
    reorder: (id: String, from: Int, to: Int) -> Unit,
    onChange: (text: String, id: String?) -> Unit,
) {
    val reorderState = rememberReorderableLazyListState(
        onMove = { from, to ->
            (from.key as? String)?.let { reorder(it, from.index, to.index) }
        }
    )

    val content = LocalContext.current

    LazyColumn(
        state = reorderState.listState,
        modifier = modifier
            .reorderable(reorderState)
            .detectReorderAfterLongPress(reorderState),
    ) {
        items(
            items = list,
            key = { it.id }
        ) { item ->
            ReorderableItem(
                state = reorderState,
                key = item.id
            ) { isDragging ->
                if (isDragging) {
                    content.tickVibrate()
                }
                val sizeScale = animateDpAsState(if (isDragging) 1.1.dp else 1.dp)
                TodoListItemUI(
                    text = item.text,
                    id = item.id,
                    selected = item.selected,
                    modifier = Modifier,
                    delete = delete,
                    checkClick = checkClick,
                    onChange = onChange,
                    scale = sizeScale.value.value
                )
            }
        }
        item {
            Gap(16)
            AddItem {
                onChange(STRING_EMPTY, null)
            }
            Gap(16)
        }
        items(
            items = selectedList,
            key = { it.id }
        ) { item ->
            SelectedTodoListItemUI(
                text = item.text,
                id = item.id,
                selected = item.selected,
                modifier = Modifier,
                delete = delete,
                checkClick = checkClick
            )
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun TodoListItemUI(
    modifier: Modifier,
    text: String,
    id: String,
    scale: Float,
    selected: Boolean,
    delete: (id: String) -> Unit,
    checkClick: (id: String) -> Unit,
    onChange: (text: String, id: String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val checkBox = remember(selected) {
        if (selected) R.drawable.ic_checkbox_selected
        else R.drawable.ic_checkbox_unchecked
    }

    val width = LocalConfiguration.current.screenWidthDp
    val fieldWidth = remember { width - 24 - 16 - 24 - 8 - 8 - 20 - 32 }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(vertical = 16.dp)
            .scale(scale)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_drag_handle),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp),
            tint = buttonPrimary
        )
        Gap(16)
        Icon(
            painter = painterResource(checkBox),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clickableNoRipple {
                    checkClick(id)
                },
            tint = buttonPrimary
        )
        Gap(8)
        BasicTextField(
            value = text,
            onValueChange = { tfv ->
                onChange(tfv, id)
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = textPrimary
            ),
            modifier = Modifier
                .width(fieldWidth.dp)
                .focusRequester(focusRequester),
            cursorBrush = SolidColor(textPrimary),
            decorationBox = { innerTextField ->
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
        Anchor()
        Gap(8)
        Icon(
            painter = painterResource(R.drawable.ic_cross),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .clickableNoRipple {
                    delete(id)
                },
            tint = buttonPrimary
        )
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SelectedTodoListItemUI(
    modifier: Modifier,
    text: String,
    id: String,
    selected: Boolean,
    delete: (id: String) -> Unit,
    checkClick: (id: String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val checkBox = remember(selected) {
        if (selected) R.drawable.ic_checkbox_selected
        else R.drawable.ic_checkbox_unchecked
    }

    val width = LocalConfiguration.current.screenWidthDp
    val fieldWidth = remember { width - 24 - 16 - 24 - 8 - 8 - 20 - 32 }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(vertical = 16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_drag_handle),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp),
            tint = Color.Transparent
        )
        Gap(16)
        Icon(
            painter = painterResource(checkBox),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clickableNoRipple {
                    checkClick(id)
                },
            tint = buttonSecondary
        )
        Gap(8)
        BasicTextField(
            enabled = false,
            value = text,
            onValueChange = { tfv ->
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = textSecondary
            ),
            modifier = Modifier
                .width(fieldWidth.dp)
                .focusRequester(focusRequester),
            cursorBrush = SolidColor(textSecondary),
            decorationBox = { innerTextField ->
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
        Anchor()
        Gap(8)
        Icon(
            painter = painterResource(R.drawable.ic_cross),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .clickableNoRipple {
                    delete(id)
                },
            tint = buttonPrimary
        )
    }
}

@Composable
fun AddItem(
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickableNoRipple(onClick = onClick)
    ) {
        Gap(40)
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = buttonPrimary
        )
        Gap(12)
        Text(
            text = stringResource(R.string.s_add_item),
            fontSize = 16.sp
        )
    }
}
