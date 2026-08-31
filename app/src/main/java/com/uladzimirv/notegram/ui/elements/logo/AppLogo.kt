package com.uladzimirv.notegram.ui.elements.logo

import androidx.compose.foundation.Image
import com.uladzimirv.notegram.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.theme.AppTheme.textPrimary

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    named: Boolean = false,
    showIcon: Boolean = true,
    dark: Boolean = false
) {
    Box(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (showIcon) {
                val logoPainter = remember(dark) {
                    if (dark) R.drawable.app_icon_light
                    else R.drawable.app_icon_dark
                }
                Image(
                    modifier = Modifier
                        .height(40.dp)
                        .width(24.dp)
                        .scale(1.7f),
                    painter = painterResource(logoPainter),
                    contentDescription = null
                )
            }
            if (named && showIcon) Gap(4)
            if (named) {
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 18.sp,
                    color = textPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview
@Composable
fun AppLogoPreview() {
    AppLogo(named = true)
}