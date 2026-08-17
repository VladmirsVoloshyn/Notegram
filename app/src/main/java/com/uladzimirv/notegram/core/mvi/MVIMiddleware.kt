package com.uladzimirv.notegram.core.mvi

interface MVIMiddleware<T : MviViewState> {
    fun reduce(viewState: T): T
}