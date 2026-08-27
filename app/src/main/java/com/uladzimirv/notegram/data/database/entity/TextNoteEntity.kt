package com.uladzimirv.notegram.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uladzimirv.notegram.util.TEXT_NOTE_TABLE_NAME

@Entity(tableName = TEXT_NOTE_TABLE_NAME)
data class TextNoteEntity(
    @PrimaryKey
    val id: String,
    val createdAt: Long,
    val updatedAd: Long,
    val title: String,
    val text: String,
    val pinned : Boolean,
    val colorPref : String,
    val status : String,
    val deletedAt: Long,
    val archivedAt: Long
)