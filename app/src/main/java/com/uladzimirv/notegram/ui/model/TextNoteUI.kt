package com.uladzimirv.notegram.ui.model

import com.uladzimirv.notegram.ui.layout.main.com.ColorPref

data class TextNoteUI(
    override val id: String,
    val title: String,
    val text: String,
    override val pinned: Boolean,
    override val colorPref: ColorPref,
    override val locked: Boolean
) : NoteUI(id, colorPref, pinned, locked) {
    override fun summary(): String = "${title}\n${text}"
}