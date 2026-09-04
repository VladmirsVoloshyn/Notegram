package com.uladzimirv.notegram.app_flow.main

import androidx.lifecycle.viewModelScope
import com.uladzimirv.notegram.app_flow.main.contract.MainEvent
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationIntent
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.app_flow.main.contract.middleware.ArchiveMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.middleware.LabelsMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.middleware.MainScreenMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.middleware.NoteScreenMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.middleware.PinCodeMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.middleware.QRScannerMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.middleware.SettingsMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.middleware.TopMenuMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.middleware.TrashboxMiddleware
import com.uladzimirv.notegram.core.mvi.AbstractMVIViewModel
import com.uladzimirv.notegram.domain.manager.LabelsManager
import com.uladzimirv.notegram.domain.manager.NotesManager
import com.uladzimirv.notegram.domain.manager.PinCodeManager
import com.uladzimirv.notegram.domain.manager.ThemeManager
import com.uladzimirv.notegram.domain.model.com.NoteStatus
import com.uladzimirv.notegram.ui.model.LabelUI.Companion.toDomainModel
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
    private val pinCodeManager: PinCodeManager,
    private val labelsManager: LabelsManager
) :
    AbstractMVIViewModel<ApplicationIntent, ApplicationViewState, MainEvent>() {

    override val viewState: StateFlow<ApplicationViewState>

    val notesFlow = notesManager.notesFlow.map { list ->
        MainScreenMiddleware.Notes(
            list.sortedBy {
                VEVO(it.createdAt)
                it.createdAt
            }
                .sortedBy { !it.pinned }.toPersistentList()
        )
    }

    val labels = labelsManager.labels.map {
        MainScreenMiddleware.Labels(
            it
        )
    }

    val themeFlow = themeManager.themeFlow.map {
        SettingsMiddleware.Theme(it)
    }

    init {
        val stateInitial = ApplicationViewState.initial(
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
    private fun SharedFlow<ApplicationIntent>.throughMiddleware(): Flow<ApplicationMiddleware> {
        val mainFlow = mainScreen()
        val scannerFlow = scanner()
        val noteEditFlow = noteEdit()
        val topMenuFlow = topMenu()
        val trashboxFlow = trashbox()
        val archiveFlow = archive()
        val settingsFlow = settings()
        val pinCodeFlow = pinCode()
        val labelsFlow = labels()
        return merge(
            mainFlow,
            labelsFlow,
            labels,
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

    private fun SharedFlow<ApplicationIntent>.scanner() =
        filterIsInstance<ApplicationIntent.QRScannerIntent>().flatMapLatest {
            flow {
                when (it) {
                    is ApplicationIntent.QRScannerIntent.QrScannerResult -> {
                        emit(QRScannerMiddleware.ScannerResult(it.result))
                    }

                    is ApplicationIntent.QRScannerIntent.DeleteResult -> {
                        emit(QRScannerMiddleware.DeleteResult)
                    }

                    is ApplicationIntent.QRScannerIntent.SaveAsTextNote -> {
                        emit(QRScannerMiddleware.SaveAsTextNote)
                        updateNote(100)
                    }
                }
            }
        }

    private fun SharedFlow<ApplicationIntent>.noteEdit() =
        filterIsInstance<ApplicationIntent.EditNoteIntent>().flatMapLatest {
            flow {
                when (it) {
                    is ApplicationIntent.EditNoteIntent.ShowAddLabelMenu -> {
                        emit(
                            NoteScreenMiddleware.OpenAddLabelMenu(
                                open = it.show
                            )
                        )
                    }

                    is ApplicationIntent.EditNoteIntent.SelectLabel -> {
                        emit(NoteScreenMiddleware.SelectLabel(it.labelId))
                    }

                    is ApplicationIntent.EditNoteIntent.RemoveLabel -> {
                        emit(NoteScreenMiddleware.RemoveLabel(it.labelId))
                    }


                    is ApplicationIntent.EditNoteIntent.AddLabel -> {
                        emit(NoteScreenMiddleware.AddLabelToNote(it.labelId))
                    }

                    is ApplicationIntent.EditNoteIntent.Title -> {
                        emit(
                            NoteScreenMiddleware.EditNoteTitle(
                                title = it.title
                            )
                        )
                    }

                    is ApplicationIntent.EditNoteIntent.Text -> {
                        emit(
                            NoteScreenMiddleware.EditNoteText(
                                text = it.text
                            )
                        )
                    }

                    is ApplicationIntent.EditNoteIntent.OpenNoteTopMenu -> {
                        emit(NoteScreenMiddleware.OpenTopMenu(it.open))
                    }

                    is ApplicationIntent.EditNoteIntent.ChangeColor -> {
                        emit(
                            NoteScreenMiddleware.EditNoteColor(
                                it.color
                            )
                        )
                    }

                    is ApplicationIntent.EditNoteIntent.EditTodo -> {
                        emit(
                            NoteScreenMiddleware.EditTodo(
                                text = it.text,
                                todoIdemId = it.todoIdemId
                            )
                        )
                    }

                    is ApplicationIntent.EditNoteIntent.DeleteTodoItem -> {
                        emit(
                            NoteScreenMiddleware.DeleteTodo(
                                todoIdemId = it.id
                            )
                        )
                    }

                    is ApplicationIntent.EditNoteIntent.CheckTodoItem -> {
                        emit(
                            NoteScreenMiddleware.CheckTodo(
                                todoIdemId = it.id
                            )
                        )
                    }

                    is ApplicationIntent.EditNoteIntent.Reorder -> {
                        emit(
                            NoteScreenMiddleware.ReorderTodo(
                                id = it.id,
                                from = it.from,
                                to = it.to
                            )
                        )
                    }
                }
                updateNote(if (it is ApplicationIntent.EditNoteIntent.AddLabel) 10 else 400)
            }
        }


    private fun SharedFlow<ApplicationIntent>.mainScreen() =
        filterIsInstance<ApplicationIntent.MainScreenIntent>().flatMapLatest { intent ->
            flow {
                when (intent) {
                    is ApplicationIntent.MainScreenIntent.OpenAddMenu -> emit(
                        MainScreenMiddleware.OpenAddMenu(
                            open = intent.open
                        )
                    )

                    is ApplicationIntent.MainScreenIntent.OpenSearchBar -> {
                        emit(
                            MainScreenMiddleware.OpenSearchBar(
                                open = intent.open
                            )
                        )
                        if (!intent.open) {
                            notesManager.query()
                        }
                    }

                    is ApplicationIntent.MainScreenIntent.SearchQuery -> {
                        emit(
                            MainScreenMiddleware.SearchQuery(
                                query = intent.query
                            )
                        )
                        delay(500.milliseconds)
                        notesManager.query(intent.query)
                    }

                    is ApplicationIntent.MainScreenIntent.CloseInfoDialog -> {
                        emit(ApplicationMiddleware.InfoDialog(show = false))
                    }

                    is ApplicationIntent.MainScreenIntent.OpenColorContainer -> {
                        emit(
                            MainScreenMiddleware.OpenColorContainer(
                                open = intent.open
                            )
                        )
                    }

                    is ApplicationIntent.MainScreenIntent.AccessNote -> {
                        emit(
                            MainScreenMiddleware.ShowNoteBottomSheet(
                                show = true,
                                id = intent.noteId
                            )
                        )
                    }

                    is ApplicationIntent.MainScreenIntent.UnlockNote -> {
                        viewState.value.main.notes.find { it.id == intent.noteId }?.let {
                            if (it.locked) {
                                notesManager.lockOrUnlockNote(it)
                            }
                        }
                    }

                    is ApplicationIntent.MainScreenIntent.LockOrUnlockNote -> {
                        if (viewState.value.settingsScreenState.hasPinCode) {
                            viewState.value.main.notes.find { it.id == intent.noteId }?.let {
                                if (!it.locked) {
                                    notesManager.lockOrUnlockNote(it)
                                } else {
                                    emit(
                                        MainScreenMiddleware.CallPinCode(
                                            purpose = ApplicationViewState.PinCodeScreenState.PinCodePurpose.Unlock(
                                                intent.noteId
                                            )
                                        )
                                    )
                                }
                            }
                        } else {
                            emit(
                                ApplicationMiddleware.InfoDialog(
                                    purpose = ApplicationViewState.InfoDialogState.InfoDialogPurpose.NO_PIN,
                                    show = true
                                )
                            )
                        }
                    }

                    is ApplicationIntent.MainScreenIntent.OpenNote -> {
                        viewState.value.main.notes.find { it.id == intent.id }?.let {
                            if (it.locked) {
                                emit(
                                    MainScreenMiddleware.CallPinCode(
                                        purpose = ApplicationViewState.PinCodeScreenState.PinCodePurpose.Access(
                                            id = intent.id
                                        )
                                    )
                                )
                            } else {
                                emit(
                                    MainScreenMiddleware.ShowNoteBottomSheet(
                                        show = true,
                                        id = intent.id
                                    )
                                )
                            }
                        }
                    }

                    is ApplicationIntent.MainScreenIntent.PinOrUnpin -> {
                        viewState.value.main.notes.find { it.id == intent.noteId }?.let {
                            notesManager.pinOrUnpinNote(it)
                        }
                    }

                    is ApplicationIntent.MainScreenIntent.Add -> {
                        emit(
                            MainScreenMiddleware.ShowNoteBottomSheet(
                                show = true,
                                addNoteRequest = intent.noteType
                            )
                        )
                    }

                    is ApplicationIntent.MainScreenIntent.CloseSheets -> {
                        emit(
                            ApplicationMiddleware.CloseSheets
                        )
                    }

                    is ApplicationIntent.MainScreenIntent.SelectNote -> emit(
                        MainScreenMiddleware.SelectNote(
                            id = intent.noteId,
                            itemLayoutInfo = intent.itemLayoutInfo
                        )
                    )

                    is ApplicationIntent.MainScreenIntent.CloseSelectionMenu -> emit(
                        MainScreenMiddleware.CloseMenu
                    )

                    is ApplicationIntent.MainScreenIntent.Delete -> {
                        emit(MainScreenMiddleware.DeleteNote(intent.noteId))
                    }

                    is ApplicationIntent.MainScreenIntent.Archive -> {
                        //TODO:
                        val noteId = viewState.value.main.selectedNote?.note?.id
                            ?: viewState.value.noteState.note?.id
                        noteId?.let {
                            notesManager.archiveNote(it)
                        }
                    }

                    is ApplicationIntent.MainScreenIntent.ConfirmDelete -> {
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

                        emit(ApplicationMiddleware.CloseSheets)
                    }

                    is ApplicationIntent.MainScreenIntent.OpenQRScanner -> {
                        emit(MainScreenMiddleware.ShowQR(show = intent.open))
                    }
                }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun SharedFlow<ApplicationIntent>.labels(): Flow<ApplicationMiddleware> =
        filterIsInstance<ApplicationIntent.LabelIntent>().flatMapLatest { intent ->
            flow {
                when (intent) {
                    is ApplicationIntent.LabelIntent.AddLabel -> emit(
                        LabelsMiddleware.SelectLabel(
                            labelId = null
                        )
                    )

                    is ApplicationIntent.LabelIntent.SelectLabel -> emit(
                        LabelsMiddleware.SelectLabel(
                            labelId = intent.id
                        )
                    )


                    is ApplicationIntent.LabelIntent.EditName -> {
                        emit(
                            LabelsMiddleware.EditName(
                                name = intent.value
                            )
                        )
                        updateLabel()
                    }

                    is ApplicationIntent.LabelIntent.DeleteLabel -> {
                        emit(LabelsMiddleware.DropLabel)
                        labelsManager.deleteLabel(intent.id)
                    }

                    is ApplicationIntent.LabelIntent.EditColorPref -> {
                        emit(
                            LabelsMiddleware.EditColorPref(
                                colorPref = intent.pref
                            )
                        )
                        updateLabel()
                    }

                    is ApplicationIntent.LabelIntent.DropLabel -> emit(LabelsMiddleware.DropLabel)
                }
            }
        }

    private fun SharedFlow<ApplicationIntent>.pinCode() =
        filterIsInstance<ApplicationIntent.PinCodeIntent>().map {
            when (it) {
                is ApplicationIntent.PinCodeIntent.SavePinCode -> {
                    pinCodeManager.setPinCode(it.pin)
                    PinCodeMiddleware.HasPinCode(true)
                }

                is ApplicationIntent.PinCodeIntent.DeletePinCode -> {
                    pinCodeManager.setPinCode(STRING_EMPTY)
                    notesManager.unlockAll()
                    PinCodeMiddleware.HasPinCode(false)
                }

                is ApplicationIntent.PinCodeIntent.ProtectedAccess -> {
                    val correct = pinCodeManager.comparePinCode(it.pin)
                    if (correct) {
                        PinCodeMiddleware.SetAttempt(ApplicationViewState.PinCodeScreenState.Attempt.SUCCESS)
                    } else {
                        PinCodeMiddleware.SetAttempt(ApplicationViewState.PinCodeScreenState.Attempt.WRONG)
                    }
                }

                is ApplicationIntent.PinCodeIntent.DropAttempt -> {
                    PinCodeMiddleware.SetAttempt(ApplicationViewState.PinCodeScreenState.Attempt.ATTEMPT)
                }
            }
        }

    private fun SharedFlow<ApplicationIntent>.settings() =
        filterIsInstance<ApplicationIntent.SettingsIntent>().map {
            when (it) {
                is ApplicationIntent.SettingsIntent.ChangeTheme -> {
                    themeManager.setTheme(it.themePreference)
                    ApplicationMiddleware.Stub
                }

                is ApplicationIntent.SettingsIntent.ShowPinCode -> SettingsMiddleware.ShowPinCode(
                    show = it.show,
                    purpose = it.purpose,
                )
            }
        }

    private fun SharedFlow<ApplicationIntent>.topMenu() =
        filterIsInstance<ApplicationIntent.TopMenuIntent>().map {
            when (it) {
                is ApplicationIntent.TopMenuIntent.Show -> TopMenuMiddleware.Show(it.show)
                is ApplicationIntent.TopMenuIntent.OpenTrashbox -> TrashboxMiddleware.Show(
                    it.open
                )

                is ApplicationIntent.TopMenuIntent.OpenArchive -> ArchiveMiddleware.Show(
                    it.open
                )

                is ApplicationIntent.TopMenuIntent.OpenSettings -> SettingsMiddleware.Show(
                    it.open
                )

                is ApplicationIntent.TopMenuIntent.OpenLabels -> LabelsMiddleware.Show(
                    it.open
                )
            }
        }

    private fun SharedFlow<ApplicationIntent>.trashbox() =
        filterIsInstance<ApplicationIntent.TrashBoxIntent>().map { intent ->
            when (intent) {
                is ApplicationIntent.TrashBoxIntent.SelectNote -> TrashboxMiddleware.SelectNote(
                    intent.noteId,
                    intent.itemLayoutInfo
                )

                ApplicationIntent.TrashBoxIntent.CloseSelectionMenu -> TrashboxMiddleware.CloseSelectionMenu

                is ApplicationIntent.TrashBoxIntent.Restore -> {
                    viewState.value.trashBoxState.selectedNote?.let {
                        notesManager.restoreNote(it.note.id)
                    }
                    ApplicationMiddleware.Stub
                }

                is ApplicationIntent.TrashBoxIntent.RemoveFromTrashbox -> {
                    MainScreenMiddleware.DeleteNote(intent.noteId)
                }

                ApplicationIntent.TrashBoxIntent.ClearTrashbox -> {
                    notesManager.clearTrashbox()
                    ApplicationMiddleware.Stub
                }
            }
        }

    private fun SharedFlow<ApplicationIntent>.archive() =
        filterIsInstance<ApplicationIntent.ArchiveIntent>().map { intent ->
            when (intent) {
                is ApplicationIntent.ArchiveIntent.SelectNote -> ArchiveMiddleware.SelectNote(
                    intent.noteId,
                    intent.itemLayoutInfo
                )

                ApplicationIntent.ArchiveIntent.CloseSelectionMenu -> ArchiveMiddleware.CloseSelectionMenu

                is ApplicationIntent.ArchiveIntent.Restore -> {
                    viewState.value.archiveState.selectedNote?.let {
                        notesManager.restoreNote(it.note.id)
                    }
                    ApplicationMiddleware.Stub
                }
            }
        }

    private suspend fun updateLabel(mills: Int = 500) {
        delay(mills.milliseconds)
        viewState.value.labelsState.label?.let {
            labelsManager.addLabel(
                it.toDomainModel()
            )
        }
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