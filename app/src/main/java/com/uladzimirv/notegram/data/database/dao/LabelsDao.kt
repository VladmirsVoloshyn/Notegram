package com.uladzimirv.notegram.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uladzimirv.notegram.data.database.entity.LabelEntity
import com.uladzimirv.notegram.domain.model.label.LabelId
import com.uladzimirv.notegram.util.LABELS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLabel(label: LabelEntity)

    @Query("SELECT * FROM $LABELS_TABLE_NAME")
    fun getAllLabelsAsFlow(): Flow<List<LabelEntity>>

    @Query("DELETE FROM $LABELS_TABLE_NAME WHERE id = :itemId")
    suspend fun deleteById(itemId: LabelId): Int

}
