package com.uladzimirv.notegram.domain.model.note.todo

import com.uladzimirv.notegram.data.database.entity.LabelEntity
import com.uladzimirv.notegram.data.database.entity.TodoListItem
import com.uladzimirv.notegram.data.database.entity.TodoNoteEntity
import com.uladzimirv.notegram.domain.model.com.FormalStatus
import com.uladzimirv.notegram.domain.model.com.NoteStatus
import com.uladzimirv.notegram.domain.model.label.Label
import com.uladzimirv.notegram.domain.model.label.NoteLabel.Companion.fromEntity
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.ui.layout.main.com.toColorNotePref
import com.uladzimirv.notegram.ui.model.NoteUI
import com.uladzimirv.notegram.ui.model.TodoNoteUI
import com.uladzimirv.notegram.util.STRING_EMPTY
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
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
    override val labels: Set<Label>,
    val todoList: ImmutableList<TodoListItem>,
    val selectedTodoList: ImmutableList<TodoListItem>
) : Note(id, createdAt, updatedAd, title, pinned, colorPref, status, locked, labels) {

    override fun toUIModel(): NoteUI = TodoNoteUI(
        id = id,
        title = title,
        pinned = pinned,
        colorPref = colorPref,
        list = todoList,
        selectedList = selectedTodoList,
        locked = locked,
        labels = labels.map { it.toUIModel() }.toPersistentSet()
    )

    override fun getType(): NoteType = NoteType.TODO

    companion object {
        fun empty(
            title: String = STRING_EMPTY,
            list: List<TodoListItem> = emptyList(),
            colorPref : ColorPref = ColorPref.COMMON,
            createdAt: Long = System.currentTimeMillis()
        ): TodoListNote =
            TodoListNote(
                id = UUID.randomUUID().toString(),
                createdAt = createdAt,
                updatedAd = createdAt,
                title = title,
                pinned = false,
                colorPref = colorPref,
                todoList = list.filter { !it.selected }.toPersistentList(),
                selectedTodoList = list.filter { it.selected }.toPersistentList(),
                status = NoteStatus.None(),
                locked = false,
                labels = emptySet()
            )

        fun TodoNoteEntity.fromEntity(labels: Set<LabelEntity>): TodoListNote = TodoListNote(
            id = id,
            createdAt = createdAt,
            updatedAd = updatedAd,
            title = title,
            pinned = pinned,
            colorPref = colorPref.toColorNotePref(),
            todoList = list.filter { !it.selected }.toPersistentList(),
            selectedTodoList = list.filter { it.selected }.toPersistentList(),
            locked = locked,
            labels = labelsId.mapNotNull { id ->
                labels.find { it.id == id }?.fromEntity()
            }.toSet(),
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
            status = status.formal.status,
            labelsId = labels.map { it.id }
        )
    }
}