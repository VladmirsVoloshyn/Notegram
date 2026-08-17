package com.uladzimirv.notegram.domain.model.note

import androidx.compose.runtime.Immutable
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.model.NoteUI

@Immutable
abstract class Note(
    open val id: String,
    open val createdAt: Long,
    open val updatedAd: Long,
    open val title: String,
    open val text: String,
    open val pinned: Boolean,
    open val colorPref: ColorPref
) {
    companion object {
        fun Note.toUIModel() = NoteUI(
            id = id,
            text = text,
            title = title,
            pinned = pinned,
            colorPref = colorPref
        )
    }
}