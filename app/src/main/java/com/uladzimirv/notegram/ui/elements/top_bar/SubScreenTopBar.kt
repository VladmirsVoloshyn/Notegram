package com.uladzimirv.notegram.ui.elements.top_bar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.Anchor
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.theme.buttonPrimary
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple

@Composable
fun SubScreenTopBar(
    modifier: Modifier = Modifier,
    iconResId: Int,
    titleResId: Int,
    onClose: () -> Unit
) {
    Box(
        modifier = modifier.padding(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                Icon(
                    tint = buttonPrimary,
                    painter = painterResource(iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Gap(8)
                Text(
                    text = stringResource(titleResId),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Anchor()
                Icon(
                    painter = painterResource(R.drawable.ic_cross),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp)
                        .clickableNoRipple(onClick = onClose)
                )
            }
        }
    }


}