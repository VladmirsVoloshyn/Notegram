package com.uladzimirv.notegram.data.repo

import com.uladzimirv.notegram.data.database.entity.TodoListItem
import com.uladzimirv.notegram.domain.model.note.text.TextNote
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote
import java.util.UUID

val textNotes = listOf(
    TextNote.empty(
        title = "Note 1",
        text = "Short Text Short TextShort TextShort TextShort TextShort TextShort TextShort TextShort TextShort TextShort TextShort Text"
    ),
    TextNote.empty(
        title = "Note 2",
        text = "Short Text Short TextShort TextShort TextShort TextShort "
    ),
    TextNote.empty(
        title = "Note 3",
        text = "Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text "
    ),
    TextNote.empty(
        title = "Note 4",
        text = "Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long TextLong Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long TextLong Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text"
    ),
    TextNote.empty(
        title = "Note 5",
        text = "Short Text Short TextShort TextShort TextShort TextShort Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long"
    ),
    TextNote.empty(
        title = "Note 6",
        text = "Short Text Short TextShort TextShort TextShort TextShort "
    ),
    TextNote.empty(
        title = "Note 7",
        text = "Short Text Short TextShort TextShort TextShort TextShort "
    ),
    TextNote.empty(
        title = "Note 8",
        text = "Short Text Short TextShort TextShort TextShort TextShort "
    ),
)

val todoNotes = listOf(
    TodoListNote.empty(
        title = "Note 1",
        list = listOf(
            TodoListItem(
                text = "SomeText",
                selected = false,
                position = 0,
                id = UUID.randomUUID().toString()
            ),
            TodoListItem(
                text = "SomeTextSomeText",
                selected = false,
                position = 1,
                id = UUID.randomUUID().toString()
            ),
            TodoListItem(
                text = "SomeText1",
                selected = false,
                position = 2,
                id = UUID.randomUUID().toString()
            ),
            TodoListItem(
                text = "SomeText2",
                selected = false,
                position = 3,
                id = UUID.randomUUID().toString()
            )
        )
    ),
    TodoListNote.empty(
        title = "Note 2",
        list = listOf(
            TodoListItem(
                text = "SomeText",
                selected = false,
                position = 0,
                id = UUID.randomUUID().toString()
            ),
            TodoListItem(
                text = "SomeTextSomeText",
                selected = false,
                position = 1,
                id = UUID.randomUUID().toString()
            ),
            TodoListItem(
                text = "SomeText1",
                selected = false,
                position = 2,
                id = UUID.randomUUID().toString()
            )
        )
    ),
    TodoListNote.empty(
        title = "Note 3",
        list = listOf(
            TodoListItem(
                text = "SomeText",
                selected = false,
                position = 0,
                id = UUID.randomUUID().toString()
            ),
            TodoListItem(
                text = "SomeTextSomeText",
                selected = false,
                position = 1,
                id = UUID.randomUUID().toString()
            ),
            TodoListItem(
                text = "SomeText1",
                selected = false,
                position = 2,
                id = UUID.randomUUID().toString()
            )
        )
    )
)