package com.uladzimirv.notegram.ui.layout.note_ui

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationIntent
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.data.database.entity.TodoListItem
import com.uladzimirv.notegram.domain.model.label.LabelId
import com.uladzimirv.notegram.domain.model.note.text.TextNote
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote
import com.uladzimirv.notegram.ui.elements.Anchor
import com.uladzimirv.notegram.ui.elements.AppBottomSheet
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.bottom_bar.NoteBottomBar
import com.uladzimirv.notegram.ui.elements.layer.MainMenuItemActionColumn
import com.uladzimirv.notegram.ui.elements.top_bar.NoteTopBar
import com.uladzimirv.notegram.ui.layout.main.DeleteConfirmationDialog
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.MenuDestination
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.ui.layout.note_ui.add_label.AddLabelBottomSheet
import com.uladzimirv.notegram.ui.layout.note_ui.label.LabelInfoLayer
import com.uladzimirv.notegram.ui.model.LabelUI
import com.uladzimirv.notegram.ui.theme.LabelColorSchema
import com.uladzimirv.notegram.ui.theme.NoteColorSchema
import com.uladzimirv.notegram.util.STRING_EMPTY
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple
import com.uladzimirv.notegram.util.vibration.tickVibrate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    state: ApplicationViewState.NoteSubState,
    deleteState: ApplicationViewState.DeleteState,
    intent: (ApplicationIntent) -> Unit
) {
    val colorSchema = NoteColorSchema.fromPref(state.note?.colorPref)
    val sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val context = LocalContext.current
    AppBottomSheet(
        sheetState = sheetState,
        showBottomSheet = state.show,
        backgroundColor = colorSchema.background,
        onDismissRequest = {
            intent(ApplicationIntent.MainScreenIntent.CloseSheets)
            intent(ApplicationIntent.EditNoteIntent.OpenNoteTopMenu(false))
            intent(ApplicationIntent.EditNoteIntent.SelectLabel(STRING_EMPTY))
        },
        sheetGesturesEnabled = false
    ) {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
            ) {
                val scroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .background(colorSchema.background)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxSize()
                        .let {
                            if (state.topMenuOpened
                                || deleteState.note != null
                                || state.selectedLabel != null
                            ) it.blur(
                                radius = 6.dp,
                                edgeTreatment = BlurredEdgeTreatment.Unbounded
                            ) else it
                        }
                        .verticalScroll(
                            state = scroll,
                            enabled = true
                        )
                ) {
                    val scope = rememberCoroutineScope()
                    NoteTopBar(
                        back = {
                            scope.launch {
                                sheetState.hide()
                                delay(100.milliseconds)
                                intent(ApplicationIntent.MainScreenIntent.CloseSheets)
                                intent(ApplicationIntent.EditNoteIntent.SelectLabel(STRING_EMPTY))
                            }
                        },
                        colorSchema = colorSchema,
                        onClick = {
                            intent(ApplicationIntent.EditNoteIntent.OpenNoteTopMenu(true))
                        }
                    )
                    Gap(10)
                    Box(
                        modifier = Modifier
                            .wrapContentHeight()
                            .animateContentSize()
                            .fillMaxWidth()
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            state = rememberLazyListState()
                        ) {
                            items(
                                items = state.note?.labels?.toList().orEmpty()
                            ) {
                                NoteLabel(
                                    modifier = Modifier,
                                    labelUI = it.toUIModel()
                                ) {
                                    intent(ApplicationIntent.EditNoteIntent.SelectLabel(it))
                                    context.tickVibrate()
                                }
                            }
                        }
                    }
                    Gap(10)
                    TitleEdit(
                        title = state.note?.title.orEmpty(),
                        colorSchema = colorSchema
                    ) {
                        intent(ApplicationIntent.EditNoteIntent.Title(it))
                    }
                    Gap(24)
                    when (state.note) {
                        is TextNote -> {
                            TextEdit(
                                text = state.note.text,
                                colorSchema = colorSchema
                            ) {
                                intent(ApplicationIntent.EditNoteIntent.Text(it))
                            }
                        }

                        is TodoListNote -> {
                            TodoEdit(
                                modifier = Modifier.weight(1f),
                                list = state.note.todoList,
                                delete = { intent(ApplicationIntent.EditNoteIntent.DeleteTodoItem(it)) },
                                checkClick = {
                                    intent(
                                        ApplicationIntent.EditNoteIntent.CheckTodoItem(
                                            it
                                        )
                                    )
                                },
                                selectedList = state.note.selectedTodoList,
                                colorSchema = colorSchema,
                                reorder = { id, from, to ->
                                    intent(
                                        ApplicationIntent.EditNoteIntent.Reorder(
                                            id = id,
                                            from = from,
                                            to = to
                                        )
                                    )
                                }
                            ) { text, id ->
                                intent(ApplicationIntent.EditNoteIntent.EditTodo(text, id))
                            }
                        }
                    }
                    Gap(100)
                }

                NoteBottomBar(
                    modifier = Modifier.align(Alignment.BottomCenter).let {
                        if (state.topMenuOpened
                            || deleteState.note != null
                            || state.selectedLabel != null
                        ) it.blur(
                            radius = 6.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        ) else it
                    },
                    pin = {
                        intent(
                            ApplicationIntent.MainScreenIntent.PinOrUnpin(
                                state.note?.id ?: STRING_EMPTY
                            )
                        )
                    },
                    palette = {
                        intent(ApplicationIntent.MainScreenIntent.OpenColorContainer(it))
                    },
                    showLabels = {
                        intent(ApplicationIntent.EditNoteIntent.ShowAddLabelMenu(true))
                    },
                    pinned = state.note?.pinned == true,
                    colorMenuOpened = state.colorMenuOpened,
                    changeColor = { intent(ApplicationIntent.EditNoteIntent.ChangeColor(it)) },
                    selected = state.note?.colorPref ?: ColorPref.COMMON,
                    colorSchema = colorSchema
                )

                NoteTopMenu(
                    isLayerVisible = state.topMenuOpened,
                    pinned = state.note?.pinned == true,
                    shareText = state.note?.toUIModel()?.summary().orEmpty(),
                    delete = {
                        state.note?.id?.let { intent(ApplicationIntent.MainScreenIntent.Delete(it)) }
                        intent(ApplicationIntent.EditNoteIntent.OpenNoteTopMenu(false))
                    },
                    pin = {
                        intent(
                            ApplicationIntent.MainScreenIntent.PinOrUnpin(
                                state.note?.id ?: STRING_EMPTY
                            )
                        )
                        intent(ApplicationIntent.EditNoteIntent.OpenNoteTopMenu(false))
                    },
                    share = {
                        intent(ApplicationIntent.EditNoteIntent.OpenNoteTopMenu(false))
                    },
                    closeMenu = {
                        intent(ApplicationIntent.EditNoteIntent.OpenNoteTopMenu(false))
                    },
                    locked = state.note?.locked == true,
                    lock = {},
                    archive = {
                        intent(
                            ApplicationIntent.MainScreenIntent.Archive(
                                state.note?.id ?: STRING_EMPTY
                            )
                        )
                        intent(ApplicationIntent.EditNoteIntent.OpenNoteTopMenu(false))
                        intent(ApplicationIntent.MainScreenIntent.CloseSheets)
                    }
                )

                DeleteConfirmationDialog(
                    show = deleteState.note != null,
                    noteTitle = deleteState.note?.title.orEmpty(),
                    type = deleteState.note?.getType() ?: NoteType.TEXT,
                    cancel = { intent(ApplicationIntent.MainScreenIntent.Delete(null)) },
                    confirm = { intent(ApplicationIntent.MainScreenIntent.ConfirmDelete) },
                    isTotalDelete = false
                )

                LabelInfoLayer(
                    label = state.selectedLabel,
                    closeLayer = { intent(ApplicationIntent.EditNoteIntent.SelectLabel(STRING_EMPTY)) }
                ) {
                    intent(ApplicationIntent.EditNoteIntent.RemoveLabel(it))
                    intent(ApplicationIntent.EditNoteIntent.SelectLabel(STRING_EMPTY))
                }
            }
        }
        AddLabelBottomSheet(
            show = state.showAddLabelSheet,
            labels = state.unaddedLabels,
            addLabel = { labelId ->
                state.note?.id?.let {
                    intent(
                        ApplicationIntent.EditNoteIntent.AddLabel(
                            labelId = labelId
                        )
                    )
                }
            },
            dismiss = {
                intent(ApplicationIntent.EditNoteIntent.ShowAddLabelMenu(false))
            }
        )
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
    locked: Boolean,
    lock: () -> Unit,
    archive: () -> Unit,
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
        MainMenuItemActionColumn(
            modifier = Modifier.align(Alignment.TopEnd),
            isLayerVisible = isLayerVisible,
            pinned = pinned,
            menuDestination = MenuDestination.LEFT,
            delete = delete,
            onShareClicked = share,
            shareText = shareText,
            pin = pin,
            archive = archive,
            lock = lock,
            ableToLockInPlace = false,
            locked = locked
        )
    }
}

@Composable
fun TitleEdit(
    title: String,
    colorSchema: NoteColorSchema,
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
            color = colorSchema.accent
        ),
        modifier = modifier
            .focusRequester(focusRequester),
        cursorBrush = SolidColor(colorSchema.accent),
        decorationBox = { innerTextField ->
            if (title.isEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.s_title),
                        color = colorSchema.dim,
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
    colorSchema: NoteColorSchema,
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
            color = colorSchema.accent
        ),
        modifier = modifier
            .focusRequester(focusRequester),
        cursorBrush = SolidColor(colorSchema.accent),
        decorationBox = { innerTextField ->
            if (text.isEmpty()) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = stringResource(R.string.s_text),
                        color = colorSchema.dim,
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
    colorSchema: NoteColorSchema,
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
                    scale = sizeScale.value.value,
                    colorSchema = colorSchema
                )
            }
        }
        item {
            Gap(16)
            AddItem(
                colorSchema = colorSchema
            ) {
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
                checkClick = checkClick,
                colorSchema = colorSchema
            )
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun TodoListItemUI(
    modifier: Modifier,
    colorSchema: NoteColorSchema,
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
            tint = colorSchema.accent
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
            tint = colorSchema.accent
        )
        Gap(8)
        BasicTextField(
            value = text,
            onValueChange = { tfv ->
                onChange(tfv, id)
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = colorSchema.accent
            ),
            modifier = Modifier
                .width(fieldWidth.dp)
                .focusRequester(focusRequester),
            cursorBrush = SolidColor(colorSchema.accent),
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
            tint = colorSchema.accent
        )
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SelectedTodoListItemUI(
    modifier: Modifier,
    colorSchema: NoteColorSchema,
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
            tint = colorSchema.dim
        )
        Gap(8)
        BasicTextField(
            enabled = false,
            value = text,
            onValueChange = { tfv ->
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = colorSchema.dim
            ),
            modifier = Modifier
                .width(fieldWidth.dp)
                .focusRequester(focusRequester),
            cursorBrush = SolidColor(colorSchema.dim),
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
            tint = colorSchema.dim
        )
    }
}

@Composable
fun NoteLabel(
    modifier: Modifier = Modifier,
    labelUI: LabelUI,
    onClick: (LabelId) -> Unit = {}
) {
    val colorSchema = LabelColorSchema.fromPref(labelUI.colorPref)
    Box(
        modifier = modifier.clickableNoRipple {
            onClick(labelUI.id)
        }
    ) {
        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .scale(1.3f)
                    .size(42.dp),
                painter = painterResource(R.drawable.ic_label_filled),
                contentDescription = null,
                tint = colorSchema.background
            )
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(start = 10.dp, end = 8.dp)
                    .width(40.dp),
                text = labelUI.name,
                color = colorSchema.textColor,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun AddItem(
    colorSchema: NoteColorSchema,
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
            tint = colorSchema.accent
        )
        Gap(12)
        Text(
            text = stringResource(R.string.s_add_item),
            fontSize = 16.sp,
            color = colorSchema.accent
        )
    }
}
