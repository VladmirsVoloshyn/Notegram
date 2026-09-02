package com.uladzimirv.notegram.app_flow.main.contract

import com.uladzimirv.notegram.core.mvi.MviViewState
import com.uladzimirv.notegram.data.preferences.PreferencesRepository
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
    val trashBoxState: TrashBoxState = TrashBoxState(),
    val archiveState: ArchiveScreenState = ArchiveScreenState(),
    val settingsScreenState: SettingsScreenState = SettingsScreenState(),
    val pinCodeState: PinCodeScreenState = PinCodeScreenState(),
    val infoDialogState: InfoDialogState = InfoDialogState()
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

    data class ArchiveScreenState(
        val show: Boolean = false,
        val archive: ImmutableList<NoteUI> = persistentListOf(),
        val selectedNote: SelectedNoteInfo? = null
    )

    data class SettingsScreenState(
        val show: Boolean = false,
        val theme: PreferencesRepository.ThemePreference = PreferencesRepository.ThemePreference.SYSTEM,
        val hasPinCode: Boolean = false
    )

    data class PinCodeScreenState(
        val callPlace: PinCodeCallPlace = PinCodeCallPlace.NONE,
        val attempt: Attempt = Attempt.ATTEMPT,
        val purpose: PinCodePurpose = PinCodePurpose.CreateNew(false)
    ) {

        enum class PinCodeCallPlace {
            SETTINGS,
            MAIN_UNLOCKER,
            NONE
        }

        enum class Attempt {
            ATTEMPT,
            WRONG,
            SUCCESS
        }

        sealed class PinCodePurpose {
            data class CreateNew(val hasPinCode: Boolean) : PinCodePurpose()
            data object DeletePinCode : PinCodePurpose()
            data class Unlock(val id: NoteId) : PinCodePurpose()
            data class Access(val id: NoteId) : PinCodePurpose()
            data object Close : PinCodePurpose()
        }

    }

    data class InfoDialogState(
        val purpose: InfoDialogPurpose = InfoDialogPurpose.NONE,
        val show: Boolean = false
    ) {
        enum class InfoDialogPurpose {
            NO_PIN,
            NONE
        }
    }

    companion object {
        fun initial(textNotes: List<TextNote>, hasPinCode: Boolean): MainViewState {
            val list = mutableListOf<Note>()
            list.addAll(textNotes)
            return MainViewState(
                main = MainScreenSubstate(
                    notes = list.toPersistentList()
                ),
                settingsScreenState = SettingsScreenState(
                    hasPinCode = hasPinCode
                )
            )
        }
    }

    data class SelectedNoteInfo(
        val note: NoteUI,
        val layoutInfo: ItemLayoutInfo
    )
}