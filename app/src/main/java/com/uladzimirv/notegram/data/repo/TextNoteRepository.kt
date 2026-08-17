package com.uladzimirv.notegram.data.repo

import com.uladzimirv.notegram.data.database.dao.TextNoteDao
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.domain.model.note.TextNote
import com.uladzimirv.notegram.domain.model.note.TextNote.Companion.fromEntity
import com.uladzimirv.notegram.domain.model.note.TextNote.Companion.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextNoteRepository @Inject constructor(
    private val textNoteDao: TextNoteDao
) {
    val notesFlow = textNoteDao.getAllTextNotesAsFlow().map {
        it.map { it.fromEntity() }
    }

    val scope = CoroutineScope(Dispatchers.IO)

    init {
       // mockedAdd()
    }

    fun mockedAdd() {
        notes.forEach {
            addNote(it)
        }
    }

    fun addNote(note: TextNote) {
        scope.launch {
            textNoteDao.insertNote(
                note = note.toEntity()
            )
        }
    }

    fun pinOrUnpinNote(note: TextNote) {
        scope.launch {
            val new = note.copy(
                pinned = !note.pinned
            )
            textNoteDao.insertNote(new.toEntity())
        }

    }

    fun deleteNote(id: NoteId) {
        scope.launch {
            textNoteDao.deleteById(
                itemId = id
            )
        }
    }
}