package com.uladzimirv.notegram.app_flow.main.contract.middleware

import com.uladzimirv.notegram.app_flow.main.contract.ApplicationMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.domain.model.note.text.TextNote
import com.uladzimirv.notegram.util.HTTP
import com.uladzimirv.notegram.util.HTTPS

sealed interface QRScannerMiddleware : ApplicationMiddleware {

    override fun reduce(viewState: ApplicationViewState): ApplicationViewState {
        return when (this) {
            is ScannerResult -> {
                val isLink = result.startsWith(HTTP) || result.startsWith(HTTPS)
                viewState.copy(
                    scannerState = viewState.scannerState.copy(
                        qrScannerResult = result,
                        isResultIsLink = isLink
                    )
                )
            }

            DeleteResult -> viewState.copy(
                scannerState = viewState.scannerState.copy(
                    qrScannerResult = null
                )
            )

            SaveAsTextNote -> {
                val text = viewState.scannerState.qrScannerResult ?: return viewState
                viewState.copy(
                    scannerState = viewState.scannerState.copy(
                        qrScannerResult = null,
                        isResultIsLink = false
                    ),
                    noteState = viewState.noteState.copy(
                        colorMenuOpened = false,
                        show = true,
                        note = TextNote.empty(text = text)
                    )
                )
            }
        }
    }

    data class ScannerResult(
        val result: String
    ) : QRScannerMiddleware

    data object DeleteResult : QRScannerMiddleware
    data object SaveAsTextNote : QRScannerMiddleware
}