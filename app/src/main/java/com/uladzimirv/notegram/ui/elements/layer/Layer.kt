package com.uladzimirv.notegram.ui.elements.layer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple

@Composable
fun BoxScope.Layer(
    contentPadding: PaddingValues = PaddingValues(),
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable (BoxScope.() -> Unit)
) {
    Box(
        modifier = Modifier
            .align(contentAlignment)
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        content()
    }
}

@Composable
fun Layer(
    contentPadding: PaddingValues = PaddingValues(),
    contentAlignment: Alignment = Alignment.Center,
    interceptTouch: () -> Unit =  {},
    layerVisible: Boolean = true,
    content: @Composable (BoxScope.() -> Unit)
) {
    if (!layerVisible) return
    Box(
        modifier = Modifier
            .clickableNoRipple(onClick = interceptTouch)
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(contentAlignment)
        ) {
            content()
        }
    }
}