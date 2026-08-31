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
import com.uladzimirv.notegram.ui.theme.NoteColorSchema

@Composable
fun NoteTopBar(
    colorSchema: NoteColorSchema,
    back: () -> Unit,
    onClick: () -> Unit,
) {
    Row {
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
            tint = colorSchema.accent,
        )
        Anchor()
        Icon(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
                .size(24.dp),
            painter = painterResource(id = R.drawable.ic_menu_dots),
            contentDescription = null,
            tint = colorSchema.accent,
        )

    }
}