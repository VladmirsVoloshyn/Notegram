package com.uladzimirv.notegram.ui.model

import com.uladzimirv.notegram.ui.layout.main.com.ColorPref

data class TextNoteUI(
    override val id: String,
    val title: String,
    val text: String,
    override val pinned: Boolean,
    override val colorPref: ColorPref
) : NoteUI(id, colorPref, pinned)