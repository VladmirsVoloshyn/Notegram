package com.uladzimirv.notegram.domain.manager

import com.uladzimirv.notegram.data.repo.NoteLabelsRepository
import com.uladzimirv.notegram.domain.manager.mock.labelsMock
import com.uladzimirv.notegram.domain.model.label.Label
import com.uladzimirv.notegram.domain.model.label.LabelId
import com.uladzimirv.notegram.domain.model.label.NoteLabel
import com.uladzimirv.notegram.domain.model.label.NoteLabel.Companion.fromEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelsManager @Inject constructor(
    private val labelsRepository: NoteLabelsRepository
) {

    val labels = labelsRepository.labelsFlow

    fun addLabel(label: Label) {
        when (label) {
            is NoteLabel -> labelsRepository.addLabel(label)
        }
    }

    fun deleteLabel(id: LabelId) {
        labelsRepository.deleteLabel(id)
    }

}