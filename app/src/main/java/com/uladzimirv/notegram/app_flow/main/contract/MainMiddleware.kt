package com.uladzimirv.notegram.app_flow.main.contract

import com.uladzimirv.notegram.core.mvi.MVIMiddleware
import com.uladzimirv.notegram.data.database.entity.TodoListItem
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.domain.model.note.text.TextNote
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.util.HTTP
import com.uladzimirv.notegram.util.HTTPS
import com.uladzimirv.notegram.util.STRING_EMPTY
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import java.util.UUID

interface MainMiddleware : MVIMiddleware<MainViewState> {

    interface MainScreenMiddleware : MainMiddleware {

        override fun reduce(viewState: MainViewState): MainViewState {
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
                    viewState.copy(
                        scannerState = viewState.scannerState.copy(
                            show = addNoteRequest == NoteType.QR
                        ),
                        main = viewState.main.copy(
                            isAddMenuOpened = false
                        ),
                        noteState = viewState.noteState.copy(
                            colorMenuOpened = false,
                            show = show && addNoteRequest != NoteType.QR,
                            note = if (show) {
                                id?.let { lookForID -> viewState.main.notes.find { it.id == lookForID } }
                                    ?: when (addNoteRequest) {
                                        NoteType.TEXT -> TextNote.empty()
                                        NoteType.VOICE -> TextNote.empty()
                                        NoteType.TODO -> TodoListNote.empty()
                                        NoteType.QR,
                                        null -> TextNote.empty()
                                    }
                            } else null
                        )
                    )
                }

                is Notes -> {
                    val substate = viewState.noteState
                    val current = substate.note?.id
                    viewState.copy(
                        noteState = substate.copy(
                            show = substate.show,
                            note = notes.find { it.id == current }
                        ),
                        main = viewState.main.copy(
                            notes = notes,
                            uiNotes = notes.map {
                                it.toUIModel()
                            }.toPersistentList()
                        )
                    )
                }

                is SelectNote -> {
                    val note = viewState.main.uiNotes.find { it.id == id }
                    if (note != null) {
                        val info = MainViewState.SelectedNoteInfo(
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

                else -> viewState
            }
        }

        data class Notes(val notes: ImmutableList<Note>) : MainScreenMiddleware

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

        data class DeleteNote(val id: NoteId?) : MainScreenMiddleware

    }

    interface NoteScreen : MainMiddleware {
        override fun reduce(viewState: MainViewState): MainViewState {
            return when (this) {
                is EditNoteTitle -> {
                    when (val note = viewState.noteState.note) {
                        is TodoListNote -> {
                            viewState.copy(
                                noteState = viewState.noteState.copy(
                                    note = note.copy(
                                        title = title
                                    )
                                )
                            )
                        }

                        is TextNote -> viewState.copy(
                            noteState = viewState.noteState.copy(
                                note = note.copy(
                                    title = title
                                )
                            )
                        )

                        else -> viewState
                    }

                }

                is EditNoteText -> {
                    if (viewState.noteState.note is TextNote) {
                        val note = viewState.noteState.note
                        viewState.copy(
                            noteState = viewState.noteState.copy(
                                note = note.copy(
                                    text = text
                                )
                            )
                        )
                    } else viewState

                }

                is EditNoteColor -> {
                    when (val note = viewState.noteState.note) {
                        is TodoListNote -> {
                            viewState.copy(
                                noteState = viewState.noteState.copy(
                                    note = note.copy(
                                        colorPref = colorPref
                                    )
                                )
                            )
                        }

                        is TextNote -> viewState.copy(
                            noteState = viewState.noteState.copy(
                                note = note.copy(
                                    colorPref = colorPref
                                )
                            )
                        )

                        else -> viewState
                    }
                }

                is EditTodo -> {
                    val note = viewState.noteState.note as? TodoListNote ?: return viewState
                    val list = note.todoList.toMutableList()
                    if (todoIdemId == null) {
                        list.add(
                            TodoListItem(
                                id = UUID.randomUUID().toString(),
                                text = STRING_EMPTY,
                                position = list.size,
                                selected = false
                            )
                        )
                        val new = note.copy(
                            todoList = list.toPersistentList()
                        )
                        viewState.copy(
                            noteState = viewState.noteState.copy(
                                note = new
                            )
                        )
                    } else {
                        val item = list.find { it.id == todoIdemId } ?: return viewState
                        val newList = list.filter { it.id != todoIdemId }.toMutableList()
                        newList.add(
                            index = item.position,
                            item.copy(
                                text = text
                            )
                        )
                        viewState.copy(
                            noteState = viewState.noteState.copy(
                                note = viewState.noteState.note.copy(
                                    todoList = newList.toPersistentList()
                                )
                            )
                        )
                    }
                }

                is DeleteTodo -> {
                    val note = viewState.noteState.note as? TodoListNote ?: return viewState
                    val list = (note.todoList + note.selectedTodoList)
                        .filter { it.id != todoIdemId }
                        .mapIndexed { index, item ->
                            item.copy(
                                position = index
                            )
                        }

                    viewState.copy(
                        noteState = viewState.noteState.copy(
                            note = viewState.noteState.note.copy(
                                todoList = list.filter { !it.selected }.toPersistentList(),
                                selectedTodoList = list.filter { it.selected }.toPersistentList()
                            )
                        )
                    )
                }

                is CheckTodo -> {
                    val note = viewState.noteState.note as? TodoListNote ?: return viewState
                    val list = note.todoList + note.selectedTodoList
                    val item = list.find { it.id == todoIdemId } ?: return viewState
                    val newList = list.filter { it.id != todoIdemId }.toMutableList()
                    newList.add(
                        index = item.position,
                        item.copy(
                            selected = !item.selected
                        )
                    )
                    viewState.copy(
                        noteState = viewState.noteState.copy(
                            note = viewState.noteState.note.copy(
                                todoList = newList.filter { !it.selected }.toPersistentList(),
                                selectedTodoList = newList.filter { it.selected }.toPersistentList()
                            )
                        )
                    )
                }

                is OpenTopMenu -> {
                    viewState.copy(
                        noteState = viewState.noteState.copy(
                            topMenuOpened = open
                        )
                    )
                }

                is ReorderTodo -> {
                    val note = viewState.noteState.note as? TodoListNote ?: return viewState
                    val newList =
                        note.todoList.toMutableList().apply {
                            if (to in note.todoList.indices) {
                                add(to, removeAt(from))
                            } else return viewState
                        }
                    viewState.copy(
                        noteState = viewState.noteState.copy(
                            note = viewState.noteState.note.copy(
                                todoList = newList.mapIndexed { index, item ->
                                    item.copy(
                                        position = index
                                    )
                                }.toPersistentList()
                            )
                        )
                    )
                }

                else -> viewState
            }
        }

        data class OpenTopMenu(val open: Boolean) : NoteScreen
        data class EditNoteTitle(val title: String) : NoteScreen
        data class EditNoteText(val text: String) : NoteScreen
        data class EditNoteColor(val colorPref: ColorPref) : NoteScreen
        data class EditTodo(val text: String, val todoIdemId: String? = null) : NoteScreen
        data class DeleteTodo(val todoIdemId: String) : NoteScreen
        data class CheckTodo(val todoIdemId: String) : NoteScreen
        data class ReorderTodo(val id: String, val from: Int, val to: Int) : NoteScreen

    }

    sealed interface QRScannerMiddleware : MainMiddleware {

        override fun reduce(viewState: MainViewState): MainViewState {
            return when (this) {
                is ScannerResult -> {
                    val isLink = result.startsWith(HTTP) || result.startsWith(HTTPS)
                    viewState.copy(
                        scannerState = viewState.scannerState.copy(
                            qrScannerResult = result,
                            isResultIsLink = isLink
                        )
                    )
                }

                DeleteResult -> viewState.copy(
                    scannerState = viewState.scannerState.copy(
                        qrScannerResult = null
                    )
                )

                SaveAsTextNote -> {
                    val text = viewState.scannerState.qrScannerResult ?: return viewState
                    viewState.copy(
                        scannerState = viewState.scannerState.copy(
                            qrScannerResult = null,
                            isResultIsLink = false
                        ),
                        noteState = viewState.noteState.copy(
                            colorMenuOpened = false,
                            show = true,
                            note = TextNote.empty(text = text)
                        )
                    )
                }
            }
        }

        data class ScannerResult(
            val result: String
        ) : QRScannerMiddleware

        data object DeleteResult : QRScannerMiddleware
        data object SaveAsTextNote : QRScannerMiddleware
    }

    data object CloseSheets : MainMiddleware {
        override fun reduce(viewState: MainViewState): MainViewState {
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
                scannerState = viewState.scannerState.copy(
                    show = false,
                    qrScannerResult = null
                ),
                deleteState = viewState.deleteState.copy(
                    note = null
                )
            )
        }
    }

    data object Stub : MainMiddleware {
        override fun reduce(viewState: MainViewState) = viewState
    }

}