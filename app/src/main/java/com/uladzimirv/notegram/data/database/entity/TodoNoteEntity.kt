package com.uladzimirv.notegram.data.database.entity

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uladzimirv.notegram.util.TODO_NOTE_TABLE_NAME

@Entity(tableName = TODO_NOTE_TABLE_NAME)
class TodoNoteEntity(
    @PrimaryKey
    val id: String,
    val createdAt: Long,
    val updatedAd: Long,
    val title: String,
    val pinned: Boolean,
    val colorPref: String,
    val list: List<TodoListItem>,
    val status: String,
    val deletedAt: Long,
    val archivedAt: Long,
    val locked: Boolean
)

@Immutable
data class TodoListItem(
    val id: String,
    val text: String,
    val selected: Boolean,
    val position: Int
)