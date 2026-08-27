package com.uladzimirv.notegram.app_flow.main.contract

import com.uladzimirv.notegram.core.mvi.MviIntent
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.ui.layout.main.com.NoteType

sealed interface MainIntent : MviIntent {

    interface MainScreenIntent : MainIntent {
        data class OpenAddMenu(val open: Boolean) : MainScreenIntent
        data class OpenSearchBar(val open: Boolean) : MainScreenIntent
        data class SearchQuery(val query: String) : MainScreenIntent
        data class OpenNote(val id: NoteId) : MainScreenIntent
        data class Add(val noteType: NoteType) : MainScreenIntent
        data class Delete(val noteId: NoteId?) : MainScreenIntent
        data object ConfirmDelete : MainScreenIntent
        data class PinOrUnpin(val noteId: NoteId) : MainScreenIntent
        data object CloseSheets : MainScreenIntent
        data class SelectNote(
            val noteId: NoteId,
            val itemLayoutInfo: ItemLayoutInfo
        ) : MainScreenIntent

        data object CloseSelectionMenu : MainScreenIntent

        data class OpenColorContainer(val open: Boolean) : MainScreenIntent
        data class OpenQRScanner(val open: Boolean) : MainScreenIntent
    }

    interface EditNoteIntent : MainIntent {
        data class OpenNoteTopMenu(val open: Boolean) : EditNoteIntent
        data class Title(val title: String) : EditNoteIntent
        data class Text(val text: String) : EditNoteIntent
        data class ChangeColor(val color: ColorPref) : EditNoteIntent
        data class EditTodo(val text: String, val todoIdemId: String? = null) : EditNoteIntent
        data class DeleteTodoItem(val id: String) : EditNoteIntent
        data class CheckTodoItem(val id: String) : EditNoteIntent
        data class Reorder(val id: String, val from: Int, val to: Int) : EditNoteIntent
    }

    interface QRScannerIntent : MainIntent {
        data object SaveAsTextNote : QRScannerIntent
        data object DeleteResult : QRScannerIntent

        data class QrScannerResult(
            val result: String
        ) : QRScannerIntent
    }

    interface TopMenuIntent : MainIntent {
        data class Show(val show: Boolean) : TopMenuIntent
        data class OpenTrashbox(val open: Boolean) : TopMenuIntent

    }

    interface TrashBoxIntent : MainIntent {
        data class SelectNote(
            val noteId: NoteId,
            val itemLayoutInfo: ItemLayoutInfo
        ) : TrashBoxIntent

        data object CloseSelectionMenu : TrashBoxIntent

        data class Restore(val noteId: NoteId) : TrashBoxIntent
        data class RemoveFromTrashbox(val noteId: NoteId) : TrashBoxIntent
        object ClearTrashbox : TrashBoxIntent
    }
}