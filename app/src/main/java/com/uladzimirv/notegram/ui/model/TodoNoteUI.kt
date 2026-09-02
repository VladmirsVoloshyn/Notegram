package com.uladzimirv.notegram.ui.model

import com.uladzimirv.notegram.data.database.entity.TodoListItem
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import kotlinx.collections.immutable.ImmutableList

data class TodoNoteUI(
    override val id: NoteId,
    override val pinned: Boolean,
    override val colorPref: ColorPref,
    override val locked: Boolean,
    val title: String,
    val list: ImmutableList<TodoListItem>,
    val selectedList: ImmutableList<TodoListItem>
) : NoteUI(id, colorPref, pinned, locked) {
    override fun summary(): String = "${title}\n${
        (list + selectedList).joinToString(
            separator = "\n"
        ) { "${if (it.selected) "+" else "-"} ${it.text}" }
    }"
}