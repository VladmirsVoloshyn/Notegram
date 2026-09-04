package com.uladzimirv.notegram.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uladzimirv.notegram.data.database.converter.LabelsTypeConverter
import com.uladzimirv.notegram.data.database.converter.ListStringTypeConverter
import com.uladzimirv.notegram.data.database.converter.ListTodoTypeConverter
import com.uladzimirv.notegram.data.database.dao.LabelsDao
import com.uladzimirv.notegram.data.database.dao.TextNoteDao
import com.uladzimirv.notegram.data.database.dao.TodoNoteDao
import com.uladzimirv.notegram.data.database.entity.LabelEntity
import com.uladzimirv.notegram.data.database.entity.TextNoteEntity
import com.uladzimirv.notegram.data.database.entity.TodoNoteEntity

@Database(
    entities = [
        TextNoteEntity::class,
        TodoNoteEntity::class,
        LabelEntity::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(ListTodoTypeConverter::class, LabelsTypeConverter::class, ListStringTypeConverter::class)
abstract class NotegramDataBase : RoomDatabase() {

    abstract fun textNotesDao(): TextNoteDao

    abstract fun todoNotesDao(): TodoNoteDao

    abstract fun labelsDao(): LabelsDao
}