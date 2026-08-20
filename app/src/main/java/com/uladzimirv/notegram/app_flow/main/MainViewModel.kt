package com.uladzimirv.notegram.app_flow.main

import androidx.lifecycle.viewModelScope
import com.uladzimirv.notegram.app_flow.main.contract.MainEvent
import com.uladzimirv.notegram.app_flow.main.contract.MainIntent
import com.uladzimirv.notegram.app_flow.main.contract.MainMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.MainMiddleware.MainScreenMiddleware.ShowQR
import com.uladzimirv.notegram.app_flow.main.contract.MainMiddleware.NoteScreen.EditNoteText
import com.uladzimirv.notegram.app_flow.main.contract.MainViewState
import com.uladzimirv.notegram.core.mvi.AbstractMVIViewModel
import com.uladzimirv.notegram.domain.manager.NotesManager
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
    private val notesManager: NotesManager
) :
    AbstractMVIViewModel<MainIntent, MainViewState, MainEvent>() {

    override val viewState: StateFlow<MainViewState>

    val notesFlow = notesManager.notesFlow.map { list ->
        MainMiddleware.MainScreenMiddleware.Notes(
            list.sortedBy { it.createdAt }
                .sortedBy { !it.pinned }.toPersistentList()
        )
    }

    init {
        val stateInitial = MainViewState.initial(
            textNotes = persistentListOf()
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

                    is MainIntent.MainScreenIntent.OpenColorContainer -> {
                        emit(
                            MainMiddleware.MainScreenMiddleware.OpenColorContainer(
                                open = intent.open
                            )
                        )
                    }

                    is MainIntent.MainScreenIntent.OpenNote -> emit(
                        MainMiddleware.MainScreenMiddleware.ShowNoteBottomSheet(
                            show = true,
                            id = intent.id
                        )
                    )

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

                    is MainIntent.MainScreenIntent.ConfirmDelete -> {
                        viewState.value.deleteState.note?.id?.let { notesManager.deleteNote(id = it) }
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

        val noteEditFlow = filterIsInstance<MainIntent.EditNote>().flatMapLatest {
            flow {
                when (it) {
                    is MainIntent.EditNote.Title -> {
                        emit(
                            MainMiddleware.NoteScreen.EditNoteTitle(
                                title = it.title
                            )
                        )
                    }

                    is MainIntent.EditNote.Text -> {
                        emit(
                            EditNoteText(
                                text = it.text
                            )
                        )
                    }

                    is MainIntent.EditNote.OpenNoteTopMenu -> {
                        emit(MainMiddleware.NoteScreen.OpenTopMenu(it.open))
                    }

                    is MainIntent.EditNote.ChangeColor -> {
                        emit(
                            MainMiddleware.NoteScreen.EditNoteColor(
                                it.color
                            )
                        )
                    }

                    is MainIntent.EditNote.EditTodo -> {
                        emit(
                            MainMiddleware.NoteScreen.EditTodo(
                                text = it.text,
                                todoIdemId = it.todoIdemId
                            )
                        )
                    }

                    is MainIntent.EditNote.DeleteTodoItem -> {
                        emit(
                            MainMiddleware.NoteScreen.DeleteTodo(
                                todoIdemId = it.id
                            )
                        )
                    }

                    is MainIntent.EditNote.CheckTodoItem -> {
                        emit(
                            MainMiddleware.NoteScreen.CheckTodo(
                                todoIdemId = it.id
                            )
                        )
                    }

                    is MainIntent.EditNote.Reorder -> {
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
        return merge(
            mainFlow,
            noteEditFlow,
            scannerFlow,
            notesFlow
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