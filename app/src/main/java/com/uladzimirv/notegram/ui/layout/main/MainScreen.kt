package com.uladzimirv.notegram.ui.layout.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateBounds
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import com.uladzimirv.notegram.ui.elements.item.MainGreedItem
import com.uladzimirv.notegram.ui.elements.layer.AddNoteLayer
import com.uladzimirv.notegram.ui.elements.layer.MainItemMenuLayer
import com.uladzimirv.notegram.ui.elements.search_bar.TopSearchBar
import com.uladzimirv.notegram.ui.theme.backgroundPrimary
import com.uladzimirv.notegram.ui.theme.textSecondary
import com.uladzimirv.notegram.util.vibration.clickVibrate
import com.uladzimirv.notegram.util.vibration.tickVibrate

@Composable
fun MainScreen(
    state: MainViewState.MainScreenSubstate,
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
                    if (state.isAddMenuOpened || state.selectedNote != null) it.blur(
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
                    enabled = state.isSearchBarActive,
                    query = state.query,
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
                        if (state.notes.isEmpty()) {
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
                                    items = state.uiNotes.toList(),
                                    key = { item -> item.id }
                                ) {
                                    MainGreedItem(
                                        note = it,
                                        modifier = Modifier.animateItem(),
                                        onClick = { intent(MainIntent.MainScreenIntent.OpenNote(it)) },
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
        AnimatedVisibility(
            visible = !state.isSearchBarActive && state.selectedNote == null,
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
                    isClosed = !state.isAddMenuOpened,
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
                            open = !state.isAddMenuOpened
                        )
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = state.selectedNote != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            state.selectedNote?.let {
                MainItemMenuLayer(
                    note = state.selectedNote.note,
                    layoutInfo = state.selectedNote.layoutInfo,
                    isLayerVisible = true,
                    close = { intent(MainIntent.MainScreenIntent.CloseSelectionMenu) },
                    delete = { intent(MainIntent.MainScreenIntent.Delete(it)) },
                    pin = { intent(MainIntent.MainScreenIntent.PinOrUnpin(it)) }
                )
            }

        }
    }
}