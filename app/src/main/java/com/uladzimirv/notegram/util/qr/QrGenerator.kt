package com.uladzimirv.notegram.util.qr

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.util.EnumMap


fun generateQrCode(
    content: String,
    sizePx: Int,
    qrColor: Color = Color.Black,
    backgroundColor: Color = Color.White
): ImageBitmap? {
    if (content.isEmpty()) return null

    return try {
        // Configure error correction and margins
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
            put(EncodeHintType.MARGIN, 1) // Smallest margin border
        }

        // Encode content into a BitMatrix
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, sizePx, sizePx, hints)

        // Map BitMatrix colors to an Android Bitmap array
        val pixels = IntArray(sizePx * sizePx)
        val pixelColor = qrColor.toArgb()
        val bgColor = backgroundColor.toArgb()

        for (y in 0 until sizePx) {
            val offset = y * sizePx
            for (x in 0 until sizePx) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) pixelColor else bgColor
            }
        }

        // Build Bitmap and transform to Compose ImageBitmap
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, sizePx, 0, 0, sizePx, sizePx)
        }
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}