package com.uladzimirv.notegram.ui.elements.layer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.app_flow.main.contract.MainIntent
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.button.AddItem
import com.uladzimirv.notegram.ui.elements.item.MainGreedItem
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.ui.layout.main.com.MenuDestination
import com.uladzimirv.notegram.ui.model.NoteUI
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MainItemMenuLayer(
    note: NoteUI,
    layoutInfo: ItemLayoutInfo?,
    modifier: Modifier = Modifier,
    isLayerVisible: Boolean,
    close: () -> Unit,
    delete: (NoteId) -> Unit,
    pin: (NoteId) -> Unit
) {
    val density = LocalDensity.current.density
    val configuration = LocalConfiguration.current.screenWidthDp

    val vertical = layoutInfo?.getY(density, 8)?.dp ?: 0.dp
    val horizontal = layoutInfo?.getX(density, 8)?.dp ?: 0.dp

    val menuDestination = remember {
        if (horizontal.value > (configuration / 2)) {
            MenuDestination.LEFT
        } else MenuDestination.RIGHT
    }

    //TODO ??? From where?
    val infelicityRight = 30
    val infelicityLeft = 15

    val menuHorizontalOffset = remember {
        when (menuDestination) {
            MenuDestination.LEFT -> horizontal.value - ((layoutInfo?.getWidth(density)
                ?: 0f) - infelicityLeft)

            MenuDestination.RIGHT -> horizontal.value + ((layoutInfo?.getWidth(density)
                ?: 0f) + infelicityRight)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickableNoRipple(onClick = close)
    ) {
        Box(
            modifier = Modifier.offset(
                x = horizontal,
                y = vertical
            )
        ) {
            MainGreedItem(
                note = note,
                onClick = {},
                layoutInfo = layoutInfo,
                onLongClick = { it, it1 -> }
            )
        }
        Box(
            modifier = Modifier.offset(
                x = menuHorizontalOffset.dp,
                y = vertical
            )
        ) {
            ActionColumn(
                isLayerVisible = isLayerVisible,
                pinned = note.pinned,
                menuDestination = menuDestination,
                delete = {
                    delete(note.id)
                    close()
                },
                pin = {
                    pin(note.id)
                    close()
                }
            )
        }
    }
}

@Composable
fun ActionColumn(
    isLayerVisible: Boolean,
    pinned: Boolean,
    menuDestination: MenuDestination,
    delete: () -> Unit,
    pin: () -> Unit
) {
    val alignment = remember {
        when (menuDestination) {
            MenuDestination.LEFT -> Alignment.End
            MenuDestination.RIGHT -> Alignment.Start
        }
    }
    Column(
        modifier = Modifier,
        horizontalAlignment = alignment
    ) {
        AddItem(
            iconResId = R.drawable.ic_delete,
            titleResId = R.string.s_delete,
            isVisible = isLayerVisible,
            onClick = delete
        )
        Gap(6)
        AddItem(
            iconResId = R.drawable.ic_share,
            titleResId = R.string.s_share,
            isVisible = isLayerVisible
        ) {

        }
        Gap(6)
        val valuesPair = remember {
            if (pinned) R.drawable.ic_pin_filled to R.string.s_unpin
            else R.drawable.ic_pin_unfilled to R.string.s_pin
        }
        AddItem(
            iconResId = valuesPair.first,
            titleResId = valuesPair.second,
            isVisible = isLayerVisible,
            onClick = pin
        )
    }
}