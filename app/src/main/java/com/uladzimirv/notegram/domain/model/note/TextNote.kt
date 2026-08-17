package com.uladzimirv.notegram.domain.model.note

import com.uladzimirv.notegram.data.database.entity.TextNoteEntity
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.toColorNotePref
import com.uladzimirv.notegram.util.STRING_EMPTY
import java.util.UUID

data class TextNote(
    override val id: String,
    override val createdAt: Long,
    override val updatedAd: Long,
    override val title: String,
    override val text: String,
    override val pinned: Boolean,
    override val colorPref: ColorPref
) : Note(id, createdAt, createdAt, title, text, pinned, colorPref) {

    companion object {
        fun empty(text: String = STRING_EMPTY, title: String = STRING_EMPTY): TextNote = TextNote(
            id = UUID.randomUUID().toString(),
            createdAt = System.currentTimeMillis(),
            updatedAd = System.currentTimeMillis(),
            title = title,
            text = text,
            pinned = false,
            colorPref = ColorPref.COMMON
        )

        fun TextNote.toEntity(): TextNoteEntity = TextNoteEntity(
            id = id,
            createdAt = createdAt,
            updatedAd = updatedAd,
            text = text,
            title = title,
            pinned = pinned,
            colorPref = colorPref.stringId
        )

        fun TextNoteEntity.fromEntity(): TextNote = TextNote(
            id = id,
            createdAt = createdAt,
            updatedAd = updatedAd,
            text = text,
            title = title,
            pinned = pinned,
            colorPref = colorPref.toColorNotePref()
        )
    }


}