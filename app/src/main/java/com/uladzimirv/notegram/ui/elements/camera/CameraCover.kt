package com.uladzimirv.notegram.ui.elements.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
internal fun CameraCover(scanned: Boolean) {
    val borderColor = if (scanned) Color.Green else Color.White

    val arcRadius: Float
    val lineLength: Float
    val strokeWidth: Float

    with(LocalDensity.current) {
        arcRadius = 12.dp.toPx()
        lineLength = 48.dp.toPx()
        strokeWidth = 3.dp.toPx()
    }

    val arcStyle = Stroke(width = strokeWidth)
    val lineStyle = Stroke(width = strokeWidth, cap = StrokeCap.Round)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                val overlayColor = Color.Black.copy(alpha = 0.6f)
                val squareSize = size.width * 0.7f
                val left = (size.width - squareSize) / 2f
                val top = (size.height - squareSize) / 2f

                // Cut square for scanning QR
                clipPath(
                    path = Path().apply {
                        addRoundRect(
                            RoundRect(
                                rect = Rect(left, top, left + squareSize, top + squareSize),
                                cornerRadius = CornerRadius(arcRadius)
                            )
                        )
                    },
                    clipOp = ClipOp.Difference
                ) {
                    drawRect(overlayColor)
                }

                //corners
                drawCorner(
                    arcStartAngle = 180f,
                    arcTopLeft = Offset(left, top),
                    horLineStart = Offset(left + arcRadius, top),
                    horLineEnd = Offset(left + arcRadius + lineLength, top),
                    verLineStart = Offset(left, top + arcRadius),
                    verLineEnd = Offset(left, top + arcRadius + lineLength),
                    arcRadius = arcRadius,
                    lineStyle = lineStyle,
                    arcStyle = arcStyle,
                    borderColor = borderColor,
                )

                drawCorner(
                    arcStartAngle = 270f,
                    arcTopLeft = Offset(left + squareSize - arcRadius * 2, top),
                    horLineStart = Offset(left + squareSize - arcRadius, top),
                    horLineEnd = Offset(left + squareSize - arcRadius - lineLength, top),
                    verLineStart = Offset(left + squareSize, top + arcRadius),
                    verLineEnd = Offset(left + squareSize, top + arcRadius + lineLength),
                    arcRadius = arcRadius,
                    lineStyle = lineStyle,
                    arcStyle = arcStyle,
                    borderColor = borderColor,
                )

                drawCorner(
                    arcStartAngle = 90f,
                    arcTopLeft = Offset(left, top + squareSize - arcRadius * 2),
                    horLineStart = Offset(left + arcRadius, top + squareSize),
                    horLineEnd = Offset(left + arcRadius + lineLength, top + squareSize),
                    verLineStart = Offset(left, top + squareSize - arcRadius),
                    verLineEnd = Offset(left, top + squareSize - arcRadius - lineLength),
                    arcRadius = arcRadius,
                    lineStyle = lineStyle,
                    arcStyle = arcStyle,
                    borderColor = borderColor,
                )

                drawCorner(
                    arcStartAngle = 0f,
                    arcTopLeft = Offset(left + squareSize - arcRadius * 2, top + squareSize - arcRadius * 2),
                    horLineStart = Offset(left + squareSize - arcRadius, top + squareSize),
                    horLineEnd = Offset(left + squareSize - arcRadius - lineLength, top + squareSize),
                    verLineStart = Offset(left + squareSize, top + squareSize - arcRadius),
                    verLineEnd = Offset(left + squareSize, top + squareSize - arcRadius - lineLength),
                    arcRadius = arcRadius,
                    lineStyle = lineStyle,
                    arcStyle = arcStyle,
                    borderColor = borderColor,
                )
            }

    )
}

private fun DrawScope.drawCorner(
    arcStartAngle: Float,
    arcTopLeft: Offset,
    horLineStart: Offset,
    horLineEnd: Offset,
    verLineStart: Offset,
    verLineEnd: Offset,
    arcRadius: Float,
    lineStyle: Stroke,
    arcStyle: Stroke,
    borderColor: Color,
) {
    drawArc(
        color = borderColor,
        startAngle = arcStartAngle,
        sweepAngle = 90f,
        useCenter = false,
        topLeft = arcTopLeft,
        size = Size(arcRadius * 2, arcRadius * 2),
        style = arcStyle
    )

    drawPath(
        path = Path().apply {
            moveTo(horLineStart.x, horLineStart.y)
            lineTo(horLineEnd.x, horLineEnd.y)
        },
        color = borderColor,
        style = lineStyle
    )

    drawPath(
        path = Path().apply {
            moveTo(verLineStart.x, verLineStart.y)
            lineTo(verLineEnd.x, verLineEnd.y)
        },
        color = borderColor,
        style = lineStyle
    )
}