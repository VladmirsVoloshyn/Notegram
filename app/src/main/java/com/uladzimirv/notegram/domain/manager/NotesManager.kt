package com.uladzimirv.notegram.domain.manager

import com.uladzimirv.notegram.data.repo.TextNoteRepository
import com.uladzimirv.notegram.data.repo.TodoNoteRepository
import com.uladzimirv.notegram.domain.model.com.NoteStatus
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.domain.model.note.text.TextNote
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote
import com.uladzimirv.notegram.util.STRING_EMPTY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesManager @Inject constructor(
    private val textNotesRepository: TextNoteRepository,
    private val todoNoteRepository: TodoNoteRepository
) {
    private val filterQuery = MutableStateFlow(STRING_EMPTY)

    val scope = CoroutineScope(Dispatchers.IO)

    private val notes = combine(
        textNotesRepository.notesFlow,
        todoNoteRepository.notesFlow
    ) { textNotes, todoNotes ->
        textNotes + todoNotes
    }

    val notesFlow = combine(notes, filterQuery) { notes, query ->
        notes.filter {
            it.title.contains(query)
        }
    }.stateIn(
        scope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    fun addNote(note: Note) {
        when (note) {
            is TextNote -> textNotesRepository.addNote(note)
            is TodoListNote -> todoNoteRepository.addNote(note)
        }
    }

    fun pinOrUnpinNote(note: Note) {
        when (note) {
            is TextNote -> textNotesRepository.addNote(
                note.copy(
                    pinned = !note.pinned
                )
            )

            is TodoListNote -> todoNoteRepository.addNote(
                note.copy(
                    pinned = !note.pinned
                )
            )
        }
    }

    fun lockOrUnlockNote(note: Note) {
        when (note) {
            is TextNote -> textNotesRepository.addNote(
                note.copy(
                    locked = !note.locked
                )
            )

            is TodoListNote -> todoNoteRepository.addNote(
                note.copy(
                    locked = !note.locked
                )
            )
        }
    }

    fun unlockAll() {
        scope.launch {
            val note = notes.firstOrNull().orEmpty()
            note.forEach {
                when (it) {
                    is TextNote -> textNotesRepository.addNote(
                        it.copy(
                            locked = false
                        )
                    )

                    is TodoListNote -> todoNoteRepository.addNote(
                        it.copy(
                            locked = false
                        )
                    )
                }
            }
        }
    }

    fun query(query: String = STRING_EMPTY) {
        filterQuery.value = query
    }

    suspend fun restoreNote(id: NoteId) {
        val note = notes.firstOrNull().orEmpty().find { it.id == id }
        when (note) {
            is TextNote -> textNotesRepository.addNote(
                note.copy(
                    status = NoteStatus.None()
                )
            )

            is TodoListNote -> todoNoteRepository.addNote(
                note.copy(
                    status = NoteStatus.None()
                )
            )
        }
    }

    suspend fun clearTrashbox() = withContext(Dispatchers.IO) {
        val note = notes.firstOrNull().orEmpty().filter { it.status is NoteStatus.Deleted }
        note.forEach {
            deleteNote(it.id)
        }
    }


    fun archiveNote(id: NoteId) {
        scope.launch {
            val note = notes.firstOrNull().orEmpty().find {
                it.id == id
            }
            when (note) {
                is TextNote -> textNotesRepository.addNote(
                    note.copy(
                        status = NoteStatus.Archived(
                            archivedAt = System.currentTimeMillis()
                        )
                    )
                )

                is TodoListNote -> todoNoteRepository.addNote(
                    note.copy(
                        status = NoteStatus.Archived(
                            archivedAt = System.currentTimeMillis()
                        )
                    )
                )
            }
        }
    }

    fun deleteNote(id: NoteId) {
        textNotesRepository.deleteNote(id)
        todoNoteRepository.deleteNote(id)
    }

    suspend fun moveToTrashbox(id: NoteId) = withContext(Dispatchers.IO) {
        val note = notes.firstOrNull().orEmpty().find { it.id == id }
        when (note) {
            is TextNote -> textNotesRepository.addNote(
                note.copy(
                    status = NoteStatus.Deleted(
                        deletedAt = System.currentTimeMillis()
                    )
                )
            )

            is TodoListNote -> todoNoteRepository.addNote(
                note.copy(
                    status = NoteStatus.Deleted(
                        deletedAt = System.currentTimeMillis()
                    )
                )
            )
        }
    }

}