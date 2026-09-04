package com.uladzimirv.notegram.domain.manager.mock

import com.uladzimirv.notegram.data.repo.NoteLabelsRepository
import com.uladzimirv.notegram.data.repo.TextNoteRepository
import com.uladzimirv.notegram.data.repo.TodoNoteRepository
import com.uladzimirv.notegram.domain.model.label.Label
import com.uladzimirv.notegram.domain.model.label.NoteLabel
import com.uladzimirv.notegram.domain.model.label.NoteLabel.Companion.fromEntity
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.domain.model.note.text.TextNote
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote.Companion.toEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockManager @Inject constructor(
    private val textNoteRepository: TextNoteRepository,
    private val todoNoteRepository: TodoNoteRepository,
    private val labelsRepository: NoteLabelsRepository
) {

    fun mock(){
//        mockedAdd()
//        mockedAddLabel()
        addNote(bigTodoItem)
    }


    fun addLabel(label: Label) {
        when (label) {
            is NoteLabel -> labelsRepository.addLabel(label)
        }
    }

    fun addNote(note: Note) {
        when (note) {
            is TextNote -> textNoteRepository.addNote(note)
            is TodoListNote -> todoNoteRepository.addNote(note)
        }
    }


    fun mockedAddLabel(){
        labelsMock.forEach {
            addLabel(it.fromEntity())
        }
    }


    private fun mockedAdd() {
        textNotes.forEach {
            addNote(it)
        }
        todoNotes.forEach {
            addNote(it)
        }
    }
}