package com.uladzimirv.notegram.ui.elements.empty_screen_state

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.theme.AppTheme.textSecondary

@Composable
fun EmptyScreenMessage(
    iconResId : Int,
    messageResId : Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = textSecondary
        )
        Gap(10)
        Text(
            text = stringResource(messageResId),
            modifier = Modifier,
            color = textSecondary
        )
    }
}