package com.uladzimirv.notegram.ui.elements.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.theme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.buttonPrimary
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple

@Composable
fun AddItem(
    modifier: Modifier = Modifier,
    iconResId: Int,
    titleResId: Int,
    isVisible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = modifier
                .wrapContentSize()
                .background(buttonPrimary, RoundedCornerShape(48.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .clickableNoRipple(onClick =  onClick)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = null,
                    tint = backgroundSecondary,
                    modifier = Modifier.size(24.dp)
                )
                Gap(3)
                Text(
                    text = stringResource(titleResId),
                    color = backgroundSecondary,
                    fontSize = 24.sp
                )
            }

        }
    }

}

@Preview
@Composable
fun AddItemPreview() {
    AddItem(
        modifier = Modifier,
        iconResId = R.drawable.ic_text_add,
        titleResId = R.string.s_add_text,
        isVisible = true
    ) {}
}