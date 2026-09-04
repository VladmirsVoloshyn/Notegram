package com.uladzimirv.notegram.ui.elements.top_bar

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.text.HeaderText
import com.uladzimirv.notegram.ui.theme.AppTheme.buttonPrimary
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple

@Composable
fun BottomSheetSecondaryTopBar(
    @StringRes
    titleResId: Int,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HeaderText(
            text = titleResId,
        )
        Icon(
            modifier = Modifier
                .clickableNoRipple(onClick = onClose)
                .size(24.dp),
            tint = buttonPrimary,
            painter = painterResource(R.drawable.ic_cross),
            contentDescription = null
        )
    }
}