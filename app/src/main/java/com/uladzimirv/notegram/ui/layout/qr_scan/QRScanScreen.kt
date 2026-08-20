package com.uladzimirv.notegram.ui.layout.qr_scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.app_flow.main.contract.MainIntent
import com.uladzimirv.notegram.app_flow.main.contract.MainViewState
import com.uladzimirv.notegram.ui.elements.BaseBottomSheet
import com.uladzimirv.notegram.ui.elements.camera.CameraCover
import com.uladzimirv.notegram.ui.elements.camera.CameraXPreview
import com.uladzimirv.notegram.ui.theme.backgroundPrimary
import com.uladzimirv.notegram.util.vibration.clickVibrate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScanQrScreen(
    state: MainViewState.QRScannerState,
    intent: (MainIntent) -> Unit
) {

    val shape = remember {
        RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        )
    }

    val context = LocalContext.current

    QrScannerResultLayer(
        result = state.qrScannerResult.orEmpty(),
        showLayer = state.qrScannerResult != null,
        isResultIsLink = state.isResultIsLink,
        delete = { intent(MainIntent.QRScannerIntent.DeleteResult) },
        saveAsText = { intent(MainIntent.QRScannerIntent.SaveAsTextNote) }
    )

    BaseBottomSheet(
        showBottomSheet = state.show,
        backgroundColor = Color.Transparent,
        onDismissRequest = { intent(MainIntent.MainScreenIntent.OpenQRScanner(false)) }
    ) {
        Column(
            modifier = Modifier
                .clip(shape = shape)
                .background(backgroundPrimary, shape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CameraXPreview(
                    flashOn = false,
                    onQrCodeScanned = {
                        intent(MainIntent.QRScannerIntent.QrScannerResult(it))
                        intent(MainIntent.MainScreenIntent.OpenQRScanner(false))
                        context.clickVibrate()
                    },
                )
                CameraCover(false)
            }
        }
    }
}