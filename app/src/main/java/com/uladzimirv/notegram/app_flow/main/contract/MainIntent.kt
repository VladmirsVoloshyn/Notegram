package com.uladzimirv.notegram.app_flow.main.contract

import com.uladzimirv.notegram.core.mvi.MviIntent
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.ui.layout.main.com.NoteType

sealed interface MainIntent : MviIntent {

    //MainScreen
    interface MainScreenIntent : MainIntent {
        data class OpenAddMenu(val open: Boolean) : MainScreenIntent
        data class OpenSearchBar(val open: Boolean) : MainScreenIntent
        data class SearchQuery(val query: String) : MainScreenIntent
        data class OpenNote(val id: NoteId) : MainScreenIntent
        data class Add(val noteType: NoteType) : MainScreenIntent
        data class Delete(val noteId: NoteId) : MainScreenIntent
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

    interface EditNote : MainIntent {
        data class Title(val title: String) : EditNote
        data class Text(val text: String) : EditNote
        data class ChangeColor(val color: ColorPref) : EditNote
        data class EditTodo(val text: String, val todoIdemId: String? = null) : EditNote
        data class DeleteTodoItem(val id: String) : EditNote
        data class CheckTodoItem(val id: String) : EditNote
        data class Reorder(val id: String, val from: Int, val to: Int) : EditNote
    }

    interface QRScannerIntent : MainIntent {
        data object SaveAsTextNote : QRScannerIntent
        data object DeleteResult : QRScannerIntent

        data class QrScannerResult(
            val result: String
        ) : QRScannerIntent
    }
}