package com.uladzimirv.notegram.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uladzimirv.notegram.util.LABELS_TABLE_NAME

@Entity(tableName = LABELS_TABLE_NAME)
class LabelEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val colorPref: String,
)