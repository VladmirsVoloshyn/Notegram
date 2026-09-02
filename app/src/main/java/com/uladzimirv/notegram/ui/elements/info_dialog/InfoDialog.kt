package com.uladzimirv.notegram.ui.elements.info_dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.Anchor
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.theme.AppTheme.buttonPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.buttonSecondary
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple

@Composable
fun InfoDialog(
    modifier: Modifier = Modifier,
    infoTextResId: Int,
    titleResId : Int = R.string.s_tip_title,
    show: Boolean,
    bottomPadding : Int = 70,
    close: () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = bottomPadding.dp)
                .background(buttonPrimary, shape = RoundedCornerShape(12.dp))

        ) {
            Column(
                modifier
                    .fillMaxWidth()
                    .clickableNoRipple(onClick = close)
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(titleResId),
                        color = buttonSecondary,
                        fontSize = 16.sp
                    )
                    Anchor()
                    Icon(
                        modifier = Modifier
                            .size(18.dp),
                        painter = painterResource(R.drawable.ic_cross),
                        contentDescription = null,
                        tint = buttonSecondary
                    )
                }
                Gap(16)
                Text(
                    text = stringResource(infoTextResId),
                    color = buttonSecondary
                )
            }
        }
    }

}