package com.uladzimirv.notegram.domain.model.note.text

import com.uladzimirv.notegram.data.database.entity.TextNoteEntity
import com.uladzimirv.notegram.domain.model.com.FormalStatus
import com.uladzimirv.notegram.domain.model.com.NoteStatus
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
    override val status: NoteStatus,
    val text: String,
) : Note(id, createdAt, updatedAd, title, pinned, colorPref, status) {

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
            colorPref = ColorPref.COMMON,
            status = NoteStatus.None()
        )

        fun TextNote.toEntity(): TextNoteEntity = TextNoteEntity(
            id = id,
            createdAt = createdAt,
            updatedAd = updatedAd,
            text = text,
            title = title,
            pinned = pinned,
            colorPref = colorPref.stringId,
            status = status.formal.status,
            archivedAt = if (status is NoteStatus.Archived) status.archivedAt else 0,
            deletedAt = if (status is NoteStatus.Deleted) status.deletedAt else 0,
        )

        fun TextNoteEntity.fromEntity(): TextNote = TextNote(
            id = id,
            createdAt = createdAt,
            updatedAd = updatedAd,
            text = text,
            title = title,
            pinned = pinned,
            colorPref = colorPref.toColorNotePref(),
            status = when (this.status) {
                FormalStatus.ARCHIVED.status -> {
                    NoteStatus.Archived(this.archivedAt)
                }

                FormalStatus.DELETED.status -> {
                    NoteStatus.Deleted(this.deletedAt)
                }

                else -> NoteStatus.None()
            }
        )
    }


}