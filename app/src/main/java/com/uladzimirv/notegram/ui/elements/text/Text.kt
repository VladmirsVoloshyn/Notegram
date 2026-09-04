package com.uladzimirv.notegram.ui.elements.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.ui.theme.AppTheme.textPrimary

@Composable
fun HeaderText(
    text: Int
) {
    Text(
        text = stringResource(text),
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = textPrimary
    )
}