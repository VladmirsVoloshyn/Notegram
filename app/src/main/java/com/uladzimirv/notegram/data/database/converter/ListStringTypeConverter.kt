package com.uladzimirv.notegram.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uladzimirv.notegram.data.database.entity.LabelEntity

class ListStringTypeConverter {

    val gson = Gson()

    @TypeConverter
    fun labelsListToString(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun labelsListFromString(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

}