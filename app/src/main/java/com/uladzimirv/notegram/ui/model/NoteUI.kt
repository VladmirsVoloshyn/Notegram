package com.uladzimirv.notegram.ui.model

import androidx.compose.runtime.Immutable
import com.uladzimirv.notegram.domain.model.label.NoteLabel
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@Immutable
abstract class NoteUI(
    open val id: NoteId,
    open val colorPref: ColorPref,
    open val pinned: Boolean,
    open val locked: Boolean,
    open val labels: ImmutableSet<LabelUI>
) {
    abstract fun summary(): String
}