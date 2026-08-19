package com.uladzimirv.notegram.ui.layout.qr_scan

import android.content.ClipData
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.Anchor
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.button.AddItem
import com.uladzimirv.notegram.ui.elements.button.SingleIconButton
import com.uladzimirv.notegram.ui.theme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.borderPrimary
import com.uladzimirv.notegram.ui.theme.textPrimary
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple
import com.uladzimirv.notegram.util.intent.openLinkInBrowser
import com.uladzimirv.notegram.util.intent.sharePlainText
import com.uladzimirv.notegram.util.qr.generateQrCode
import kotlinx.coroutines.launch

@Composable
fun QrScannerResultLayer(
    result: String,
    modifier: Modifier = Modifier,
    isResultIsLink: Boolean,
    showLayer: Boolean,
    delete: () -> Unit,
    saveAsText: () -> Unit
) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    AnimatedVisibility(
        visible = showLayer,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickableNoRipple(onClick = delete)
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp))
                    .align(Alignment.Center)
            ) {
                Column(
                    modifier = Modifier
                        .background(backgroundSecondary, RoundedCornerShape(16.dp))
                        .border(
                            width = 1.dp,
                            color = borderPrimary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.s_scan_result),
                        fontSize = 16.sp,
                        color = textPrimary
                    )
                    Gap(8)
                    QRCodeImage(
                        text = result
                    )
                    Gap(8)
                    Text(
                        text = result
                    )
                    Gap(16)
                    Column {
                        val intentErrorStrong = stringResource(R.string.s_unable_open_link)
                        Row {
                            AddItem(
                                iconResId = R.drawable.ic_text_add,
                                titleResId = R.string.s_save_as_text_note,
                                isVisible = true,
                                onClick = saveAsText
                            )
                            Anchor()
                            SingleIconButton(
                                iconRes = R.drawable.ic_copy
                            ) {
                                val clipData = ClipData.newPlainText("copy", result)
                                scope.launch {
                                    clipboard.setClipEntry(clipData.toClipEntry())
                                }
                            }
                            Gap(8)
                            val title = stringResource(R.string.s_share_qr_result)
                            SingleIconButton(
                                iconRes = R.drawable.ic_share,
                                onClick = {
                                    context.sharePlainText(
                                        title = title,
                                        text = result
                                    ) {
                                        Toast.makeText(
                                            context,
                                            intentErrorStrong,
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                    }
                                }
                            )
                        }
                        Gap(16)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (isResultIsLink) {
                                AddItem(
                                    iconResId = R.drawable.ic_link,
                                    titleResId = R.string.s_link,
                                    isVisible = true
                                ) {
                                    context.openLinkInBrowser(result) {
                                        Toast.makeText(
                                            context,
                                            intentErrorStrong,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                                Gap(8)
                            }
                            Anchor()
                            SingleIconButton(
                                iconRes = R.drawable.ic_delete,
                                onClick = delete
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QRCodeImage(
    text: String
) {
    val qrBitmap = remember(text) {
        generateQrCode(
            content = text,
            sizePx = 512,
            qrColor = Color.Black,
            backgroundColor = Color.White
        )
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (qrBitmap != null) {
            Image(
                bitmap = qrBitmap,
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.Center)
            )
        } else {
            Text(text = "Failed to generate QR Code")
        }
    }

}