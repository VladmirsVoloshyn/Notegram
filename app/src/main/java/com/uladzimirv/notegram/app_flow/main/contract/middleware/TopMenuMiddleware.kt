package com.uladzimirv.notegram.app_flow.main.contract.middleware

import com.uladzimirv.notegram.app_flow.main.contract.ApplicationMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState

sealed interface TopMenuMiddleware : ApplicationMiddleware {

    override fun reduce(viewState: ApplicationViewState): ApplicationViewState {
        return when (this) {
            is Show -> viewState.copy(
                topMenuState = viewState.topMenuState.copy(show = show)
            )
        }
    }

    data class Show(
        val show: Boolean
    ) : TopMenuMiddleware
}