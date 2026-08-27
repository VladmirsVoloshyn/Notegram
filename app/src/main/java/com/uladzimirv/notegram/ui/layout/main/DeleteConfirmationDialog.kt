package com.uladzimirv.notegram.ui.layout.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.Anchor
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.button.AddItem
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.ui.theme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.borderPrimary
import com.uladzimirv.notegram.ui.theme.buttonPrimary
import com.uladzimirv.notegram.ui.theme.textPrimary
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple

@Composable
fun DeleteConfirmationDialog(
    isTotalDelete: Boolean,
    show: Boolean,
    noteTitle: String,
    type: NoteType,
    confirm: () -> Unit,
    cancel: () -> Unit,
) {
    AnimatedVisibility(
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickableNoRipple(onClick = cancel)
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp))
                    .align(Alignment.Center)
            ) {
                Column(
                    modifier = Modifier
                        .background(backgroundSecondary, RoundedCornerShape(16.dp))
                        .border(
                            width = 1.dp,
                            color = borderPrimary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    val res = remember(
                        isTotalDelete
                    ) {
                        if (isTotalDelete) R.string.s_confirm_delete_title else
                            R.string.s_confirm_trashbox_title
                    }
                    Text(
                        text = stringResource(res),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Gap(16)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val resource = remember {
                            when (type) {
                                NoteType.TEXT -> R.drawable.ic_text_add
                                NoteType.TODO -> R.drawable.ic_todo_list
                                NoteType.QR,
                                NoteType.VOICE -> null

                            }
                        }
                        resource?.let {
                            Icon(
                                painter = painterResource(it),
                                contentDescription = null,
                                tint = buttonPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Gap(8)
                        Text(
                            text = noteTitle,
                            fontSize = 16.sp,
                            color = textPrimary
                        )
                    }
                    Gap(24)
                    Row {
                        Anchor()
                        AddItem(
                            iconResId = R.drawable.ic_delete,
                            titleResId = R.string.s_confirm_delete,
                            isVisible = true,
                            onClick = confirm
                        )
                        Gap(16)
                        AddItem(
                            iconResId = R.drawable.ic_cross,
                            titleResId = R.string.s_cancel,
                            isVisible = true,
                            onClick = cancel
                        )
                    }
                }
            }
        }
    }
}