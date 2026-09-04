package com.uladzimirv.notegram.domain.model.note

import com.uladzimirv.notegram.domain.model.com.NoteStatus
import com.uladzimirv.notegram.domain.model.label.Label
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.ui.model.NoteUI

abstract class Note(
    open val id: String,
    open val createdAt: Long,
    open val updatedAd: Long,
    open val title: String,
    open val pinned: Boolean,
    open val colorPref: ColorPref,
    open val status: NoteStatus,
    open val locked: Boolean,
    open val labels: Set<Label>
) {

    abstract fun toUIModel(): NoteUI

    abstract fun getType(): NoteType

}