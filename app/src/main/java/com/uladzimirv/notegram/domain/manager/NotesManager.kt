package com.uladzimirv.notegram.domain.manager

import androidx.room.Query
import com.uladzimirv.notegram.data.repo.TextNoteRepository
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.domain.model.note.TextNote
import com.uladzimirv.notegram.util.STRING_EMPTY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class NotesManager @Inject constructor(
    private val notesRepository: TextNoteRepository
) {

    private val filterQuery = MutableStateFlow(STRING_EMPTY)
    val textNotesFlow = combine(notesRepository.notesFlow, filterQuery) { notes, query ->
        notes.filter {
            it.text.contains(query) ||
                    it.title.contains(query)
        }
    }

    fun addNote(note: Note) {
        (note as? TextNote)?.let {
            notesRepository.addNote(note)
        }
    }

    fun pinOrUnpinNote(note: Note) {
        (note as? TextNote)?.let {
            notesRepository.pinOrUnpinNote(note)
        }

    }

    fun query(query: String = STRING_EMPTY) {
        filterQuery.value = query
    }

    fun deleteNote(id: NoteId) {
        notesRepository.deleteNote(id)
    }
}