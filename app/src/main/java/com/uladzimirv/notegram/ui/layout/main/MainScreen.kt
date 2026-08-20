package com.uladzimirv.notegram.ui.layout.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.app_flow.main.contract.MainIntent
import com.uladzimirv.notegram.app_flow.main.contract.MainViewState
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.item.MainTextNoteGreedItem
import com.uladzimirv.notegram.ui.elements.item.MainTodoNoteGreedItem
import com.uladzimirv.notegram.ui.elements.layer.AddNoteLayer
import com.uladzimirv.notegram.ui.elements.layer.MainItemMenuLayer
import com.uladzimirv.notegram.ui.elements.search_bar.TopSearchBar
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.ui.model.TextNoteUI
import com.uladzimirv.notegram.ui.model.TodoNoteUI
import com.uladzimirv.notegram.ui.theme.backgroundPrimary
import com.uladzimirv.notegram.ui.theme.textSecondary
import com.uladzimirv.notegram.util.vibration.tickVibrate

@Composable
fun MainScreen(
    state: MainViewState,
    intent: (MainIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .background(color = backgroundPrimary)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .let {
                    if (state.main.isAddMenuOpened
                        || state.main.selectedNote != null
                        || state.scannerState.qrScannerResult != null
                        || state.deleteState.note != null
                    ) it.blur(
                        4.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
                    else it
                }
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp)
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
                    isBarEnabled = {
                        intent(
                            MainIntent.MainScreenIntent.OpenSearchBar(
                                open = it
                            )
                        )
                    }
                )
                Gap(16)
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (state.main.notes.isEmpty()) {
                            Text(
                                text = stringResource(R.string.s_no_notes),
                                modifier = Modifier.align(Alignment.Center),
                                color = textSecondary
                            )
                        } else {
                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalItemSpacing = 12.dp
                            ) {
                                items(
                                    items = state.main.uiNotes.toList(),
                                    key = { item -> item.id }
                                ) {
                                    when (it) {
                                        is TextNoteUI -> {
                                            MainTextNoteGreedItem(
                                                note = it,
                                                modifier = Modifier.animateItem(),
                                                onClick = {
                                                    intent(
                                                        MainIntent.MainScreenIntent.OpenNote(
                                                            it
                                                        )
                                                    )
                                                },
                                                layoutInfo = null,
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

                                        is TodoNoteUI -> {
                                            MainTodoNoteGreedItem(
                                                note = it,
                                                modifier = Modifier.animateItem(),
                                                onClick = {
                                                    intent(
                                                        MainIntent.MainScreenIntent.OpenNote(
                                                            it
                                                        )
                                                    )
                                                },
                                                layoutInfo = null,
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
                                }

                            }
                        }
                    }

                }
            }
        }
        AnimatedVisibility(
            visible = !state.main.isSearchBarActive
                    && state.main.selectedNote == null
                    && state.scannerState.qrScannerResult == null
                    && state.deleteState.note == null,
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
                MainItemMenuLayer(
                    note = state.main.selectedNote.note,
                    layoutInfo = state.main.selectedNote.layoutInfo,
                    isLayerVisible = true,
                    close = { intent(MainIntent.MainScreenIntent.CloseSelectionMenu) },
                    delete = { intent(MainIntent.MainScreenIntent.Delete(it)) },
                    pin = { intent(MainIntent.MainScreenIntent.PinOrUnpin(it)) }
                )
            }
        }
        DeleteConfirmationDialog(
            show = state.deleteState.note != null,
            noteTitle = state.deleteState.note?.title.orEmpty(),
            type = state.deleteState.note?.getType() ?: NoteType.TEXT,
            cancel = { intent(MainIntent.MainScreenIntent.CloseSheets) },
            confirm = { intent(MainIntent.MainScreenIntent.ConfirmDelete) }
        )
    }
}