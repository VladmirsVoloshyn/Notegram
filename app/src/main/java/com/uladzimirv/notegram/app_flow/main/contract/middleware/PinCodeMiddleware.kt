package com.uladzimirv.notegram.app_flow.main.contract.middleware

import com.uladzimirv.notegram.app_flow.main.contract.ApplicationMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState.PinCodeScreenState

sealed interface PinCodeMiddleware : ApplicationMiddleware {
    override fun reduce(viewState: ApplicationViewState): ApplicationViewState {
        return when (this) {
            is HasPinCode -> viewState.copy(
                settingsScreenState = viewState.settingsScreenState.copy(
                    hasPinCode = hasPinCode
                )
            )

            is SetAttempt -> viewState.copy(
                pinCodeState = viewState.pinCodeState.copy(
                    attempt = attempt
                )
            )
        }
    }

    data class SetAttempt(val attempt: PinCodeScreenState.Attempt) : PinCodeMiddleware

    data class HasPinCode(val hasPinCode: Boolean) : PinCodeMiddleware
}