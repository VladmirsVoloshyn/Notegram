package com.uladzimirv.notegram.ui.elements.layer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.button.AddItem
import com.uladzimirv.notegram.ui.elements.item.MainTextNoteGreedItem
import com.uladzimirv.notegram.ui.elements.item.MainTodoNoteGreedItem
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.ui.layout.main.com.MenuDestination
import com.uladzimirv.notegram.ui.model.NoteUI
import com.uladzimirv.notegram.ui.model.TextNoteUI
import com.uladzimirv.notegram.ui.model.TodoNoteUI
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple
import com.uladzimirv.notegram.util.orZero

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun TrashboxGreedItemMenuLayer(
    note: NoteUI,
    layoutInfo: ItemLayoutInfo?,
    modifier: Modifier = Modifier,
    close: () -> Unit,
    actionsContent: @Composable BoxScope.(menuDestination: MenuDestination) -> Unit
) {
    val density = LocalDensity.current.density
    val configuration = LocalConfiguration.current.screenWidthDp

    val vertical = layoutInfo?.getY(density, 50)?.dp ?: 0.dp
    val horizontal = layoutInfo?.getX(density, 8)?.dp ?: 0.dp

    val menuDestination = remember {
        if (horizontal.value > (configuration / 2)) {
            MenuDestination.LEFT
        } else MenuDestination.RIGHT
    }

    val menuHorizontalOffset = remember {
        when (menuDestination) {
            MenuDestination.LEFT -> horizontal.value - (layoutInfo?.getWidth(density).orZero() - 40f)
            MenuDestination.RIGHT -> horizontal.value
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickableNoRipple(onClick = close)
    ) {
        Box(
            modifier = Modifier
                .offset(
                    x = horizontal,
                    y = vertical
                )
        ) {
            when (note) {
                is TextNoteUI -> {
                    MainTextNoteGreedItem(
                        note = note,
                        layoutInfo = layoutInfo
                    )
                }

                is TodoNoteUI -> {
                    MainTodoNoteGreedItem(
                        note = note,
                        layoutInfo = layoutInfo
                    )
                }
            }
        }
        val topPos = layoutInfo?.getY(density, 30).orZero()
        val height = layoutInfo?.getHeight(density).orZero()
        Box(
            modifier = Modifier.offset(
                y = (topPos + height).dp,
                x = menuHorizontalOffset.dp
            )
        ) {
            actionsContent(menuDestination)
        }
    }
}

@Composable
fun TrashboxActionColumn(
    modifier: Modifier = Modifier,
    isLayerVisible: Boolean,
    menuDestination: MenuDestination,
    removeFromTrashbox: () -> Unit,
    restore: () -> Unit
) {
    val alignment = remember {
        when (menuDestination) {
            MenuDestination.LEFT -> Alignment.End
            MenuDestination.RIGHT -> Alignment.Start
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {
        AddItem(
            iconResId = R.drawable.ic_delete,
            titleResId = R.string.s_delete_from_trashbox,
            isVisible = isLayerVisible,
            onClick = removeFromTrashbox
        )
        Gap(6)
        AddItem(
            iconResId = R.drawable.ic_restore,
            titleResId = R.string.s_restore,
            isVisible = isLayerVisible,
            onClick = restore
        )
    }
}