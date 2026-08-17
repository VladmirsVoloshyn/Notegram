package com.uladzimirv.notegram.ui.elements

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun RowScope.Gap(width: Int, modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.width(width.dp))
}

@Composable
fun ColumnScope.Gap(height: Int, modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(height.dp))
}

@Composable
fun LazyItemScope.Gap(padding: Int, modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.size(padding.dp))
}

@Composable
fun ColumnScope.Anchor() {
    Spacer(modifier = Modifier.weight(1f))
}

@Composable
fun RowScope.Anchor() {
    Spacer(modifier = Modifier.weight(1f))
}