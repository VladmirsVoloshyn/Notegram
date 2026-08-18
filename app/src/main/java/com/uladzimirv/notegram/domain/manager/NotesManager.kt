package com.uladzimirv.notegram.domain.manager

import com.uladzimirv.notegram.data.repo.TextNoteRepository
import com.uladzimirv.notegram.data.repo.TodoNoteRepository
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.domain.model.note.text.TextNote
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote
import com.uladzimirv.notegram.util.STRING_EMPTY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesManager @Inject constructor(
    private val textNotesRepository: TextNoteRepository,
    private val todoNoteRepository: TodoNoteRepository
) {

    private val filterQuery = MutableStateFlow(STRING_EMPTY)

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
    }

    fun addNote(note: Note) {
        when (note) {
            is TextNote -> textNotesRepository.addNote(note)
            is TodoListNote -> todoNoteRepository.addNote(note)
        }
    }

    //TODO: turn logic to add
    fun pinOrUnpinNote(note: Note) {
        when (note) {
            is TextNote -> textNotesRepository.pinOrUnpinNote(note)
            is TodoListNote -> todoNoteRepository.pinOrUnpinNote(note)
        }
    }

    fun query(query: String = STRING_EMPTY) {
        filterQuery.value = query
    }

    //TODO
    fun deleteNote(id: NoteId) {
        textNotesRepository.deleteNote(id)
        todoNoteRepository.deleteNote(id)
    }
}