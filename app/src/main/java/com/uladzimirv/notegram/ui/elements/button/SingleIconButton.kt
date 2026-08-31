package com.uladzimirv.notegram.ui.elements.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.AppTheme.buttonPrimary
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple

@Composable
fun SingleIconButton(
    iconRes: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(buttonPrimary, CircleShape)
            .padding(8.dp)
            .clickableNoRipple(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = backgroundSecondary,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center)

        )
    }

}