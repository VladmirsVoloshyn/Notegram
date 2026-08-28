package com.uladzimirv.notegram.ui.elements.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.uladzimirv.notegram.ui.model.TextNoteUI
import com.uladzimirv.notegram.ui.theme.NoteColorSchema
import com.uladzimirv.notegram.ui.theme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.borderPrimary
import com.uladzimirv.notegram.ui.theme.cyan
import com.uladzimirv.notegram.ui.theme.glow
import com.uladzimirv.notegram.ui.theme.orange
import com.uladzimirv.notegram.ui.theme.red
import com.uladzimirv.notegram.util.ifNotEmpty

@Composable
fun MainTextNoteGreedItem(
    modifier: Modifier = Modifier,
    note: TextNoteUI,
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
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
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
                    color = schema.accent,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.padding(end = 18.dp)
                )
                if (note.text.isNotEmpty()) Gap(10)
            }
            note.text.ifNotEmpty {
                Text(
                    text = it,
                    color = schema.accent,
                    fontSize = 14.sp,
                    maxLines = 15,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}