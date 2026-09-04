package com.uladzimirv.notegram.ui.layout.trashbox

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationIntent
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.ui.elements.AppBottomSheet
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.info_dialog.InfoDialog
import com.uladzimirv.notegram.ui.elements.layer.TrashboxActionColumn
import com.uladzimirv.notegram.ui.elements.layer.TrashboxGreedItemMenuLayer
import com.uladzimirv.notegram.ui.elements.top_bar.SubScreenTopBar
import com.uladzimirv.notegram.ui.layout.main.DeleteConfirmationDialog
import com.uladzimirv.notegram.ui.layout.main.NotesGreedList
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.AppTheme.buttonPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.buttonSecondary
import com.uladzimirv.notegram.ui.theme.AppTheme.textSecondary
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashboxScreen(
    state: ApplicationViewState.TrashBoxState,
    deleteState: ApplicationViewState.DeleteState,
    intent: (ApplicationIntent) -> Unit
) {
    val showTip = remember {
        mutableStateOf(false)
    }
    val sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    AppBottomSheet(
        backgroundColor = backgroundSecondary,
        sheetState = sheetState,
        showBottomSheet = state.show,
        onDismissRequest = {
            intent(ApplicationIntent.TopMenuIntent.OpenTrashbox(false))
            showTip.value = false
        },
        sheetGesturesEnabled = false
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .background(backgroundSecondary)
                    .padding(top = paddingValues.calculateTopPadding())
                    .fillMaxSize()
                    .let {
                        if (state.selectedNote == null &&
                            deleteState.note == null
                        ) it
                        else it.blur(6.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    }
            ) {
                if (state.trashBox.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = textSecondary
                        )
                        Gap(10)
                        Text(
                            text = stringResource(R.string.s_empty_trashbox),
                            modifier = Modifier,
                            color = textSecondary
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    val scope = rememberCoroutineScope()
                    SubScreenTopBar(
                        iconResId = R.drawable.ic_delete,
                        titleResId = R.string.s_main_menu_trashbox
                    ) {
                        scope.launch {
                            sheetState.hide()
                            delay(100.milliseconds)
                            intent(ApplicationIntent.TopMenuIntent.OpenTrashbox(false))
                        }
                    }
                    Gap(16)
                    NotesGreedList(
                        list = state.trashBox,
                        onClick = {},
                        onLongClick = { id, li ->
                            intent(ApplicationIntent.TrashBoxIntent.SelectNote(id, li))
                        }
                    )
                }
                DoubleButtonNestedBottomBar(
                    isEmpty = state.trashBox.isEmpty(),
                    modifier = Modifier.align(Alignment.BottomCenter),
                    clearTrashbox = { intent(ApplicationIntent.TrashBoxIntent.ClearTrashbox) },
                    showInfo = {
                        showTip.value = true
                    }
                )

                InfoDialog(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    infoTextResId = R.string.s_tip_trashbox_text,
                    show = showTip.value,
                ) {
                    showTip.value = false
                }
            }
            AnimatedVisibility(
                visible = state.selectedNote != null,
                modifier = Modifier.fillMaxSize(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                state.selectedNote?.let { note ->
                    TrashboxGreedItemMenuLayer(
                        note = note.note,
                        layoutInfo = note.layoutInfo,
                        close = { intent(ApplicationIntent.TrashBoxIntent.CloseSelectionMenu) },
                        actionsContent = { destination ->
                            TrashboxActionColumn(
                                isLayerVisible = true,
                                menuDestination = destination,
                                removeFromTrashbox = {
                                    intent(
                                        ApplicationIntent.MainScreenIntent.Delete(
                                            note.note.id
                                        )
                                    )
                                    intent(ApplicationIntent.TrashBoxIntent.CloseSelectionMenu)
                                },
                                restore = {
                                    intent(ApplicationIntent.TrashBoxIntent.Restore(note.note.id))
                                    intent(ApplicationIntent.TrashBoxIntent.CloseSelectionMenu)
                                }
                            )
                        }
                    )
                }
            }

            DeleteConfirmationDialog(
                isTotalDelete = true,
                show = deleteState.note != null,
                noteTitle = deleteState.note?.title.orEmpty(),
                type = deleteState.note?.getType() ?: NoteType.TEXT,
                cancel = { intent(ApplicationIntent.MainScreenIntent.CloseSheets) },
                confirm = { intent(ApplicationIntent.MainScreenIntent.ConfirmDelete) }
            )
        }
    }
}

@Composable
fun DoubleButtonNestedBottomBar(
    isEmpty: Boolean,
    modifier: Modifier = Modifier,
    clearTrashbox: () -> Unit,
    showInfo: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickableNoRipple(onClick = clearTrashbox)
    ) {
        if (!isEmpty) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((16 + 24 + 16).dp)
                    .background(buttonPrimary, CircleShape)
                    .padding(start = 16.dp)
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        modifier = Modifier.size(24.dp),
                        contentDescription = null,
                        tint = buttonSecondary
                    )
                    Gap(8)
                    Text(
                        text = stringResource(R.string.s_trashbox_clear),
                        fontSize = 18.sp,
                        color = buttonSecondary
                    )
                }

            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .border(color = backgroundPrimary, shape = CircleShape, width = 2.dp)
                .background(buttonPrimary, CircleShape)
                .clickableNoRipple(onClick = showInfo)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                modifier = Modifier.size(24.dp),
                contentDescription = null,
                tint = buttonSecondary
            )
        }
    }
}