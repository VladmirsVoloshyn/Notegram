package com.uladzimirv.notegram.data.repo

import com.uladzimirv.notegram.data.database.dao.TodoNoteDao
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote.Companion.fromEntity
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote.Companion.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoNoteRepository @Inject constructor(
    private val todoNoteDao: TodoNoteDao
) {
    val notesFlow = todoNoteDao.getAllTextNotesAsFlow().map {
        it.map { it.fromEntity() }
    }

    val scope = CoroutineScope(Dispatchers.IO)

    init {
        //mockedAdd()
    }

    fun mockedAdd() {
        todoNotes.forEach {
            addNote(it)
        }
    }

    fun addNote(note: TodoListNote) {
        scope.launch {
            todoNoteDao.insertNote(
                note = note.toEntity()
            )
        }
    }

    fun pinOrUnpinNote(note: TodoListNote) {
        scope.launch {
            val new = note.copy(
                pinned = !note.pinned
            )
            todoNoteDao.insertNote(new.toEntity())
        }
    }

    fun deleteNote(id: NoteId) {
        scope.launch {
            todoNoteDao.deleteById(
                itemId = id
            )
        }
    }


}