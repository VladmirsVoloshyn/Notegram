package com.uladzimirv.notegram.ui.layout.main.com

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Rect

@Immutable
data class ItemLayoutInfo(
    private val width: Int,
    private val height: Int,
    private val position: Rect
) {

    fun getHeight(density: Float): Float {
        return height / density
    }

    fun getWidth(density: Float): Float {
        return width / density
    }

    fun getX(density: Float, paddingReduction: Int): Float {
        return (position.left / density) - paddingReduction
    }

    fun getY(density: Float, paddingReduction: Int): Float {
        return position.top / density - paddingReduction
    }

}