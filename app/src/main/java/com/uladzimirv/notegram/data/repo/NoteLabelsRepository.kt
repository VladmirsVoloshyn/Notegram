package com.uladzimirv.notegram.data.repo

import com.uladzimirv.notegram.data.database.dao.LabelsDao
import com.uladzimirv.notegram.domain.model.label.LabelId
import com.uladzimirv.notegram.domain.model.label.NoteLabel
import com.uladzimirv.notegram.domain.model.label.NoteLabel.Companion.fromEntity
import com.uladzimirv.notegram.util.VEVO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteLabelsRepository @Inject constructor(
    private val labelsDao: LabelsDao
) : LabelsRepository<NoteLabel>() {


    val scope = CoroutineScope(Dispatchers.IO)

    override val labelsFlow: Flow<List<NoteLabel>> = labelsDao.getAllLabelsAsFlow().map { entity ->
        entity.map { it.fromEntity() }
    }.flowOn(Dispatchers.IO)

    override fun addLabel(label: NoteLabel) {
        scope.launch {
            labelsDao.insertLabel(
                label = label.toEntity()
            )
        }
    }

    override fun deleteLabel(labelId: LabelId) {
        scope.launch {
            labelsDao.deleteById(
                labelId
            )
        }
    }
}