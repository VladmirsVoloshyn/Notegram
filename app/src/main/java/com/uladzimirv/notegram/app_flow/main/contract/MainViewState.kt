package com.uladzimirv.notegram.app_flow.main.contract

import com.uladzimirv.notegram.core.mvi.MviViewState
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.domain.model.note.text.TextNote
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.ui.model.NoteUI
import com.uladzimirv.notegram.util.STRING_EMPTY
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

data class MainViewState(
    val main: MainScreenSubstate,
    val noteState: NoteSubState = NoteSubState(),
    val scannerState: QRScannerState = QRScannerState(),
    val deleteState: DeleteState = DeleteState(),
    val topMenuState: TopMenuState = TopMenuState(),
    val trashBoxState: TrashBoxState = TrashBoxState()
) : MviViewState {

    data class MainScreenSubstate(
        val notes: ImmutableList<Note> = persistentListOf(),
        val uiNotes: ImmutableList<NoteUI> = persistentListOf(),
        val selectedNote: SelectedNoteInfo? = null,
        val query: String = STRING_EMPTY,
        val isAddMenuOpened: Boolean = false,
        val isSearchBarActive: Boolean = false
    )

    data class NoteSubState(
        val show: Boolean = false,
        val note: Note? = null,
        val colorMenuOpened: Boolean = false,
        val topMenuOpened: Boolean = false
    )

    data class QRScannerState(
        val show: Boolean = false,
        val qrScannerResult: String? = null,
        val isResultIsLink: Boolean = false
    )

    data class DeleteState(
        val note: Note? = null,
    )

    data class TopMenuState(
        val show: Boolean = false
    )

    data class TrashBoxState(
        val show: Boolean = false,
        val trashBox: ImmutableList<NoteUI> = persistentListOf(),
        val selectedNote: SelectedNoteInfo? = null
    )

    companion object {
        fun initial(textNotes: List<TextNote>): MainViewState {
            val list = mutableListOf<Note>()
            list.addAll(textNotes)
            return MainViewState(
                main = MainScreenSubstate(
                    notes = list.toPersistentList()
                )
            )
        }
    }

    data class SelectedNoteInfo(
        val note: NoteUI,
        val layoutInfo: ItemLayoutInfo
    )
}