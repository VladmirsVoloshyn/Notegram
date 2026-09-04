package com.uladzimirv.notegram.app_flow.main.contract.middleware

import com.uladzimirv.notegram.app_flow.main.contract.ApplicationMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.data.database.entity.TodoListItem
import com.uladzimirv.notegram.domain.model.label.LabelId
import com.uladzimirv.notegram.domain.model.note.text.TextNote
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.model.LabelUI.Companion.toDomainModel
import com.uladzimirv.notegram.util.STRING_EMPTY
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import java.util.UUID

sealed interface NoteScreenMiddleware : ApplicationMiddleware {
    override fun reduce(viewState: ApplicationViewState): ApplicationViewState {
        return when (this) {
            is OpenAddLabelMenu -> {
                viewState.copy(
                    noteState = viewState.noteState.copy(
                        showAddLabelSheet = open
                    )
                )
            }

            is SelectLabel -> {
                viewState.copy(
                    noteState = viewState.noteState.copy(
                        selectedLabel = viewState.labelsState.labels.find { it.id == id }
                    )
                )
            }

            is RemoveLabel -> {
                viewState.noteState.note?.let { note ->
                    when (note) {
                        is TextNote -> {
                            val list = note.labels.toMutableList().filter { it.id != id }
                                .toPersistentSet()
                            viewState.copy(
                                noteState = viewState.noteState.copy(
                                    note = note.copy(
                                        labels = list
                                    )
                                )
                            )
                        }

                        is TodoListNote -> {
                            val list = note.labels.toMutableList().filter { it.id != id }
                                .toPersistentSet()
                            viewState.copy(
                                noteState = viewState.noteState.copy(
                                    note = note.copy(
                                        labels = list
                                    )
                                )
                            )
                        }

                        else -> viewState
                    }
                } ?: viewState
            }

            is AddLabelToNote -> {
                viewState.noteState.note?.let { note ->
                    when (note) {
                        is TextNote -> {
                            val list = note.labels.toMutableSet()
                            val label = viewState.labelsState.labels.find { it.id == labelId }
                            label?.let { list.add(it.toDomainModel()) }
                            viewState.copy(
                                noteState = viewState.noteState.copy(
                                    note = note.copy(
                                        labels = list
                                    )
                                )
                            )
                        }

                        is TodoListNote -> {
                            val list = note.labels.toMutableSet()
                            val label = viewState.labelsState.labels.find { it.id == labelId }
                            label?.let { list.add(it.toDomainModel()) }
                            viewState.copy(
                                noteState = viewState.noteState.copy(
                                    note = note.copy(
                                        labels = list
                                    )
                                )
                            )
                        }

                        else -> viewState
                    }
                } ?: viewState
            }

            is EditNoteTitle -> {
                when (val note = viewState.noteState.note) {
                    is TodoListNote -> {
                        viewState.copy(
                            noteState = viewState.noteState.copy(
                                note = note.copy(
                                    title = title
                                )
                            )
                        )
                    }

                    is TextNote -> viewState.copy(
                        noteState = viewState.noteState.copy(
                            note = note.copy(
                                title = title
                            )
                        )
                    )

                    else -> viewState
                }

            }

            is EditNoteText -> {
                if (viewState.noteState.note is TextNote) {
                    val note = viewState.noteState.note
                    viewState.copy(
                        noteState = viewState.noteState.copy(
                            note = note.copy(
                                text = text
                            )
                        )
                    )
                } else viewState

            }

            is EditNoteColor -> {
                when (val note = viewState.noteState.note) {
                    is TodoListNote -> {
                        viewState.copy(
                            noteState = viewState.noteState.copy(
                                note = note.copy(
                                    colorPref = colorPref
                                )
                            )
                        )
                    }

                    is TextNote -> viewState.copy(
                        noteState = viewState.noteState.copy(
                            note = note.copy(
                                colorPref = colorPref
                            )
                        )
                    )

                    else -> viewState
                }
            }

            is EditTodo -> {
                val note = viewState.noteState.note as? TodoListNote ?: return viewState
                val list = note.todoList.toMutableList()
                if (todoIdemId == null) {
                    list.add(
                        TodoListItem(
                            id = UUID.randomUUID().toString(),
                            text = STRING_EMPTY,
                            position = list.size,
                            selected = false
                        )
                    )
                    val new = note.copy(
                        todoList = list.toPersistentList()
                    )
                    viewState.copy(
                        noteState = viewState.noteState.copy(
                            note = new
                        )
                    )
                } else {
                    val item = list.find { it.id == todoIdemId } ?: return viewState
                    val newList = list.filter { it.id != todoIdemId }.toMutableList()
                    newList.add(
                        index = item.position,
                        item.copy(
                            text = text
                        )
                    )
                    viewState.copy(
                        noteState = viewState.noteState.copy(
                            note = viewState.noteState.note.copy(
                                todoList = newList.toPersistentList()
                            )
                        )
                    )
                }
            }

            is DeleteTodo -> {
                val note = viewState.noteState.note as? TodoListNote ?: return viewState
                val list = (note.todoList + note.selectedTodoList)
                    .filter { it.id != todoIdemId }
                    .mapIndexed { index, item ->
                        item.copy(
                            position = index
                        )
                    }

                viewState.copy(
                    noteState = viewState.noteState.copy(
                        note = viewState.noteState.note.copy(
                            todoList = list.filter { !it.selected }.toPersistentList(),
                            selectedTodoList = list.filter { it.selected }.toPersistentList()
                        )
                    )
                )
            }

            is CheckTodo -> {
                val note = viewState.noteState.note as? TodoListNote ?: return viewState
                val list = note.todoList + note.selectedTodoList
                val item = list.find { it.id == todoIdemId } ?: return viewState
                val newList = list.filter { it.id != todoIdemId }.toMutableList()
                newList.add(
                    index = item.position,
                    item.copy(
                        selected = !item.selected
                    )
                )
                viewState.copy(
                    noteState = viewState.noteState.copy(
                        note = viewState.noteState.note.copy(
                            todoList = newList.filter { !it.selected }.toPersistentList(),
                            selectedTodoList = newList.filter { it.selected }.toPersistentList()
                        )
                    )
                )
            }

            is OpenTopMenu -> {
                viewState.copy(
                    noteState = viewState.noteState.copy(
                        topMenuOpened = open
                    )
                )
            }

            is ReorderTodo -> {
                val note = viewState.noteState.note as? TodoListNote ?: return viewState
                val newList =
                    note.todoList.toMutableList().apply {
                        if (to in note.todoList.indices) {
                            add(to, removeAt(from))
                        } else return viewState
                    }
                viewState.copy(
                    noteState = viewState.noteState.copy(
                        note = viewState.noteState.note.copy(
                            todoList = newList.mapIndexed { index, item ->
                                item.copy(
                                    position = index
                                )
                            }.toPersistentList()
                        )
                    )
                )
            }
        }
    }

    data class OpenAddLabelMenu(val open: Boolean) : NoteScreenMiddleware
    data class OpenTopMenu(val open: Boolean) : NoteScreenMiddleware
    data class SelectLabel(val id: LabelId) : NoteScreenMiddleware
    data class RemoveLabel(val id: LabelId) : NoteScreenMiddleware
    data class EditNoteTitle(val title: String) : NoteScreenMiddleware
    data class AddLabelToNote(val labelId: LabelId) : NoteScreenMiddleware
    data class EditNoteText(val text: String) : NoteScreenMiddleware
    data class EditNoteColor(val colorPref: ColorPref) : NoteScreenMiddleware
    data class EditTodo(val text: String, val todoIdemId: String? = null) : NoteScreenMiddleware
    data class DeleteTodo(val todoIdemId: String) : NoteScreenMiddleware
    data class CheckTodo(val todoIdemId: String) : NoteScreenMiddleware
    data class ReorderTodo(val id: String, val from: Int, val to: Int) : NoteScreenMiddleware
}