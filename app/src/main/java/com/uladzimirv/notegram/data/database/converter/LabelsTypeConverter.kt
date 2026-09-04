package com.uladzimirv.notegram.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uladzimirv.notegram.data.database.entity.LabelEntity

class LabelsTypeConverter {

    val gson = Gson()

    @TypeConverter
    fun labelsListToString(value: List<LabelEntity>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun labelsListFromString(value: String): List<LabelEntity> {
        val type = object : TypeToken<List<LabelEntity>>() {}.type
        return gson.fromJson(value, type)
    }

}