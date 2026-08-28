package com.uladzimirv.notegram.ui.layout.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.app_flow.main.contract.MainIntent
import com.uladzimirv.notegram.app_flow.main.contract.MainViewState
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.item.MainTextNoteGreedItem
import com.uladzimirv.notegram.ui.elements.item.MainTodoNoteGreedItem
import com.uladzimirv.notegram.ui.elements.layer.AddNoteLayer
import com.uladzimirv.notegram.ui.elements.layer.GreedItemMenuLayer
import com.uladzimirv.notegram.ui.elements.layer.MainMenuItemActionColumn
import com.uladzimirv.notegram.ui.elements.search_bar.TopSearchBar
import com.uladzimirv.notegram.ui.elements.top_main_menu.TopMainMenu
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.ui.model.NoteUI
import com.uladzimirv.notegram.ui.model.TextNoteUI
import com.uladzimirv.notegram.ui.model.TodoNoteUI
import com.uladzimirv.notegram.ui.theme.backgroundPrimary
import com.uladzimirv.notegram.ui.theme.textSecondary
import com.uladzimirv.notegram.util.vibration.tickVibrate
import kotlinx.collections.immutable.ImmutableList

@SuppressLint("FrequentlyChangingValue")
@Composable
fun MainScreen(
    state: MainViewState,
    intent: (MainIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        val listState = rememberLazyStaggeredGridState()
        val isSearchBarVisible = remember {
            mutableStateOf(true)
        }
        Box(
            modifier = Modifier.fillMaxSize()
                .background(color = backgroundPrimary)
                .let {
                    val padding = innerPadding.calculateTopPadding() -
                            ((listState.scrollIndicatorState?.scrollOffset ?: 0) / 2).dp
                    it.padding(
                        top = if (padding.value.toInt() > 0) padding else 0.dp
                    )
                }
                .let {
                    if (isSearchBarVisible.value) it.padding(top = 12.dp)
                    else it
                }
                .padding(horizontal = 12.dp)
                .let {
                    if (state.main.isAddMenuOpened
                        || state.main.selectedNote != null
                        || state.scannerState.qrScannerResult != null
                        || state.deleteState.note != null
                        || state.topMenuState.show
                    ) it.blur(
                        4.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
                    else it
                }
        ) {

            Column(
                modifier = Modifier.let {
                    if (isSearchBarVisible.value) it.padding(top = 12.dp)
                    else it
                }
            ) {
                AnimatedVisibility(
                    visible = isSearchBarVisible.value,
                    enter = expandVertically(
                        expandFrom = Alignment.Top,
                        animationSpec = tween(durationMillis = 100)
                    ),
                    exit = shrinkVertically(
                        shrinkTowards = Alignment.Top,
                        animationSpec = tween(durationMillis = 100)
                    )
                ) {
                    TopSearchBar(
                        enabled = state.main.isSearchBarActive,
                        query = state.main.query,
                        onTextChanged = {
                            intent(
                                MainIntent.MainScreenIntent.SearchQuery(
                                    query = it
                                )
                            )
                        },
                        menu = { intent(MainIntent.TopMenuIntent.Show(true)) },
                        isBarEnabled = {
                            intent(
                                MainIntent.MainScreenIntent.OpenSearchBar(
                                    open = it
                                )
                            )
                        }
                    )
                }

                Gap(16)


                LaunchedEffect(listState.scrollIndicatorState?.scrollOffset) {
                    isSearchBarVisible.value = listState.scrollIndicatorState?.scrollOffset == 0
                }
                if (state.main.uiNotes.isNotEmpty()) {
                    NotesGreedList(
                        state = listState,
                        list = state.main.uiNotes,
                        onClick = {
                            intent(
                                MainIntent.MainScreenIntent.OpenNote(
                                    it
                                )
                            )
                        },
                        onLongClick = { id, li ->
                            intent(
                                MainIntent.MainScreenIntent.SelectNote(
                                    noteId = id,
                                    itemLayoutInfo = li
                                )
                            )
                        }
                    )
                }
            }
            if (state.main.uiNotes.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_note),
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = textSecondary
                    )
                    Gap(10)
                    Text(
                        text = stringResource(R.string.s_no_notes),
                        modifier = Modifier,
                        color = textSecondary
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !state.main.isSearchBarActive
                    && state.main.selectedNote == null
                    && state.scannerState.qrScannerResult == null
                    && state.deleteState.note == null
                    && !state.topMenuState.show,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val context = LocalContext.current
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AddNoteLayer(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    isClosed = !state.main.isAddMenuOpened,
                    add = {
                        intent(
                            MainIntent.MainScreenIntent.Add(
                                noteType = it
                            )
                        )
                    }
                ) {
                    context.tickVibrate()
                    intent(
                        MainIntent.MainScreenIntent.OpenAddMenu(
                            open = !state.main.isAddMenuOpened
                        )
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = state.main.selectedNote != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            state.main.selectedNote?.let {
                GreedItemMenuLayer(
                    note = state.main.selectedNote.note,
                    layoutInfo = state.main.selectedNote.layoutInfo,
                    close = { intent(MainIntent.MainScreenIntent.CloseSelectionMenu) },
                    actionsContent = { destination ->
                        MainMenuItemActionColumn(
                            isLayerVisible = true,
                            pinned = state.main.selectedNote.note.pinned,
                            menuDestination = destination,
                            delete = {
                                intent(MainIntent.MainScreenIntent.Delete(state.main.selectedNote.note.id))
                                intent(MainIntent.MainScreenIntent.CloseSelectionMenu)
                            },
                            pin = {
                                intent(MainIntent.MainScreenIntent.PinOrUnpin(state.main.selectedNote.note.id))
                                intent(MainIntent.MainScreenIntent.CloseSelectionMenu)
                            },
                            shareText = state.main.selectedNote.note.shareText(),
                            archive = {
                                intent(MainIntent.MainScreenIntent.Archive(state.main.selectedNote.note.id))
                                intent(MainIntent.MainScreenIntent.CloseSelectionMenu)
                            }
                        )
                    }
                )
            }
        }

        DeleteConfirmationDialog(
            show = state.deleteState.note != null,
            noteTitle = state.deleteState.note?.title.orEmpty(),
            type = state.deleteState.note?.getType() ?: NoteType.TEXT,
            cancel = { intent(MainIntent.MainScreenIntent.CloseSheets) },
            confirm = { intent(MainIntent.MainScreenIntent.ConfirmDelete) },
            isTotalDelete = false
        )

        TopMainMenu(
            show = state.topMenuState.show,
            dismiss = { intent(MainIntent.TopMenuIntent.Show(false)) },
            topPadding = innerPadding.calculateTopPadding(),
            openTrashbox = { intent(MainIntent.TopMenuIntent.OpenTrashbox(true)) },
            openArchive = { intent(MainIntent.TopMenuIntent.OpenArchive(true)) }
        )
    }
}

@Composable
fun NotesGreedList(
    list: ImmutableList<NoteUI>,
    onClick: (id: String) -> Unit,
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    onLongClick: (id: String, li: ItemLayoutInfo) -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            LazyVerticalStaggeredGrid(
                state = state,
                columns = StaggeredGridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp
            ) {
                items(
                    items = list,
                    key = { item -> item.id }
                ) {
                    when (it) {
                        is TextNoteUI -> {
                            MainTextNoteGreedItem(
                                note = it,
                                modifier = Modifier.animateItem(),
                                onClick = onClick,
                                layoutInfo = null,
                                onLongClick = onLongClick
                            )
                        }

                        is TodoNoteUI -> {
                            MainTodoNoteGreedItem(
                                note = it,
                                modifier = Modifier.animateItem(),
                                onClick = onClick,
                                layoutInfo = null,
                                onLongClick = onLongClick
                            )
                        }
                    }
                }
                item {
                    Box(modifier = Modifier.height(300.dp))
                }
            }
        }
    }
}