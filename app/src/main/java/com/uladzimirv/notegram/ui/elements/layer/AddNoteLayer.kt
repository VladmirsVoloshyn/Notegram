package com.uladzimirv.notegram.ui.elements.layer

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.button.AddItem
import com.uladzimirv.notegram.ui.elements.button.FloatingButton
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple

@Composable
fun AddNoteLayer(
    modifier: Modifier = Modifier,
    isClosed: Boolean,
    add: (NoteType) -> Unit,
    onAddClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .let {
                if (isClosed) it
                    .wrapContentSize()
                else it.fillMaxSize()
            }
            .padding(end = 18.dp, bottom = 64.dp)
            .clickableNoRipple(onClick = onAddClick)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.End
        ) {
            Column(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = tween(durationMillis = 100)
                    ),
                horizontalAlignment = Alignment.End
            ) {
                AddItem(
                    modifier = Modifier,
                    iconResId = R.drawable.ic_label_thin,
                    titleResId = R.string.s_label,
                    isVisible = !isClosed,
                ) {
                    add(NoteType.LABEL)
                }
                Gap(6)
                AddItem(
                    modifier = Modifier,
                    iconResId = R.drawable.ic_todo_list,
                    titleResId = R.string.s_todo,
                    isVisible = !isClosed
                ) {
                    add(NoteType.TODO)
                }
                Gap(6)
                AddItem(
                    modifier = Modifier,
                    iconResId = R.drawable.ic_note_qr,
                    titleResId = R.string.s_add_text_qr,
                    isVisible = !isClosed
                ) {
                    add(NoteType.QR)
                }
                Gap(6)
                AddItem(
                    modifier = Modifier,
                    iconResId = R.drawable.ic_text_add,
                    titleResId = R.string.s_add_text,
                    isVisible = !isClosed
                ) {
                    add(NoteType.TEXT)
                }
            }
            FloatingButton(
                isClosed = isClosed,
                onClick = onAddClick
            )
        }
    }
}