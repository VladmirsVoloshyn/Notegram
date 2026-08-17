package com.uladzimirv.notegram.ui.elements.top_bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.Anchor
import com.uladzimirv.notegram.ui.theme.buttonPrimary

@Composable
fun NoteTopBar(
    back: () -> Unit,
    delete: () -> Unit
) {
    Row() {
        Icon(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = back
                )
                .size(24.dp),
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = null,
            tint = buttonPrimary,
        )
        Anchor()
        Icon(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = delete
                )
                .size(24.dp),
            painter = painterResource(id = R.drawable.ic_delete),
            contentDescription = null,
            tint = buttonPrimary,
        )

    }
}