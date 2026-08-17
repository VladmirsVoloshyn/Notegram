package com.uladzimirv.notegram.core.mvi

import android.os.Bundle
import androidx.compose.runtime.Immutable

/**
 * Immutable object which contains all the required information to render a [MviView].
 */
@Immutable
interface MviViewState

/**
 * An interface that converts a [MviViewState] to a [Bundle] and vice versa.
 */
interface MviViewStateSaver<S : MviViewState> {
    fun S.toBundle(): Bundle
    fun restore(bundle: Bundle?): S
}
