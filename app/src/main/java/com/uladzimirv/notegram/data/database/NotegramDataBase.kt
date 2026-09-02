package com.uladzimirv.notegram.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uladzimirv.notegram.data.database.converter.ListTodoTypeConverter
import com.uladzimirv.notegram.data.database.dao.TextNoteDao
import com.uladzimirv.notegram.data.database.dao.TodoNoteDao
import com.uladzimirv.notegram.data.database.entity.TextNoteEntity
import com.uladzimirv.notegram.data.database.entity.TodoNoteEntity

@Database(
    entities = [
        TextNoteEntity::class,
        TodoNoteEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(ListTodoTypeConverter::class)
abstract class NotegramDataBase : RoomDatabase() {

    abstract fun textNotesDao(): TextNoteDao

    abstract fun todoNotesDao(): TodoNoteDao
}