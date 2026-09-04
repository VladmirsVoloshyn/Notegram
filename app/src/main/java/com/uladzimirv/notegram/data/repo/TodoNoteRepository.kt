package com.uladzimirv.notegram.data.repo

import com.uladzimirv.notegram.data.database.dao.LabelsDao
import com.uladzimirv.notegram.data.database.dao.TodoNoteDao
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote.Companion.fromEntity
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote.Companion.toEntity
import com.uladzimirv.notegram.util.VEVO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class TodoNoteRepository @Inject constructor(
    private val todoNoteDao: TodoNoteDao,
    labelsDao: LabelsDao
) : NotesRepository<TodoListNote>() {


    val scope = CoroutineScope(Dispatchers.IO)

    override val notesFlow =
        combine(
            todoNoteDao.getAllTextNotesAsFlow(),
            labelsDao.getAllLabelsAsFlow()
        ) { notes, labels ->
            notes.map { it.fromEntity(labels.toSet()) }
        }.flowOn(Dispatchers.IO)

    init {
        scope.launch { scanTrashbox() }
    }

    override fun addNote(note: TodoListNote) {
        scope.launch {
            todoNoteDao.insertNote(
                note = note.toEntity()
            )
        }
    }

    override fun deleteNote(noteId: NoteId) {
        scope.launch {
            todoNoteDao.deleteById(
                itemId = noteId
            )
        }
    }


}