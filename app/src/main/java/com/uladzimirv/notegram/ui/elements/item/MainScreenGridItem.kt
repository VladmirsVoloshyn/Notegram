package com.uladzimirv.notegram.ui.elements.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.ui.model.LabelUI
import com.uladzimirv.notegram.ui.model.TextNoteUI
import com.uladzimirv.notegram.ui.theme.AppTheme.borderPrimary
import com.uladzimirv.notegram.ui.theme.LabelColorSchema
import com.uladzimirv.notegram.ui.theme.NoteColorSchema
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
        Column(
            modifier = Modifier
        ) {
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
                    modifier = Modifier.let {
                        if (note.locked) it.blur(
                            radius = 5.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )
                        else it
                    },
                    text = it,
                    color = schema.accent,
                    fontSize = 14.sp,
                    maxLines = 25,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Box(
                modifier = Modifier
                    .let {
                        if (note.labels.isEmpty()) it.height(0.dp) else it.heightIn(0.dp, 120.dp)
                    }
                    .let {
                        if (note.locked) {
                            it.blur(6.dp, BlurredEdgeTreatment.Unbounded)
                        } else it.background(schema.background.copy(alpha = .9f))
                    }
                    .padding(top = 4.dp, bottom = 6.dp)
            ) {
                LazyVerticalGrid(
                    modifier= Modifier,
                    columns = GridCells.Fixed(5),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(
                        items = note.labels.toList(),
                        key = { it.id }
                    ) { label ->
                        LabelGridMiniatureItem(
                            label = label
                        )
                    }
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
fun LabelGridMiniatureItem(
    label: LabelUI
) {
    val colorSchema = LabelColorSchema.fromPref(label.colorPref)
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .scale(1.8f)
                    .size(16.dp),
                painter = painterResource(R.drawable.ic_label_filled),
                contentDescription = null,
                tint = colorSchema.background
            )
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(17.dp),
                text = label.name,
                color = colorSchema.textColor,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                fontSize = 8.sp
            )
        }
    }

}

