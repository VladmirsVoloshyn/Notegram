package com.uladzimirv.notegram.domain.model.label

import com.uladzimirv.notegram.data.database.entity.LabelEntity
import com.uladzimirv.notegram.ui.layout.main.com.LabelColorPref
import com.uladzimirv.notegram.ui.layout.main.com.toColorLabelPref
import com.uladzimirv.notegram.ui.model.LabelUI


data class NoteLabel(
    override val id: LabelId,
    override val name: String,
    override val colorPref: LabelColorPref
) : Label(id, name, colorPref) {

    override fun toUIModel() = LabelUI(
        id = id,
        name = name,
        colorPref = colorPref
    )

    override fun toEntity()= LabelEntity(
        id = id,
        name = name,
        colorPref = colorPref.stringId
    )

    companion object {
        fun LabelEntity.fromEntity() = NoteLabel(
            id = id,
            name = name,
            colorPref = colorPref.toColorLabelPref()
        )
    }
}