package com.uladzimirv.notegram.app_flow.main

import androidx.lifecycle.viewModelScope
import com.uladzimirv.notegram.app_flow.main.contract.MainEvent
import com.uladzimirv.notegram.app_flow.main.contract.MainIntent
import com.uladzimirv.notegram.app_flow.main.contract.MainMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.MainMiddleware.MainScreenMiddleware.CallPinCode
import com.uladzimirv.notegram.app_flow.main.contract.MainMiddleware.MainScreenMiddleware.ShowQR
import com.uladzimirv.notegram.app_flow.main.contract.MainMiddleware.NoteScreen.EditNoteText
import com.uladzimirv.notegram.app_flow.main.contract.MainViewState
import com.uladzimirv.notegram.core.mvi.AbstractMVIViewModel
import com.uladzimirv.notegram.domain.manager.NotesManager
import com.uladzimirv.notegram.domain.manager.PinCodeManager
import com.uladzimirv.notegram.domain.manager.ThemeManager
import com.uladzimirv.notegram.domain.model.com.NoteStatus
import com.uladzimirv.notegram.util.STRING_EMPTY
import com.uladzimirv.notegram.util.VEVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class MainViewModel @Inject constructor(
    private val notesManager: NotesManager,
    private val themeManager: ThemeManager,
    private val pinCodeManager: PinCodeManager
) :
    AbstractMVIViewModel<MainIntent, MainViewState, MainEvent>() {

    override val viewState: StateFlow<MainViewState>

    val notesFlow = notesManager.notesFlow.map { list ->
        MainMiddleware.MainScreenMiddleware.Notes(
            list.sortedBy { it.createdAt }
                .sortedBy { !it.pinned }.toPersistentList()
        )
    }

    val themeFlow = themeManager.themeFlow.map {
        MainMiddleware.Setting.Theme(it)
    }

    init {
        val stateInitial = MainViewState.initial(
            textNotes = persistentListOf(),
            hasPinCode = pinCodeManager.hasPinCode()
        )
        viewState = intentSharedFlow.throughMiddleware()
            .scan(stateInitial) { state, change -> change.reduce(state) }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                stateInitial
            )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun SharedFlow<MainIntent>.throughMiddleware(): Flow<MainMiddleware> {
        val mainFlow = filterIsInstance<MainIntent.MainScreenIntent>().flatMapLatest { intent ->
            flow {
                when (intent) {
                    is MainIntent.MainScreenIntent.OpenAddMenu -> emit(
                        MainMiddleware.MainScreenMiddleware.OpenAddMenu(
                            open = intent.open
                        )
                    )

                    is MainIntent.MainScreenIntent.OpenSearchBar -> {
                        emit(
                            MainMiddleware.MainScreenMiddleware.OpenSearchBar(
                                open = intent.open
                            )
                        )
                        if (!intent.open) {
                            notesManager.query()
                        }
                    }

                    is MainIntent.MainScreenIntent.SearchQuery -> {
                        emit(
                            MainMiddleware.MainScreenMiddleware.SearchQuery(
                                query = intent.query
                            )
                        )
                        delay(500.milliseconds)
                        notesManager.query(intent.query)
                    }

                    is MainIntent.MainScreenIntent.CloseInfoDialog -> {
                        emit(MainMiddleware.InfoDialog(show = false))
                    }

                    is MainIntent.MainScreenIntent.OpenColorContainer -> {
                        emit(
                            MainMiddleware.MainScreenMiddleware.OpenColorContainer(
                                open = intent.open
                            )
                        )
                    }

                    is MainIntent.MainScreenIntent.AccessNote -> {
                        emit(
                            MainMiddleware.MainScreenMiddleware.ShowNoteBottomSheet(
                                show = true,
                                id = intent.noteId
                            )
                        )
                    }

                    is MainIntent.MainScreenIntent.UnlockNote -> {
                        viewState.value.main.notes.find { it.id == intent.noteId }?.let {
                            if (it.locked) {
                                notesManager.lockOrUnlockNote(it)
                            }
                        }
                    }

                    is MainIntent.MainScreenIntent.LockOrUnlockNote -> {
                        if (viewState.value.settingsScreenState.hasPinCode) {
                            viewState.value.main.notes.find { it.id == intent.noteId }?.let {
                                if (!it.locked) {
                                    notesManager.lockOrUnlockNote(it)
                                } else {
                                    emit(
                                        CallPinCode(
                                            purpose = MainViewState.PinCodeScreenState.PinCodePurpose.Unlock(
                                                intent.noteId
                                            )
                                        )
                                    )
                                }
                            }
                        } else {
                            emit(
                                MainMiddleware.InfoDialog(
                                    purpose = MainViewState.InfoDialogState.InfoDialogPurpose.NO_PIN,
                                    show = true
                                )
                            )
                        }
                    }

                    is MainIntent.MainScreenIntent.OpenNote -> {
                        viewState.value.main.notes.find { it.id == intent.id }?.let {
                            if (it.locked) {
                                emit(
                                    CallPinCode(
                                        purpose = MainViewState.PinCodeScreenState.PinCodePurpose.Access(
                                            id = intent.id
                                        )
                                    )
                                )
                            } else {
                                emit(
                                    MainMiddleware.MainScreenMiddleware.ShowNoteBottomSheet(
                                        show = true,
                                        id = intent.id
                                    )
                                )
                            }
                        }
                    }

                    is MainIntent.MainScreenIntent.PinOrUnpin -> {
                        viewState.value.main.notes.find { it.id == intent.noteId }?.let {
                            notesManager.pinOrUnpinNote(it)
                        }
                    }

                    is MainIntent.MainScreenIntent.Add -> {
                        emit(
                            MainMiddleware.MainScreenMiddleware.ShowNoteBottomSheet(
                                show = true,
                                addNoteRequest = intent.noteType
                            )
                        )
                    }

                    is MainIntent.MainScreenIntent.CloseSheets -> {
                        emit(
                            MainMiddleware.CloseSheets
                        )
                    }

                    is MainIntent.MainScreenIntent.SelectNote -> emit(
                        MainMiddleware.MainScreenMiddleware.SelectNote(
                            id = intent.noteId,
                            itemLayoutInfo = intent.itemLayoutInfo
                        )
                    )

                    is MainIntent.MainScreenIntent.CloseSelectionMenu -> emit(
                        MainMiddleware.MainScreenMiddleware.CloseMenu
                    )

                    is MainIntent.MainScreenIntent.Delete -> {
                        emit(MainMiddleware.MainScreenMiddleware.DeleteNote(intent.noteId))
                    }

                    is MainIntent.MainScreenIntent.Archive -> {
                        //TODO:
                        val noteId = viewState.value.main.selectedNote?.note?.id
                            ?: viewState.value.noteState.note?.id
                        noteId?.let {
                            notesManager.archiveNote(it)
                        }
                    }

                    is MainIntent.MainScreenIntent.ConfirmDelete -> {
                        val note = viewState.value.deleteState.note
                        if (note?.status is NoteStatus.Deleted) {
                            viewState.value.deleteState.note?.id?.let {
                                notesManager.deleteNote(id = it)
                            }
                        } else {
                            viewState.value.deleteState.note?.id?.let {
                                notesManager.moveToTrashbox(
                                    id = it
                                )
                            }
                        }

                        emit(MainMiddleware.CloseSheets)
                    }

                    is MainIntent.MainScreenIntent.OpenQRScanner -> {
                        emit(ShowQR(show = intent.open))
                    }
                }
            }
        }

        val scannerFlow = filterIsInstance<MainIntent.QRScannerIntent>().flatMapLatest {
            flow {
                when (it) {
                    is MainIntent.QRScannerIntent.QrScannerResult -> {
                        emit(MainMiddleware.QRScannerMiddleware.ScannerResult(it.result))
                    }

                    is MainIntent.QRScannerIntent.DeleteResult -> {
                        emit(MainMiddleware.QRScannerMiddleware.DeleteResult)
                    }

                    is MainIntent.QRScannerIntent.SaveAsTextNote -> {
                        emit(MainMiddleware.QRScannerMiddleware.SaveAsTextNote)
                        updateNote(100)
                    }
                }
            }
        }

        val noteEditFlow = filterIsInstance<MainIntent.EditNoteIntent>().flatMapLatest {
            flow {
                when (it) {
                    is MainIntent.EditNoteIntent.Title -> {
                        emit(
                            MainMiddleware.NoteScreen.EditNoteTitle(
                                title = it.title
                            )
                        )
                    }

                    is MainIntent.EditNoteIntent.Text -> {
                        emit(
                            EditNoteText(
                                text = it.text
                            )
                        )
                    }

                    is MainIntent.EditNoteIntent.OpenNoteTopMenu -> {
                        emit(MainMiddleware.NoteScreen.OpenTopMenu(it.open))
                    }

                    is MainIntent.EditNoteIntent.ChangeColor -> {
                        emit(
                            MainMiddleware.NoteScreen.EditNoteColor(
                                it.color
                            )
                        )
                    }

                    is MainIntent.EditNoteIntent.EditTodo -> {
                        emit(
                            MainMiddleware.NoteScreen.EditTodo(
                                text = it.text,
                                todoIdemId = it.todoIdemId
                            )
                        )
                    }

                    is MainIntent.EditNoteIntent.DeleteTodoItem -> {
                        emit(
                            MainMiddleware.NoteScreen.DeleteTodo(
                                todoIdemId = it.id
                            )
                        )
                    }

                    is MainIntent.EditNoteIntent.CheckTodoItem -> {
                        emit(
                            MainMiddleware.NoteScreen.CheckTodo(
                                todoIdemId = it.id
                            )
                        )
                    }

                    is MainIntent.EditNoteIntent.Reorder -> {
                        emit(
                            MainMiddleware.NoteScreen.ReorderTodo(
                                id = it.id,
                                from = it.from,
                                to = it.to
                            )
                        )
                    }
                }
                updateNote()
            }
        }

        val topMenuFlow = filterIsInstance<MainIntent.TopMenuIntent>().map {
            when (it) {
                is MainIntent.TopMenuIntent.Show -> MainMiddleware.TopMenu.Show(it.show)
                is MainIntent.TopMenuIntent.OpenTrashbox -> MainMiddleware.Trashbox.Show(it.open)
                is MainIntent.TopMenuIntent.OpenArchive -> MainMiddleware.Archive.Show(it.open)
                is MainIntent.TopMenuIntent.OpenSettings -> MainMiddleware.Setting.Show(it.open)
                else -> MainMiddleware.Stub
            }
        }

        val trashboxFlow = filterIsInstance<MainIntent.TrashBoxIntent>().map { intent ->
            when (intent) {
                is MainIntent.TrashBoxIntent.SelectNote -> MainMiddleware.Trashbox.SelectNote(
                    intent.noteId,
                    intent.itemLayoutInfo
                )

                MainIntent.TrashBoxIntent.CloseSelectionMenu -> MainMiddleware.Trashbox.CloseSelectionMenu

                is MainIntent.TrashBoxIntent.Restore -> {
                    viewState.value.trashBoxState.selectedNote?.let {
                        notesManager.restoreNote(it.note.id)
                    }
                    MainMiddleware.Stub
                }

                is MainIntent.TrashBoxIntent.RemoveFromTrashbox -> {
                    MainMiddleware.MainScreenMiddleware.DeleteNote(intent.noteId)
                }

                MainIntent.TrashBoxIntent.ClearTrashbox -> {
                    notesManager.clearTrashbox()
                    MainMiddleware.Stub
                }

                else -> MainMiddleware.Stub
            }
        }

        val archiveFlow = filterIsInstance<MainIntent.ArchiveIntent>().map { intent ->
            when (intent) {
                is MainIntent.ArchiveIntent.SelectNote -> MainMiddleware.Archive.SelectNote(
                    intent.noteId,
                    intent.itemLayoutInfo
                )

                MainIntent.ArchiveIntent.CloseSelectionMenu -> MainMiddleware.Archive.CloseSelectionMenu

                is MainIntent.ArchiveIntent.Restore -> {
                    viewState.value.archiveState.selectedNote?.let {
                        notesManager.restoreNote(it.note.id)
                    }
                    MainMiddleware.Stub
                }

                else -> MainMiddleware.Stub
            }
        }

        val settingsFlow = filterIsInstance<MainIntent.SettingsIntent>().map {
            return@map when (it) {
                is MainIntent.SettingsIntent.ChangeTheme -> {
                    themeManager.setTheme(it.themePreference)
                    MainMiddleware.Stub
                }

                is MainIntent.SettingsIntent.ShowPinCode -> MainMiddleware.Setting.ShowPinCode(
                    show = it.show,
                    purpose = it.purpose,
                )

                else -> MainMiddleware.Stub
            }
        }

        val pinCodeFlow = filterIsInstance<MainIntent.PinCodeIntent>().map {
            when (it) {
                is MainIntent.PinCodeIntent.SavePinCode -> {
                    pinCodeManager.setPinCode(it.pin)
                    MainMiddleware.PinCode.HasPinCode(true)
                }

                is MainIntent.PinCodeIntent.DeletePinCode -> {
                    pinCodeManager.setPinCode(STRING_EMPTY)
                    notesManager.unlockAll()
                    MainMiddleware.PinCode.HasPinCode(false)
                }

                is MainIntent.PinCodeIntent.ProtectedAccess -> {
                    val correct = pinCodeManager.comparePinCode(it.pin)
                    if (correct) {
                        MainMiddleware.PinCode.SetAttempt(MainViewState.PinCodeScreenState.Attempt.SUCCESS)
                    } else {
                        MainMiddleware.PinCode.SetAttempt(MainViewState.PinCodeScreenState.Attempt.WRONG)
                    }
                }

                is MainIntent.PinCodeIntent.DropAttempt -> {
                    MainMiddleware.PinCode.SetAttempt(MainViewState.PinCodeScreenState.Attempt.ATTEMPT)
                }

                else -> MainMiddleware.Stub
            }
        }

        return merge(
            mainFlow,
            noteEditFlow,
            scannerFlow,
            notesFlow,
            topMenuFlow,
            trashboxFlow,
            archiveFlow,
            settingsFlow,
            themeFlow,
            pinCodeFlow
        )
    }

    private suspend fun updateNote(mills: Int = 500) {
        delay(mills.milliseconds)
        viewState.value.noteState.note?.let {
            notesManager.addNote(
                it
            )
        }

    }


}