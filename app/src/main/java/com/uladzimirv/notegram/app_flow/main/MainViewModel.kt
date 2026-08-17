package com.uladzimirv.notegram.app_flow.main

import androidx.lifecycle.viewModelScope
import com.uladzimirv.notegram.app_flow.main.contract.MainEvent
import com.uladzimirv.notegram.app_flow.main.contract.MainIntent
import com.uladzimirv.notegram.app_flow.main.contract.MainMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.MainMiddleware.NoteScreen.EditNoteText
import com.uladzimirv.notegram.app_flow.main.contract.MainViewState
import com.uladzimirv.notegram.core.mvi.AbstractMVIViewModel
import com.uladzimirv.notegram.domain.manager.NotesManager
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
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

    val fl = notesManager.textNotesFlow.map {
        val list = mutableListOf<Note>()
        list.addAll(it)
        MainMiddleware.MainScreenMiddleware.Notes(list.sortedBy { it.createdAt }
            .sortedBy { !it.pinned }.toPersistentList())
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
                        MainMiddleware.MainScreenMiddleware.ShowTextNoteBottomSheet(
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
                        when (intent.noteType) {
                            NoteType.TEXT -> {
                                emit(
                                    MainMiddleware.MainScreenMiddleware.ShowTextNoteBottomSheet(
                                        show = true
                                    )
                                )
                            }

                            NoteType.VOICE,
                            NoteType.QR,
                            NoteType.TODO -> {
                            }
                        }
                    }

                    is MainIntent.MainScreenIntent.CloseSheets -> emit(
                        MainMiddleware.MainScreenMiddleware.ShowTextNoteBottomSheet(
                            show = false
                        )
                    )

                    is MainIntent.MainScreenIntent.SelectNote -> emit(
                        MainMiddleware.MainScreenMiddleware.SelectNote(
                            id = intent.noteId,
                            itemLayoutInfo = intent.itemLayoutInfo
                        )
                    )

                    is MainIntent.MainScreenIntent.CloseSelectionMenu -> emit(MainMiddleware.MainScreenMiddleware.CloseMenu)

                    is MainIntent.MainScreenIntent.Delete -> {
                        notesManager.deleteNote(intent.noteId)
                    }
                }
            }

        }

        val textNoteFlow = filterIsInstance<MainIntent.EditNote>().flatMapLatest {
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

                    is MainIntent.EditNote.ChangeColor -> {
                        emit(
                            MainMiddleware.NoteScreen.EditNoteColor(
                                it.color
                            )
                        )
                    }
                }
                updateNote()
            }
        }
        return merge(
            mainFlow,
            textNoteFlow,
            fl
        )
    }

    private suspend fun updateNote(mills: Int = 500) {
        delay(mills.milliseconds)
        viewState.value.note.note?.let {
            notesManager.addNote(
                it
            )
        }

    }


}