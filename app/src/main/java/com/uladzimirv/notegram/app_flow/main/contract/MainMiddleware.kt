package com.uladzimirv.notegram.app_flow.main.contract

import com.uladzimirv.notegram.core.mvi.MVIMiddleware
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.domain.model.note.Note.Companion.toUIModel
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.domain.model.note.TextNote
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.ItemLayoutInfo
import com.uladzimirv.notegram.util.STRING_EMPTY
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

interface MainMiddleware : MVIMiddleware<MainViewState> {

    interface MainScreenMiddleware : MainMiddleware {

        override fun reduce(viewState: MainViewState): MainViewState {
            return when (this) {
                is OpenAddMenu -> viewState.copy(
                    main = viewState.main.copy(
                        isAddMenuOpened = open
                    )
                )

                is SearchQuery -> viewState.copy(
                    main = viewState.main.copy(
                        query = query
                    )
                )

                is OpenSearchBar -> viewState.copy(
                    main = viewState.main.copy(
                        isSearchBarActive = open,
                        query = STRING_EMPTY
                    )
                )

                is ShowTextNoteBottomSheet -> {
                    viewState.copy(
                        main = viewState.main.copy(
                            isAddMenuOpened = false
                        ),
                        note = viewState.note.copy(
                            colorMenuOpened = false,
                            show = show,
                            note = if (show) {
                                id?.let { lookForID -> viewState.main.notes.find { it.id == lookForID } as? TextNote }
                                    ?: TextNote.empty()
                            } else null
                        )
                    )
                }

                is Notes -> {
                    val substate = viewState.note
                    val current = substate.note?.id
                    viewState.copy(
                        note = substate.copy(
                            show = substate.show,
                            note = notes.find { it.id == current } as? TextNote
                        ),
                        main = viewState.main.copy(
                            notes = notes,
                            uiNotes = notes.map {
                                it.toUIModel()
                            }.toPersistentList()
                        )
                    )
                }

                is SelectNote -> {
                    val note = viewState.main.uiNotes.find { it.id == id }
                    if (note != null) {
                        val info = MainViewState.SelectedNoteInfo(
                            note = note,
                            layoutInfo = itemLayoutInfo
                        )
                        viewState.copy(
                            main = viewState.main.copy(
                                selectedNote = info
                            )
                        )
                    } else viewState
                }

                is CloseMenu -> viewState.copy(
                    main = viewState.main.copy(
                        selectedNote = null
                    )
                )

                is OpenColorContainer -> viewState.copy(
                    note = viewState.note.copy(
                        colorMenuOpened = open
                    )
                )

                else -> viewState
            }
        }

        data class Notes(val notes: ImmutableList<Note>) : MainScreenMiddleware

        data class OpenAddMenu(
            val open: Boolean
        ) : MainScreenMiddleware

        data class OpenColorContainer(val open: Boolean) : MainScreenMiddleware

        data class OpenSearchBar(val open: Boolean) : MainScreenMiddleware
        data class SearchQuery(val query: String) : MainScreenMiddleware

        data class ShowTextNoteBottomSheet(val show: Boolean, val id: NoteId? = null) :
            MainScreenMiddleware

        data class SelectNote(val id: NoteId, val itemLayoutInfo: ItemLayoutInfo) :
            MainScreenMiddleware

        data object CloseMenu : MainScreenMiddleware

    }

    interface NoteScreen : MainMiddleware {
        override fun reduce(viewState: MainViewState): MainViewState {
            return when (this) {
                is EditNoteTitle -> {
                    val note = viewState.note.note
                    viewState.copy(
                        note = viewState.note.copy(
                            note = note?.copy(
                                title = title
                            )
                        )
                    )
                }

                is EditNoteText -> {
                    val note = viewState.note.note
                    viewState.copy(
                        note = viewState.note.copy(
                            note = note?.copy(
                                text = text
                            )
                        )
                    )
                }

                is EditNoteColor -> {
                    val note = viewState.note.note
                    viewState.copy(
                        note = viewState.note.copy(
                            note = note?.copy(
                                colorPref = colorPref
                            )
                        )
                    )
                }

                else -> viewState
            }
        }

        data class EditNoteTitle(val title: String) : NoteScreen
        data class EditNoteText(val text: String) : NoteScreen
        data class EditNoteColor(val colorPref: ColorPref) : NoteScreen

    }

    data object Stub : MainMiddleware {
        override fun reduce(viewState: MainViewState) = viewState
    }

}