package com.uladzimirv.notegram.app_flow.main.contract

import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState.*
import com.uladzimirv.notegram.core.mvi.MVIMiddleware
import com.uladzimirv.notegram.util.STRING_EMPTY

interface ApplicationMiddleware : MVIMiddleware<ApplicationViewState> {

    data object CloseSheets : ApplicationMiddleware {
        override fun reduce(viewState: ApplicationViewState): ApplicationViewState {
            return viewState.copy(
                main = viewState.main.copy(
                    selectedNote = null,
                    query = STRING_EMPTY,
                    isAddMenuOpened = false,
                    isSearchBarActive = false
                ),
                noteState = viewState.noteState.copy(
                    show = false,
                    note = null
                ),
                labelsState = viewState.labelsState.copy(
                    label = null
                ),
                scannerState = viewState.scannerState.copy(
                    show = false,
                    qrScannerResult = null
                ),
                deleteState = viewState.deleteState.copy(
                    note = null
                ),
                topMenuState = viewState.topMenuState.copy(
                    show = false
                )
            )
        }
    }


    data class InfoDialog(
        val purpose: InfoDialogState.InfoDialogPurpose = InfoDialogState.InfoDialogPurpose.NONE,
        val show: Boolean
    ) : ApplicationMiddleware {
        override fun reduce(viewState: ApplicationViewState): ApplicationViewState {
            return viewState.copy(
                infoDialogState = viewState.infoDialogState.copy(
                    purpose = purpose,
                    show = show
                )
            )
        }
    }

    data object Stub : ApplicationMiddleware {
        override fun reduce(viewState: ApplicationViewState) = viewState
    }

}