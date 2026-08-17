package com.uladzimirv.notegram.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uladzimirv.notegram.data.database.entity.TextNoteEntity
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.util.TEXT_NOTE_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface TextNoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: TextNoteEntity)

    @Query("SELECT * FROM $TEXT_NOTE_TABLE_NAME")
    fun getAllTextNotesAsFlow(): Flow<List<TextNoteEntity>>

    @Query("SELECT * FROM $TEXT_NOTE_TABLE_NAME")
    fun getAllTextNotesAsList(): List<TextNoteEntity>

    @Query("DELETE FROM $TEXT_NOTE_TABLE_NAME WHERE id = :itemId")
    suspend fun deleteById(itemId: NoteId): Int

}