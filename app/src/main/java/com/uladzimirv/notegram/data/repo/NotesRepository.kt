package com.uladzimirv.notegram.data.repo

import com.uladzimirv.notegram.domain.model.com.NoteStatus
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.util.TRASHBOX_CLEAR_DELAY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

abstract class NotesRepository<T : Note> {

    abstract val notesFlow: Flow<List<T>>

    abstract fun addNote(note: T)

    abstract fun deleteNote(noteId: NoteId)

    suspend fun scanTrashbox() {
        val notes = notesFlow.firstOrNull() ?: return
        notes.forEach {
            if (it.status is NoteStatus.Deleted) {
                if (((it.status as? NoteStatus.Deleted
                        ?: return).deletedAt + TRASHBOX_CLEAR_DELAY) < System.currentTimeMillis()
                ) {
                    deleteNote(it.id)
                }
            }
        }
    }
}