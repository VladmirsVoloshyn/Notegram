package com.uladzimirv.notegram.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uladzimirv.notegram.data.database.dao.TextNoteDao
import com.uladzimirv.notegram.data.database.entity.TextNoteEntity

@Database(
    entities = [TextNoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NotegramDataBase : RoomDatabase() {

    abstract fun textNotesDao(): TextNoteDao
}