package com.uladzimirv.notegram.app_flow.main.contract.middleware

import com.uladzimirv.notegram.app_flow.main.contract.ApplicationMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState.PinCodeScreenState
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState.SelectedNoteInfo
import com.uladzimirv.notegram.domain.model.com.NoteStatus
import com.uladzimirv.notegram.domain.model.label.NoteLabel
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.domain.model.note.text.TextNote
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.util.STRING_EMPTY
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

sealed interface MainScreenMiddleware : ApplicationMiddleware {

    override fun reduce(viewState: ApplicationViewState): ApplicationViewState {
        return when (this) {
            is OpenAddMenu -> viewState.copy(
                main = viewState.main.copy(
                    isAddMenuOpened = open
                )
            )

            is SearchQuery -> viewState.copy(
                main = viewState.main.copy(
                    query = query
                )
            )

            is OpenSearchBar -> viewState.copy(
                main = viewState.main.copy(
                    isSearchBarActive = open,
                    query = STRING_EMPTY
                )
            )

            is ShowNoteBottomSheet -> {
                val note =
                    id?.let { lookForID -> viewState.main.notes.find { it.id == lookForID } }
                        ?: when (addNoteRequest) {
                            NoteType.TEXT -> TextNote.empty()
                            NoteType.VOICE -> TextNote.empty()
                            NoteType.TODO -> TodoListNote.empty()
                            NoteType.QR,
                            NoteType.LABEL,
                            null -> TextNote.empty()
                        }
                viewState.copy(
                    scannerState = viewState.scannerState.copy(
                        show = addNoteRequest == NoteType.QR
                    ),
                    main = viewState.main.copy(
                        isAddMenuOpened = false
                    ),
                    noteState = viewState.noteState.copy(
                        unaddedLabels = viewState.labelsState.labels.filter {
                            !note.labels.map { it.id }.contains(it.id)
                        }.toPersistentList(),
                        colorMenuOpened = false,
                        show = show && addNoteRequest != NoteType.QR,
                        note = if (show) {
                            note
                        } else null
                    )
                )
            }

            is Notes -> {
                val substate = viewState.noteState
                val current = substate.note?.id
                val note = notes.find { it.id == current }
                viewState.copy(
                    noteState = substate.copy(
                        show = substate.show,
                        note = note,
                        unaddedLabels = note?.let {
                            viewState.labelsState.labels.filter {
                                !note.labels.map { it.id }.contains(it.id)
                            }.toPersistentList()
                        } ?: persistentListOf(),
                    ),
                    main = viewState.main.copy(
                        notes = notes.toPersistentList(),
                        uiNotes = notes.filter { it.status is NoteStatus.None }.map {
                            it.toUIModel()
                        }.toPersistentList()
                    ),
                    trashBoxState = viewState.trashBoxState.copy(
                        trashBox = notes.filter { it.status is NoteStatus.Deleted }.map {
                            it.toUIModel()
                        }.toPersistentList()
                    ),
                    archiveState = viewState.archiveState.copy(
                        archive = notes.filter { it.status is NoteStatus.Archived }.map {
                            it.toUIModel()
                        }.toPersistentList()
                    )
                )
            }

            is SelectNote -> {
                val note = viewState.main.uiNotes.find { it.id == id }
                if (note != null) {
                    val info = SelectedNoteInfo(
                        note = note,
                        layoutInfo = itemLayoutInfo
                    )
                    viewState.copy(
                        main = viewState.main.copy(
                            selectedNote = info
                        )
                    )
                } else viewState
            }

            is CloseMenu -> viewState.copy(
                main = viewState.main.copy(
                    selectedNote = null
                )
            )

            is ShowQR -> viewState.copy(
                scannerState = viewState.scannerState.copy(
                    show = show
                )
            )

            is OpenColorContainer -> viewState.copy(
                noteState = viewState.noteState.copy(
                    colorMenuOpened = open
                )
            )

            is DeleteNote -> viewState.copy(
                deleteState = viewState.deleteState.copy(
                    note = viewState.main.notes.find { it.id == id }
                )
            )

            is CallPinCode -> {
                viewState.copy(
                    pinCodeState = viewState.pinCodeState.copy(
                        callPlace = PinCodeScreenState.PinCodeCallPlace.MAIN_UNLOCKER,
                        purpose = purpose
                    )
                )
            }

            is Labels -> viewState.copy(
                labelsState = viewState.labelsState.copy(
                    labels = labels.map {
                        it.toUIModel()
                    }.toPersistentList()
                )
            )
        }
    }

    data class Notes(val notes: List<Note>) : MainScreenMiddleware

    data class Labels(val labels: List<NoteLabel>) : MainScreenMiddleware

    data class ShowQR(
        val show: Boolean
    ) : MainScreenMiddleware

    data class OpenAddMenu(
        val open: Boolean
    ) : MainScreenMiddleware

    data class OpenColorContainer(val open: Boolean) : MainScreenMiddleware

    data class OpenSearchBar(val open: Boolean) : MainScreenMiddleware
    data class SearchQuery(val query: String) : MainScreenMiddleware

    data class ShowNoteBottomSheet(
        val show: Boolean,
        val id: NoteId? = null,
        val addNoteRequest: NoteType? = null
    ) :
        MainScreenMiddleware

    data class SelectNote(val id: NoteId, val itemLayoutInfo: ItemLayoutInfo) :
        MainScreenMiddleware

    data object CloseMenu : MainScreenMiddleware

    data class CallPinCode(val purpose: PinCodeScreenState.PinCodePurpose) :
        MainScreenMiddleware

    data class DeleteNote(val id: NoteId?) : MainScreenMiddleware

}