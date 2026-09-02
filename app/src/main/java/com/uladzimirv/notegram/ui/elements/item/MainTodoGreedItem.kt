package com.uladzimirv.notegram.ui.elements.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
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
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.ui.model.TodoNoteUI
import com.uladzimirv.notegram.ui.theme.AppTheme.borderPrimary
import com.uladzimirv.notegram.ui.theme.NoteColorSchema
import com.uladzimirv.notegram.util.ifNotEmpty


@Composable
fun MainTodoNoteGreedItem(
    modifier: Modifier = Modifier,
    note: TodoNoteUI,
    onClick: (NoteId) -> Unit = {},
    layoutInfo: ItemLayoutInfo?,
    onLongClick: (id: NoteId, li: ItemLayoutInfo) -> Unit = { it, it1 -> }
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

    val schema = NoteColorSchema.fromPref(note.colorPref)

    val density = LocalDensity.current.density

    Box(
        modifier = modifier.run {
            clip(RoundedCornerShape(14.dp))
                .border(
                    width = 1.dp,
                    color = borderPrimary,
                    shape = RoundedCornerShape(14.dp)
                )
                .background(schema.background)
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
        }
    ) {
        if (note.pinned) {
            Icon(
                painter = painterResource(R.drawable.ic_pin_filled),
                contentDescription = null,
                tint = schema.accent,
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
                    color = schema.accent,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.padding(end = 18.dp)
                )
                if (note.list.isNotEmpty()) Gap(10)
            }
            Column(
                modifier = Modifier.let {
                    if (note.locked) it.blur(
                        radius = 5.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
                    else it
                }
            ) {
                val list = remember(note.list, note.selectedList) {
                    note.list + note.selectedList
                }
                repeat(if (list.size > 5) 5 else list.size) {
                    TodoListItemMainUI(
                        text = list[it].text,
                        selected = list[it].selected,
                        schema = schema
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
        LockIcon(
            show = note.locked,
            color = schema.accent
        )
    }
}

@Composable
fun BoxScope.LockIcon(
    show: Boolean,
    color: Color
) {
    AnimatedVisibility(
        visible = show,
        modifier = Modifier
            .align(Alignment.Center),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_lock),
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp),
            tint = color,
            contentDescription = null
        )
    }
}

@Composable
fun TodoListItemMainUI(
    text: String,
    selected: Boolean,
    schema: NoteColorSchema
) {
    val checkBox = remember(selected) {
        if (selected) R.drawable.ic_checkbox_selected
        else R.drawable.ic_checkbox_unchecked
    }

    val textColor = if (selected) schema.dim
    else schema.accent

    val buttonColor = if (selected) schema.dim
    else schema.accent

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

