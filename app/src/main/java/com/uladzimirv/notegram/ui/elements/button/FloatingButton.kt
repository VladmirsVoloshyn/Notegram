package com.uladzimirv.notegram.ui.elements.button


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.AppTheme.buttonPrimary

@Composable
fun FloatingButton(
    modifier: Modifier = Modifier,
    isClosed: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(6.dp)
            .shadow(elevation = 3.dp, shape = CircleShape)
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .background(buttonPrimary, CircleShape)
                .clickable(onClick = onClick)
        ) {
            val resId = remember(isClosed) {
                if (isClosed) {
                    R.drawable.ic_add
                } else {
                    R.drawable.ic_arrow_cool_down
                }

            }
            Icon(
                painter = painterResource(resId),
                contentDescription = "add",
                tint = backgroundSecondary,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp)
            )
        }
    }

}

@Preview
@Composable
fun FloatingButtonPreview() {
    Box(
        modifier = Modifier
            .background(Color.White)
            .size(56.dp)
    ) {
        FloatingButton(
            modifier = Modifier.align(Alignment.Center),
            isClosed = false
        ) {}
    }
}