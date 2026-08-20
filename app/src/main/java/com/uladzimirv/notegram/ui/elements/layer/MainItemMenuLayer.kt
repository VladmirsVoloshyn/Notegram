package com.uladzimirv.notegram.ui.elements.layer

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.domain.model.note.NoteId
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
import com.uladzimirv.notegram.util.intent.sharePlainText

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

        val shareText = remember {
            when (note) {
                is TextNoteUI -> "${note.title}\n${note.text}"
                is TodoNoteUI -> "${note.title}\n${
                    (note.list + note.selectedList).joinToString(
                        separator = "\n"
                    ) { "${if (it.selected) "+" else "-"} ${it.text}" }
                }"

                else -> ""
            }
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
                shareText = shareText,
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
    shareText: String,
    delete: () -> Unit,
    pin: () -> Unit
) {
    val context = LocalContext.current
    val shareTitle = stringResource(R.string.s_menu_share_title)
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
        val intentErrorString = stringResource(R.string.s_unable_open_link)
        AddItem(
            iconResId = R.drawable.ic_share,
            titleResId = R.string.s_share,
            isVisible = isLayerVisible
        ) {
            context.sharePlainText(
                title = shareTitle,
                text = shareText
            ) {
                Toast.makeText(
                    context,
                    intentErrorString,
                    Toast.LENGTH_LONG
                ).show()
            }
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