package com.uladzimirv.notegram.domain.model.note.text

import com.uladzimirv.notegram.data.database.entity.LabelEntity
import com.uladzimirv.notegram.data.database.entity.TextNoteEntity
import com.uladzimirv.notegram.domain.model.com.FormalStatus
import com.uladzimirv.notegram.domain.model.com.NoteStatus
import com.uladzimirv.notegram.domain.model.label.Label
import com.uladzimirv.notegram.domain.model.label.NoteLabel.Companion.fromEntity
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.NoteType
import com.uladzimirv.notegram.ui.layout.main.com.toColorNotePref
import com.uladzimirv.notegram.ui.model.NoteUI
import com.uladzimirv.notegram.ui.model.TextNoteUI
import com.uladzimirv.notegram.util.STRING_EMPTY
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import java.util.UUID

data class TextNote(
    override val id: String,
    override val createdAt: Long,
    override val updatedAd: Long,
    override val title: String,
    override val pinned: Boolean,
    override val colorPref: ColorPref,
    override val status: NoteStatus,
    override val locked: Boolean,
    override val labels: Set<Label>,
    val text: String,
) : Note(id, createdAt, updatedAd, title, pinned, colorPref, status, locked, labels) {

    override fun toUIModel(): NoteUI = TextNoteUI(
        id = id,
        text = text,
        title = title,
        pinned = pinned,
        colorPref = colorPref,
        locked = locked,
        labels = labels.map { it.toUIModel() }.toPersistentSet()
    )

    override fun getType(): NoteType = NoteType.TEXT

    companion object {
        fun empty(
            text: String = STRING_EMPTY,
            title: String = STRING_EMPTY,
            colorPref: ColorPref = ColorPref.COMMON,
            createdAt: Long = System.currentTimeMillis()
        ): TextNote = TextNote(
            id = UUID.randomUUID().toString(),
            createdAt = createdAt,
            updatedAd = createdAt,
            title = title,
            text = text,
            pinned = false,
            colorPref = colorPref,
            status = NoteStatus.None(),
            locked = false,
            labels = emptySet()
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
            locked = locked,
            labelsId = labels.map { it.id },
            archivedAt = if (status is NoteStatus.Archived) status.archivedAt else 0,
            deletedAt = if (status is NoteStatus.Deleted) status.deletedAt else 0,
        )

        fun TextNoteEntity.fromEntity(labels: Set<LabelEntity>): TextNote = TextNote(
            id = id,
            createdAt = createdAt,
            updatedAd = updatedAd,
            text = text,
            title = title,
            pinned = pinned,
            colorPref = colorPref.toColorNotePref(),
            locked = locked,
            labels = labelsId.mapNotNull { id ->
                labels.find { it.id == id }?.fromEntity()
            }.toSet(),
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