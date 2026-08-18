package com.uladzimirv.notegram.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uladzimirv.notegram.data.database.entity.TodoNoteEntity
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.util.TODO_NOTE_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoNoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: TodoNoteEntity)

    @Query("SELECT * FROM $TODO_NOTE_TABLE_NAME")
    fun getAllTextNotesAsFlow(): Flow<List<TodoNoteEntity>>

    @Query("SELECT * FROM $TODO_NOTE_TABLE_NAME")
    fun getAllTextNotesAsList(): List<TodoNoteEntity>

    @Query("DELETE FROM $TODO_NOTE_TABLE_NAME WHERE id = :itemId")
    suspend fun deleteById(itemId: NoteId): Int

}