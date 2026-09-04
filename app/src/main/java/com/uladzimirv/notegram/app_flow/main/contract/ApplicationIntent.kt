package com.uladzimirv.notegram.app_flow.main.contract

import com.uladzimirv.notegram.core.mvi.MviIntent
import com.uladzimirv.notegram.data.preferences.PreferencesRepository
import com.uladzimirv.notegram.domain.model.label.LabelId
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.ui.layout.main.com.LabelColorPref
import com.uladzimirv.notegram.ui.layout.main.com.NoteType

sealed interface ApplicationIntent : MviIntent {

    sealed interface MainScreenIntent : ApplicationIntent {
        data class OpenAddMenu(val open: Boolean) : MainScreenIntent
        data class OpenSearchBar(val open: Boolean) : MainScreenIntent
        data class SearchQuery(val query: String) : MainScreenIntent
        data class OpenNote(val id: NoteId) : MainScreenIntent
        data class Add(val noteType: NoteType) : MainScreenIntent
        data class Delete(val noteId: NoteId?) : MainScreenIntent
        data class Archive(val noteId: NoteId?) : MainScreenIntent
        data object ConfirmDelete : MainScreenIntent
        data class PinOrUnpin(val noteId: NoteId) : MainScreenIntent
        data class LockOrUnlockNote(val noteId: NoteId) : MainScreenIntent
        data class UnlockNote(val noteId: NoteId) : MainScreenIntent
        data class AccessNote(val noteId: NoteId) : MainScreenIntent

        data object CloseSheets : MainScreenIntent

        data class SelectNote(
            val noteId: NoteId,
            val itemLayoutInfo: ItemLayoutInfo
        ) : MainScreenIntent

        data object CloseSelectionMenu : MainScreenIntent
        data class OpenQRScanner(val open: Boolean) : MainScreenIntent
        data object CloseInfoDialog : MainScreenIntent
    }

    sealed interface EditNoteIntent : ApplicationIntent {
        data class SelectLabel(val labelId: LabelId) : EditNoteIntent
        data class AddLabel(val labelId: LabelId) : EditNoteIntent
        data class RemoveLabel(val labelId: LabelId) : EditNoteIntent
        data class ShowAddLabelMenu(val show: Boolean) : EditNoteIntent
        data class OpenNoteTopMenu(val open: Boolean) : EditNoteIntent
        data class Title(val title: String) : EditNoteIntent
        data class Text(val text: String) : EditNoteIntent
        data class ShowChangeColorMenu(val show: Boolean) : EditNoteIntent
        data class ChangeColor(val color: ColorPref) : EditNoteIntent
        data class EditTodo(val text: String, val todoIdemId: String? = null) : EditNoteIntent
        data class DeleteTodoItem(val id: String) : EditNoteIntent
        data class CheckTodoItem(val id: String) : EditNoteIntent
        data class Reorder(val id: String, val from: Int, val to: Int) : EditNoteIntent
    }

    sealed interface QRScannerIntent : ApplicationIntent {
        data object SaveAsTextNote : QRScannerIntent
        data object DeleteResult : QRScannerIntent

        data class QrScannerResult(
            val result: String
        ) : QRScannerIntent
    }

    sealed interface TopMenuIntent : ApplicationIntent {
        data class Show(val show: Boolean) : TopMenuIntent
        data class OpenTrashbox(val open: Boolean) : TopMenuIntent
        data class OpenArchive(val open: Boolean) : TopMenuIntent
        data class OpenSettings(val open: Boolean) : TopMenuIntent
        data class OpenLabels(val open: Boolean) : TopMenuIntent
    }

    sealed interface TrashBoxIntent : ApplicationIntent {
        data class SelectNote(
            val noteId: NoteId,
            val itemLayoutInfo: ItemLayoutInfo
        ) : TrashBoxIntent

        data object CloseSelectionMenu : TrashBoxIntent
        data class Restore(val noteId: NoteId) : TrashBoxIntent
        data class RemoveFromTrashbox(val noteId: NoteId) : TrashBoxIntent
        object ClearTrashbox : TrashBoxIntent
    }

    sealed interface ArchiveIntent : ApplicationIntent {
        data class SelectNote(
            val noteId: NoteId,
            val itemLayoutInfo: ItemLayoutInfo
        ) : ArchiveIntent

        data object CloseSelectionMenu : ArchiveIntent
        data class Restore(val noteId: NoteId) : ArchiveIntent
    }

    sealed interface SettingsIntent : ApplicationIntent {
        data class ChangeTheme(
            val themePreference: PreferencesRepository.ThemePreference
        ) : SettingsIntent

        data class ShowPinCode(
            val show: Boolean,
            val purpose: ApplicationViewState.PinCodeScreenState.PinCodePurpose
        ) : SettingsIntent
    }

    sealed interface PinCodeIntent : ApplicationIntent {
        data class SavePinCode(val pin: String) : PinCodeIntent
        data object DeletePinCode : PinCodeIntent
        data class ProtectedAccess(val pin: String) : PinCodeIntent
        data object DropAttempt : PinCodeIntent
    }

    sealed interface LabelIntent : ApplicationIntent {
        data object AddLabel : LabelIntent
        data object DropLabel : LabelIntent
        data class SelectLabel(val id: LabelId) : LabelIntent
        data class EditColorPref(val pref: LabelColorPref) : LabelIntent
        data class EditName(val value: String) : LabelIntent
        data class DeleteLabel(val id: LabelId) : LabelIntent
    }
}