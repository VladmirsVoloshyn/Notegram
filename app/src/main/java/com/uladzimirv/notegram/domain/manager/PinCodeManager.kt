package com.uladzimirv.notegram.domain.manager

import com.uladzimirv.notegram.data.preferences.PreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PinCodeManager @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    fun comparePinCode(pin: String): Boolean {
        return pin == preferencesRepository.pinCode.value
    }

    fun setPinCode(newPin: String) {
        preferencesRepository.pinCode.value = newPin
    }

    fun hasPinCode() = preferencesRepository.pinCode.value.isNotEmpty()
}