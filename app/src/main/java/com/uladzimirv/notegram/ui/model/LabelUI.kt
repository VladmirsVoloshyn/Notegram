package com.uladzimirv.notegram.ui.model

import androidx.compose.runtime.Immutable
import com.uladzimirv.notegram.domain.model.label.LabelId
import com.uladzimirv.notegram.domain.model.label.NoteLabel
import com.uladzimirv.notegram.ui.layout.main.com.LabelColorPref
import com.uladzimirv.notegram.util.STRING_EMPTY
import java.util.UUID

@Immutable
data class LabelUI(
    val id: LabelId,
    val name: String,
    val colorPref: LabelColorPref
) {
    companion object {
        fun empty() = LabelUI(
            id = UUID.randomUUID().toString(),
            name = STRING_EMPTY,
            colorPref = LabelColorPref.COMMON
        )

        fun LabelUI.toDomainModel() = NoteLabel(
            id = id,
            name = name,
            colorPref = colorPref
        )
    }
}