package com.uladzimirv.notegram.domain.model.label

import com.uladzimirv.notegram.data.database.entity.LabelEntity
import com.uladzimirv.notegram.ui.layout.main.com.LabelColorPref
import com.uladzimirv.notegram.ui.model.LabelUI

abstract class Label(
    open val id: LabelId,
    open val name: String,
    open val colorPref: LabelColorPref
) {
    abstract fun toUIModel(): LabelUI

    abstract fun toEntity() : LabelEntity
}