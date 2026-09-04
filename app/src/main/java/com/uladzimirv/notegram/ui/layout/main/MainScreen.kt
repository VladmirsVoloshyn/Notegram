package com.uladzimirv.notegram.ui.layout.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationIntent
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.info_dialog.InfoDialog
import com.uladzimirv.notegram.ui.elements.item.MainTextNoteGreedItem
import com.uladzimirv.notegram.ui.elements.item.MainTodoNoteGreedItem
import com.uladzimirv.notegram.ui.elements.layer.AddNoteLayer
import com.uladzimirv.notegram.ui.elements.layer.GreedItemMenuLayer
import com.uladzimirv.notegram.ui.elements.layer.MainMenuItemActionColumn
import com.uladzimirv.notegram.ui.elements.search_bar.TopSearchBar
import com.uladzimirv.notegram.ui.elements.top_main_menu.TopMainMenu
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.ui.layout.pin_code.PinCodeScreen
import com.uladzimirv.notegram.ui.model.NoteUI
import com.uladzimirv.notegram.ui.model.TextNoteUI
import com.uladzimirv.notegram.ui.model.TodoNoteUI
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.textSecondary
import com.uladzimirv.notegram.util.vibration.tickVibrate
import kotlinx.collections.immutable.ImmutableList

@SuppressLint("FrequentlyChangingValue")
@Composable
fun MainScreen(
    state: ApplicationViewState,
    intent: (ApplicationIntent) -> Unit
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
                .padding(horizontal = 12.dp)
                .let {
                    if (state.main.isAddMenuOpened
                        || state.main.selectedNote != null
                        || state.scannerState.qrScannerResult != null
                        || state.deleteState.note != null
                        || state.topMenuState.show
                    ) it.blur(
                        radius = 4.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
                    else it
                }
        ) {
            val height = remember(listState.scrollIndicatorState?.scrollOffset) {
                50 - ((listState.scrollIndicatorState?.scrollOffset ?: 0) / 8)
            }
            val alpha = remember(listState.scrollIndicatorState?.scrollOffset) {
                val hundAlpha = 1f
                val perc = (80f / 100f)
                val percs = ((listState.scrollIndicatorState?.scrollOffset ?: 0) / perc) / 2
                val aPerc = 1f / 100
                hundAlpha - (aPerc * percs)
            }

            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height.dp)
                        .alpha(alpha)
                ) {
                    TopSearchBar(
                        enabled = state.main.isSearchBarActive,
                        query = state.main.query,
                        onTextChanged = {
                            intent(
                                ApplicationIntent.MainScreenIntent.SearchQuery(
                                    query = it
                                )
                            )
                        },
                        menu = { intent(ApplicationIntent.TopMenuIntent.Show(true)) },
                        isBarEnabled = {
                            intent(
                                ApplicationIntent.MainScreenIntent.OpenSearchBar(
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
                                ApplicationIntent.MainScreenIntent.OpenNote(
                                    it
                                )
                            )
                        },
                        onLongClick = { id, li ->
                            intent(
                                ApplicationIntent.MainScreenIntent.SelectNote(
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

            val infoDialogDecorator = remember(state.infoDialogState.purpose) {
                when (state.infoDialogState.purpose) {
                    ApplicationViewState.InfoDialogState.InfoDialogPurpose.NO_PIN -> R.string.s_tip_no_pin_code to R.string.s_tip_no_pin_code_text
                    ApplicationViewState.InfoDialogState.InfoDialogPurpose.NONE -> R.string.s_tip_no_pin_code to R.string.s_tip_no_pin_code_text
                }
            }
            InfoDialog(
                modifier = Modifier.align(Alignment.BottomCenter),
                infoTextResId = infoDialogDecorator.second,
                titleResId = infoDialogDecorator.first,
                show = state.infoDialogState.show,
                bottomPadding = 200
            ) {
                intent(ApplicationIntent.MainScreenIntent.CloseInfoDialog)
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
                        if (it == NoteType.LABEL) {
                            intent(ApplicationIntent.TopMenuIntent.OpenLabels(true))
                            intent(ApplicationIntent.MainScreenIntent.CloseSheets)
                            intent(ApplicationIntent.LabelIntent.AddLabel)
                        } else {
                            intent(
                                ApplicationIntent.MainScreenIntent.Add(
                                    noteType = it
                                )
                            )
                        }
                    }
                ) {
                    context.tickVibrate()
                    intent(
                        ApplicationIntent.MainScreenIntent.OpenAddMenu(
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
                    close = { intent(ApplicationIntent.MainScreenIntent.CloseSelectionMenu) },
                    actionsContent = { destination ->
                        MainMenuItemActionColumn(
                            isLayerVisible = true,
                            ableToLockInPlace = true,
                            pinned = state.main.selectedNote.note.pinned,
                            menuDestination = destination,
                            locked = state.main.selectedNote.note.locked,
                            lock = {
                                intent(ApplicationIntent.MainScreenIntent.LockOrUnlockNote(state.main.selectedNote.note.id))
                                intent(ApplicationIntent.MainScreenIntent.CloseSelectionMenu)
                            },
                            delete = {
                                intent(ApplicationIntent.MainScreenIntent.Delete(state.main.selectedNote.note.id))
                                intent(ApplicationIntent.MainScreenIntent.CloseSelectionMenu)
                            },
                            pin = {
                                intent(ApplicationIntent.MainScreenIntent.PinOrUnpin(state.main.selectedNote.note.id))
                                intent(ApplicationIntent.MainScreenIntent.CloseSelectionMenu)
                            },
                            shareText = state.main.selectedNote.note.summary(),
                            archive = {
                                intent(ApplicationIntent.MainScreenIntent.Archive(state.main.selectedNote.note.id))
                                intent(ApplicationIntent.MainScreenIntent.CloseSelectionMenu)
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
            cancel = { intent(ApplicationIntent.MainScreenIntent.CloseSheets) },
            confirm = { intent(ApplicationIntent.MainScreenIntent.ConfirmDelete) },
            isTotalDelete = false
        )

        TopMainMenu(
            show = state.topMenuState.show,
            dismiss = { intent(ApplicationIntent.TopMenuIntent.Show(false)) },
            topPadding = innerPadding.calculateTopPadding(),
            openTrashbox = { intent(ApplicationIntent.TopMenuIntent.OpenTrashbox(true)) },
            openArchive = { intent(ApplicationIntent.TopMenuIntent.OpenArchive(true)) },
            openSettings = { intent(ApplicationIntent.TopMenuIntent.OpenSettings(true)) },
            openLabels = { intent(ApplicationIntent.TopMenuIntent.OpenLabels(true)) }
        )

        PinCodeScreen(
            state = state.pinCodeState,
            send = { intent(ApplicationIntent.PinCodeIntent.ProtectedAccess(it)) },
            drop = { intent(ApplicationIntent.PinCodeIntent.DropAttempt) },
            show = state.pinCodeState.callPlace == ApplicationViewState.PinCodeScreenState.PinCodeCallPlace.MAIN_UNLOCKER,
            onUnlock = {
                when (state.pinCodeState.purpose) {
                    is ApplicationViewState.PinCodeScreenState.PinCodePurpose.Unlock -> {
                        intent(ApplicationIntent.MainScreenIntent.UnlockNote(state.pinCodeState.purpose.id))
                    }

                    is ApplicationViewState.PinCodeScreenState.PinCodePurpose.Access -> {
                        intent(ApplicationIntent.MainScreenIntent.AccessNote(state.pinCodeState.purpose.id))
                    }

                    else -> {}
                }
            },
            onDismissRequest = {
                intent(
                    ApplicationIntent.SettingsIntent.ShowPinCode(
                        show = false,
                        purpose = ApplicationViewState.PinCodeScreenState.PinCodePurpose.Close
                    )
                )
            }
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