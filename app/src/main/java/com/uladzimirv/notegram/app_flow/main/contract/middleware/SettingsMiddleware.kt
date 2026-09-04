package com.uladzimirv.notegram.app_flow.main.contract.middleware

import com.uladzimirv.notegram.app_flow.main.contract.ApplicationMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState.PinCodeScreenState
import com.uladzimirv.notegram.data.preferences.PreferencesRepository


sealed interface SettingsMiddleware : ApplicationMiddleware {
    override fun reduce(viewState: ApplicationViewState): ApplicationViewState {
        return when (this) {

            is Show -> {
                viewState.copy(
                    settingsScreenState = viewState.settingsScreenState.copy(show = show)
                )
            }

            is Theme -> viewState.copy(
                settingsScreenState = viewState.settingsScreenState.copy(
                    theme = themePreference
                )
            )

            is ShowPinCode -> viewState.copy(
                pinCodeState = viewState.pinCodeState.copy(
                    callPlace = if (show)
                        PinCodeScreenState.PinCodeCallPlace.SETTINGS else
                        PinCodeScreenState.PinCodeCallPlace.NONE,
                    purpose = purpose,
                    attempt = PinCodeScreenState.Attempt.ATTEMPT
                )
            )
        }
    }

    data class Theme(val themePreference: PreferencesRepository.ThemePreference) : SettingsMiddleware

    data class Show(
        val show: Boolean
    ) : SettingsMiddleware

    data class ShowPinCode(
        val show: Boolean,
        val purpose: PinCodeScreenState.PinCodePurpose
    ) : SettingsMiddleware
}