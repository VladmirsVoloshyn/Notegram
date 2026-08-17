package com.uladzimirv.notegram.ui.model

import androidx.compose.runtime.Immutable
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref

@Immutable
data class NoteUI(
    val id: String,
    val title: String,
    val text: String,
    val pinned : Boolean,
    val colorPref : ColorPref
)