package com.uladzimirv.notegram.domain.manager.mock

import com.uladzimirv.notegram.data.database.entity.LabelEntity
import com.uladzimirv.notegram.data.database.entity.TodoListItem
import com.uladzimirv.notegram.domain.model.note.text.TextNote
import com.uladzimirv.notegram.domain.model.note.todo.TodoListNote
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.LabelColorPref
import java.util.UUID
import kotlin.random.Random

val labelsMock = listOf(
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),
    LabelEntity(
        id = UUID.randomUUID().toString(),
        name = "Label ${Random.nextInt(100)}",
        colorPref = LabelColorPref.entries.random().stringId
    ),


    )

val textNotes = listOf(
    TextNote.empty(
        title = "Note 1",
        createdAt = 1788443520418,
        text = "Short Text Short TextShort TextShort TextShort TextShort TextShort TextShort TextShort TextShort TextShort TextShort Text",
        colorPref = ColorPref.entries.random()
    ),
    TextNote.empty(
        title = "Note 2",
        createdAt = 1788443520419,
        text = "Short Text Short TextShort TextShort TextShort TextShort ",
        colorPref = ColorPref.entries.random()
    ),
    TextNote.empty(
        title = "Note 3",
        createdAt = 1788443520420,
        text = "Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text ",
        colorPref = ColorPref.entries.random()
    ),
    TextNote.empty(
        title = "Note 4",
        createdAt = 1788443520421,
        text = "Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long TextLong Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long TextLong Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text",
        colorPref = ColorPref.entries.random()
    ),
    TextNote.empty(
        title = "Note 5",
        createdAt = 1788443520422,
        text = "Short Text Short TextShort TextShort TextShort TextShort Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long",
        colorPref = ColorPref.entries.random()
    ),
    TextNote.empty(
        title = "Note 6",
        createdAt = 1788443520423,
        text = "Short Text Short TextShort TextShort TextShort TextShort ",
        colorPref = ColorPref.entries.random()
    ),
    TextNote.empty(
        title = "Note 7",
        createdAt = 1788443520424,
        text = "Short Text Short TextShort TextShort TextShort TextShort ",
        colorPref = ColorPref.entries.random()
    ),
    TextNote.empty(
        title = "Note 8",
        createdAt = 1788443520425,
        text = "Short Text Short TextShort TextShort TextShort TextShort ",
        colorPref = ColorPref.entries.random()
    ),
)

val bigTodoItem = TodoListNote.empty(
    title = "Note 1",
    createdAt = 1788443520438,
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
        ),
        TodoListItem(
            text = "SomeText2",
            selected = false,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = false,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = false,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = false,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = false,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = false,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = true,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = true,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = true,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = true,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = true,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = true,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = true,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = true,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = true,
            position = 3,
            id = UUID.randomUUID().toString()
        ),
        TodoListItem(
            text = "SomeText2",
            selected = true,
            position = 3,
            id = UUID.randomUUID().toString()
        )
    ),
    colorPref = ColorPref.entries.random()
)

val todoNotes = listOf(
    bigTodoItem,
    TodoListNote.empty(
        title = "Note 2",
        createdAt = 1788443520439,
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
        ),
        colorPref = ColorPref.entries.random()
    ),
    TodoListNote.empty(
        title = "Note 3",
        createdAt = 1788443520440,
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
        ),
        colorPref = ColorPref.entries.random()
    )
)