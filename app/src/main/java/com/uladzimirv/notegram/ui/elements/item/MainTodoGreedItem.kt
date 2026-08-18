package com.uladzimirv.notegram.ui.elements.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.ui.model.TodoNoteUI
import com.uladzimirv.notegram.ui.theme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.borderPrimary
import com.uladzimirv.notegram.ui.theme.buttonPrimary
import com.uladzimirv.notegram.ui.theme.buttonSecondary
import com.uladzimirv.notegram.ui.theme.cyan
import com.uladzimirv.notegram.ui.theme.glow
import com.uladzimirv.notegram.ui.theme.orange
import com.uladzimirv.notegram.ui.theme.pink
import com.uladzimirv.notegram.ui.theme.textPrimary
import com.uladzimirv.notegram.ui.theme.textSecondary
import com.uladzimirv.notegram.util.VEVO
import com.uladzimirv.notegram.util.ifNotEmpty


@Composable
fun MainTodoNoteGreedItem(
    modifier: Modifier = Modifier,
    note: TodoNoteUI,
    onClick: (NoteId) -> Unit,
    layoutInfo: ItemLayoutInfo?,
    onLongClick: (id: NoteId, li: ItemLayoutInfo) -> Unit
) {
    val width = remember {
        mutableIntStateOf(0)
    }

    val height = remember {
        mutableIntStateOf(0)
    }
    val pos = remember {
        mutableStateOf(Rect.Zero)
    }

    val background =
        when (note.colorPref) {
            ColorPref.COMMON -> backgroundSecondary
            ColorPref.ORANGE -> orange
            ColorPref.CYAN -> cyan
            ColorPref.GLOW -> glow
            ColorPref.PINK -> pink
        }

    val density = LocalDensity.current.density

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = 1.dp,
                color = borderPrimary,
                shape = RoundedCornerShape(14.dp)
            )
            .background(background)
            .padding(8.dp)
            .onGloballyPositioned { lc ->
                width.intValue = lc.size.width
                height.intValue = lc.size.height
                pos.value = lc.boundsInRoot()
            }
            .let {
                if (layoutInfo == null) it
                else it.size(
                    height = layoutInfo.getHeight(density).dp,
                    width = layoutInfo.getWidth(density).dp
                )
            }
            .combinedClickable(
                onClick = { onClick(note.id) },
                onLongClick = {
                    onLongClick(
                        note.id,
                        ItemLayoutInfo(
                            width = width.intValue,
                            height = height.intValue,
                            position = pos.value
                        )
                    )
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )

    ) {
        if (note.pinned) {
            Icon(
                painter = painterResource(R.drawable.ic_pin_filled),
                contentDescription = null,
                modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.TopEnd)
            )
        }

        Column {
            note.title.ifNotEmpty {
                Text(
                    text = it,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.padding(end = 18.dp)
                )
                if (note.list.isNotEmpty()) Gap(10)
            }
            val list = remember(note.list, note.selectedList) {
                note.list + note.selectedList
            }
            VEVO(list)
            repeat(if (list.size > 5) 5 else list.size) {
                TodoListItemMainUI(
                    text = list[it].text,
                    selected = list[it].selected
                )
                Gap(6)
            }
            if (list.size > 5) {
                Text(
                    text = "...",
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun TodoListItemMainUI(
    text: String,
    selected: Boolean
) {
    val checkBox = remember(selected) {
        if (selected) R.drawable.ic_checkbox_selected
        else R.drawable.ic_checkbox_unchecked
    }

    val textColor = if (selected) textSecondary
    else textPrimary

    val buttonColor = if (selected) buttonSecondary
    else buttonPrimary

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(checkBox),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = buttonColor
        )
        Gap(3)
        Text(
            text = text,
            fontSize = 12.sp,
            color = textColor
        )
    }
}

