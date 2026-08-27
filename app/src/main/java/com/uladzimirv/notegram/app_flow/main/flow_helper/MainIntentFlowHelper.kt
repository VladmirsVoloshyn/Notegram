package com.uladzimirv.notegram.app_flow.main.flow_helper

import com.uladzimirv.notegram.app_flow.main.contract.MainIntent
import com.uladzimirv.notegram.app_flow.main.contract.MainMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.MainMiddleware.MainScreenMiddleware.ShowQR
import com.uladzimirv.notegram.app_flow.main.contract.MainViewState
import com.uladzimirv.notegram.domain.model.com.NoteStatus
import com.uladzimirv.notegram.domain.model.note.Note
import com.uladzimirv.notegram.domain.model.note.NoteId
import com.uladzimirv.notegram.util.STRING_EMPTY
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.milliseconds

inline fun Flow<MainIntent>.proceed(
    viewState: MainViewState,
    crossinline query: (String) -> Unit,
    crossinline pinOrUnpin: (Note) -> Unit,
    crossinline delete: (NoteId) -> Unit,
    crossinline moveToTrash: suspend (NoteId) -> Unit,
): Flow<MainMiddleware> {
    return this.filterIsInstance<MainIntent.MainScreenIntent>().flatMapLatest { intent ->
        flow {
            when (intent) {
                is MainIntent.MainScreenIntent.OpenAddMenu -> emit(
                    MainMiddleware.MainScreenMiddleware.OpenAddMenu(
                        open = intent.open
                    )
                )

                is MainIntent.MainScreenIntent.OpenSearchBar -> {
                    emit(
                        MainMiddleware.MainScreenMiddleware.OpenSearchBar(
                            open = intent.open
                        )
                    )
                    if (!intent.open) {
                        query(STRING_EMPTY)
                    } else {
                        emit(MainMiddleware.Stub)
                    }
                }

                is MainIntent.MainScreenIntent.SearchQuery -> {
                    emit(
                        MainMiddleware.MainScreenMiddleware.SearchQuery(
                            query = intent.query
                        )
                    )
                    delay(500.milliseconds)
                    query(intent.query)
                }

                is MainIntent.MainScreenIntent.OpenColorContainer -> {
                    emit(
                        MainMiddleware.MainScreenMiddleware.OpenColorContainer(
                            open = intent.open
                        )
                    )
                }

                is MainIntent.MainScreenIntent.OpenNote -> emit(
                    MainMiddleware.MainScreenMiddleware.ShowNoteBottomSheet(
                        show = true,
                        id = intent.id
                    )
                )

                is MainIntent.MainScreenIntent.PinOrUnpin -> {
                    viewState.main.notes.find { it.id == intent.noteId }?.let {
                        pinOrUnpin(it)
                    }
                    emit(MainMiddleware.Stub)
                }


                is MainIntent.MainScreenIntent.Add -> {
                    emit(
                        MainMiddleware.MainScreenMiddleware.ShowNoteBottomSheet(
                            show = true,
                            addNoteRequest = intent.noteType
                        )
                    )
                }

                is MainIntent.MainScreenIntent.CloseSheets -> {
                    emit(
                        MainMiddleware.CloseSheets
                    )
                }

                is MainIntent.MainScreenIntent.SelectNote -> emit(
                    MainMiddleware.MainScreenMiddleware.SelectNote(
                        id = intent.noteId,
                        itemLayoutInfo = intent.itemLayoutInfo
                    )
                )

                is MainIntent.MainScreenIntent.CloseSelectionMenu -> emit(
                    MainMiddleware.MainScreenMiddleware.CloseMenu
                )

                is MainIntent.MainScreenIntent.Delete -> {
                    emit(MainMiddleware.MainScreenMiddleware.DeleteNote(intent.noteId))
                }

                is MainIntent.MainScreenIntent.ConfirmDelete -> {
                    val note = viewState.deleteState.note
                    if (note?.status is NoteStatus.Deleted) {
                        viewState.deleteState.note?.id?.let { id ->
                            delete(id)
                        }
                    } else {
                        viewState.deleteState.note?.id?.let { id ->
                            moveToTrash(id)
                        }
                    }

                    emit(MainMiddleware.CloseSheets)
                }

                is MainIntent.MainScreenIntent.OpenQRScanner -> {
                    emit(ShowQR(show = intent.open))
                }

                else -> {}
            }
        }
    }
}