package com.uladzimirv.notegram.data.repo

import com.uladzimirv.notegram.data.database.dao.LabelsDao
import com.uladzimirv.notegram.data.database.dao.TextNoteDao
import com.uladzimirv.notegram.domain.model.com.NoteStatus
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.domain.model.note.text.TextNote
import com.uladzimirv.notegram.domain.model.note.text.TextNote.Companion.fromEntity
import com.uladzimirv.notegram.domain.model.note.text.TextNote.Companion.toEntity
import com.uladzimirv.notegram.util.VEVO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextNoteRepository @Inject constructor(
    private val textNoteDao: TextNoteDao,
    labelsDao: LabelsDao
) : NotesRepository<TextNote>() {

    override val notesFlow =
        combine(
            textNoteDao.getAllTextNotesAsFlow(),
            labelsDao.getAllLabelsAsFlow()
        ) { notes, labels ->
            notes.map { it.fromEntity(labels.toSet()) }
        }.flowOn(Dispatchers.IO)

    val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch { scanTrashbox() }
    }

    override fun addNote(note: TextNote) {
        scope.launch {
            textNoteDao.insertNote(
                note = note.toEntity()
            )
        }
    }


    override fun deleteNote(id: NoteId) {
        scope.launch {
            textNoteDao.deleteById(
                itemId = id
            )
        }
    }
}