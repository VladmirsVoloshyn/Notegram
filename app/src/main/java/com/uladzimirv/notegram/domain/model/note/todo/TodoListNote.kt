package com.uladzimirv.notegram.domain.model.note.todo

import com.uladzimirv.notegram.data.database.entity.TodoListItem
import com.uladzimirv.notegram.data.database.entity.TodoNoteEntity
import com.uladzimirv.notegram.domain.model.com.FormalStatus
import com.uladzimirv.notegram.domain.model.com.NoteStatus
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.ui.layout.main.com.toColorNotePref
import com.uladzimirv.notegram.ui.model.NoteUI
import com.uladzimirv.notegram.ui.model.TodoNoteUI
import com.uladzimirv.notegram.util.STRING_EMPTY
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import java.util.UUID

data class TodoListNote(
    override val id: String,
    override val createdAt: Long,
    override val updatedAd: Long,
    override val title: String,
    override val pinned: Boolean,
    override val colorPref: ColorPref,
    override val status: NoteStatus,
    override val locked: Boolean,
    val todoList: ImmutableList<TodoListItem>,
    val selectedTodoList: ImmutableList<TodoListItem>
) : Note(id, createdAt, updatedAd, title, pinned, colorPref, status, locked) {

    override fun toUIModel(): NoteUI = TodoNoteUI(
        id = id,
        title = title,
        pinned = pinned,
        colorPref = colorPref,
        list = todoList,
        selectedList = selectedTodoList,
        locked = locked
    )

    override fun getType(): NoteType = NoteType.TODO

    companion object {

        fun empty(
            title: String = STRING_EMPTY,
            list: List<TodoListItem> = emptyList()
        ): TodoListNote =
            TodoListNote(
                id = UUID.randomUUID().toString(),
                createdAt = System.currentTimeMillis(),
                updatedAd = System.currentTimeMillis(),
                title = title,
                pinned = false,
                colorPref = ColorPref.COMMON,
                todoList = list.filter { !it.selected }.toPersistentList(),
                selectedTodoList = list.filter { it.selected }.toPersistentList(),
                status = NoteStatus.None(),
                locked = false
            )

        fun TodoNoteEntity.fromEntity(): TodoListNote = TodoListNote(
            id = id,
            createdAt = createdAt,
            updatedAd = updatedAd,
            title = title,
            pinned = pinned,
            colorPref = colorPref.toColorNotePref(),
            todoList = list.filter { !it.selected }.toPersistentList(),
            selectedTodoList = list.filter { it.selected }.toPersistentList(),
            locked = locked,
            status = when (this.status) {

                FormalStatus.ARCHIVED.status -> {
                    NoteStatus.Archived(this.archivedAt)
                }

                FormalStatus.DELETED.status -> {
                    NoteStatus.Deleted(this.deletedAt)
                }

                else -> NoteStatus.None()
            }
        )

        fun TodoListNote.toEntity(): TodoNoteEntity = TodoNoteEntity(
            id = id,
            createdAt = createdAt,
            updatedAd = updatedAd,
            title = title,
            pinned = pinned,
            locked = locked,
            colorPref = colorPref.stringId,
            list = todoList + selectedTodoList,
            archivedAt = if (status is NoteStatus.Archived) status.archivedAt else 0,
            deletedAt = if (status is NoteStatus.Deleted) status.deletedAt else 0,
            status = status.formal.status
        )
    }
}