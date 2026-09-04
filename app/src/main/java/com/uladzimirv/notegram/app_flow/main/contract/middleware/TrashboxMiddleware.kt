package com.uladzimirv.notegram.app_flow.main.contract.middleware

import com.uladzimirv.notegram.app_flow.main.contract.ApplicationMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState.SelectedNoteInfo
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo

sealed interface TrashboxMiddleware : ApplicationMiddleware {
    override fun reduce(viewState: ApplicationViewState): ApplicationViewState {
        return when (this) {
            is Show -> viewState.copy(
                trashBoxState = viewState.trashBoxState.copy(show = show)
            )

            is SelectNote -> {

                val note = viewState.trashBoxState.trashBox.find { it.id == noteId }
                if (note != null) {
                    val info = SelectedNoteInfo(
                        note = note,
                        layoutInfo = itemLayoutInfo
                    )
                    viewState.copy(
                        trashBoxState = viewState.trashBoxState.copy(
                            selectedNote = info
                        )
                    )
                } else viewState
            }

            CloseSelectionMenu -> viewState.copy(
                trashBoxState = viewState.trashBoxState.copy(
                    selectedNote = null
                )
            )
        }
    }

    data class Show(
        val show: Boolean
    ) : TrashboxMiddleware

    data object CloseSelectionMenu : TrashboxMiddleware

    data class SelectNote(
        val noteId: NoteId,
        val itemLayoutInfo: ItemLayoutInfo
    ) : TrashboxMiddleware
}