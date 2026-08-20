package com.uladzimirv.notegram.domain.model.note.text

import com.uladzimirv.notegram.data.database.entity.TextNoteEntity
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.ui.layout.main.com.toColorNotePref
import com.uladzimirv.notegram.ui.model.NoteUI
import com.uladzimirv.notegram.ui.model.TextNoteUI
import com.uladzimirv.notegram.util.STRING_EMPTY
import java.util.UUID

data class TextNote(
    override val id: String,
    override val createdAt: Long,
    override val updatedAd: Long,
    override val title: String,
    override val pinned: Boolean,
    override val colorPref: ColorPref,
    val text: String,
) : Note(id, createdAt, updatedAd, title, pinned, colorPref) {

    override fun toUIModel(): NoteUI = TextNoteUI(
        id = id,
        text = text,
        title = title,
        pinned = pinned,
        colorPref = colorPref
    )

    override fun getType(): NoteType = NoteType.TEXT

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