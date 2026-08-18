package com.uladzimirv.notegram.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uladzimirv.notegram.data.database.entity.TodoListItem
import com.uladzimirv.notegram.data.database.entity.TodoNoteEntity

class ListTodoTypeConverter {

    val gson = Gson()

    @TypeConverter
    fun todoListToString(value: List<TodoListItem>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun todoListFronString(value: String): List<TodoListItem> {
        val type = object : TypeToken<List<TodoListItem>>() {}.type
        return gson.fromJson(value, type)
    }

}