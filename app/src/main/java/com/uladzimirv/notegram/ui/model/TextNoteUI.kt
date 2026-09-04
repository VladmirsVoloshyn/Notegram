package com.uladzimirv.notegram.ui.model

import com.uladzimirv.notegram.domain.model.label.NoteLabel
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

data class TextNoteUI(
    override val id: String,
    val title: String,
    val text: String,
    override val pinned: Boolean,
    override val colorPref: ColorPref,
    override val locked: Boolean,
    override val labels: ImmutableSet<LabelUI>,
) : NoteUI(id, colorPref, pinned, locked, labels) {
    override fun summary(): String = "${title}\n${text}"
}