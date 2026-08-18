package com.uladzimirv.notegram.ui.model

import androidx.compose.runtime.Immutable
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref

@Immutable
abstract class NoteUI(
    open val id: NoteId,
    open val colorPref: ColorPref,
    open val pinned: Boolean
)