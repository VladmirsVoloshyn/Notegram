package com.uladzimirv.notegram.data.repo

import com.uladzimirv.notegram.domain.model.label.Label
import com.uladzimirv.notegram.domain.model.label.LabelId
import kotlinx.coroutines.flow.Flow

abstract class LabelsRepository <T : Label> {

    abstract val labelsFlow: Flow<List<T>>

    abstract fun addLabel(label: T)

    abstract fun deleteLabel(labelId: LabelId)
}