package com.uladzimirv.notegram.ui.layout.note_ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationIntent
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote
import com.uladzimirv.notegram.ui.elements.AppBottomSheet
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.bottom_bar.NoteBottomBar
import com.uladzimirv.notegram.ui.elements.top_bar.NoteTopBar
import com.uladzimirv.notegram.ui.layout.main.DeleteConfirmationDialog
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.ui.layout.note_ui.add_label.AddLabelBottomSheet
import com.uladzimirv.notegram.ui.layout.note_ui.label.LabelInfoLayer
import com.uladzimirv.notegram.ui.model.LabelUI
import com.uladzimirv.notegram.ui.theme.NoteColorSchema
import com.uladzimirv.notegram.util.STRING_EMPTY
import com.uladzimirv.notegram.util.color.darker
import com.uladzimirv.notegram.util.vibration.tickVibrate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoNoteScreen(
    colorMenuOpened: Boolean,
    topMenuOpened: Boolean,
    selectedLabel: LabelUI?,
    showAddLabelSheet: Boolean,
    unaddedLabels: ImmutableList<LabelUI>,
    show: Boolean,
    note: TodoListNote,
    shouldBlur: Boolean,
    deleteState: ApplicationViewState.DeleteState,
    intent: (ApplicationIntent) -> Unit
) {
    val colorSchema = NoteColorSchema.fromPref(note.colorPref)
    val sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val scrollOffset = remember {
        mutableIntStateOf(0)
    }

    val shouldGetTransparency = remember(scrollOffset.intValue) {
        mutableStateOf(scrollOffset.intValue > 0)
    }


    val animatedColor = animateColorAsState(
        targetValue = if (shouldGetTransparency.value) colorSchema.background.darker(
            .8f
        ) else colorSchema.background,
        animationSpec = tween(durationMillis = 200)
    )

    val context = LocalContext.current
    AppBottomSheet(
        sheetState = sheetState,
        showBottomSheet = show,
        backgroundColor = animatedColor.value,
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
                        .fillMaxSize()
                        .let {
                            if (shouldBlur) it.blur(
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(animatedColor.value)
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 12.dp)
                    ) {
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
                                    items = note.labels?.toList().orEmpty()
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
                            title = note.title,
                            colorSchema = colorSchema
                        ) {
                            intent(ApplicationIntent.EditNoteIntent.Title(it))
                        }
                    }
                    TodoEdit(
                        modifier = Modifier.weight(1f),
                        list = note.todoList,
                        delete = { intent(ApplicationIntent.EditNoteIntent.DeleteTodoItem(it)) },
                        checkClick = {
                            intent(
                                ApplicationIntent.EditNoteIntent.CheckTodoItem(
                                    it
                                )
                            )
                        },
                        selectedList = note.selectedTodoList,
                        colorSchema = colorSchema,
                        onScroll = {
                            scrollOffset.value = it
                        },
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

                NoteBottomBar(
                    modifier = Modifier.align(Alignment.BottomCenter).let {
                        if (shouldBlur) it.blur(
                            radius = 6.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        ) else it
                    },
                    pin = {
                        intent(
                            ApplicationIntent.MainScreenIntent.PinOrUnpin(
                                note.id
                            )
                        )
                    },
                    palette = {
                        intent(ApplicationIntent.EditNoteIntent.ShowChangeColorMenu(true))
                    },
                    showLabels = {
                        intent(ApplicationIntent.EditNoteIntent.ShowAddLabelMenu(true))
                    },
                    pinned = note.pinned,
                    colorMenuOpened = colorMenuOpened,
                    colorSchema = colorSchema
                )

                NoteTopMenu(
                    isLayerVisible = topMenuOpened,
                    pinned = note.pinned,
                    shareText = note.toUIModel().summary(),
                    delete = {
                        intent(ApplicationIntent.MainScreenIntent.Delete(note.id))
                        intent(ApplicationIntent.EditNoteIntent.OpenNoteTopMenu(false))
                    },
                    pin = {
                        intent(
                            ApplicationIntent.MainScreenIntent.PinOrUnpin(
                                note.id
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
                    locked = note.locked == true,
                    lock = {},
                    archive = {
                        intent(
                            ApplicationIntent.MainScreenIntent.Archive(
                                note.id
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
                    label = selectedLabel,
                    closeLayer = { intent(ApplicationIntent.EditNoteIntent.SelectLabel(STRING_EMPTY)) }
                ) {
                    intent(ApplicationIntent.EditNoteIntent.RemoveLabel(it))
                    intent(ApplicationIntent.EditNoteIntent.SelectLabel(STRING_EMPTY))
                }
            }
        }
        AddLabelBottomSheet(
            show = showAddLabelSheet,
            labels = unaddedLabels,
            addLabel = { labelId ->
                intent(
                    ApplicationIntent.EditNoteIntent.AddLabel(
                        labelId = labelId
                    )
                )
            },
            dismiss = {
                intent(ApplicationIntent.EditNoteIntent.ShowAddLabelMenu(false))
            }
        )
        ColorSelectorBottomSheet(
            show = colorMenuOpened,
            selected = note.colorPref,
            changeColor = { intent(ApplicationIntent.EditNoteIntent.ChangeColor(it)) },
        ) {
            intent(ApplicationIntent.EditNoteIntent.ShowChangeColorMenu(false))
        }
    }
}
