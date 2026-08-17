package com.uladzimirv.notegram.core.mvi

import android.util.Log
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

abstract class AbstractMVIViewModel<Intent : MviIntent, State : MviViewState, Event : MviEvent> :
    MviViewModel<Intent, State, Event>, ViewModel() {


    private val eventChannel = Channel<Event>(Channel.UNLIMITED)
    private val intentMutableFlow = MutableSharedFlow<Intent>(extraBufferCapacity = Int.MAX_VALUE)

    final override val singleEvent: Flow<Event> = eventChannel.receiveAsFlow()


    @MainThread
    final override suspend fun processIntent(intent: Intent) {
        check(intentMutableFlow.tryEmit(intent)) { "Failed to emit intent: $intent" }
    }

    protected fun sendEvent(event: Event) {

        eventChannel.trySend(event)
            .onSuccess { Log.d("sendEvent: ", "event=$event") }
            .onFailure {
                Log.d("Failed to send event", "$event")
            }
            .getOrThrow()
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
    }

    // Send event and access intent flow.

    /**
     * Must be called in [kotlinx.coroutines.Dispatchers.Main.immediate],
     * otherwise it will throw an exception.
     *
     * If you want to send an event from other [kotlinx.coroutines.CoroutineDispatcher],
     * use `withContext(Dispatchers.Main.immediate) { sendEvent(event) }`.
     */

    protected val intentSharedFlow: SharedFlow<Intent> get() = intentMutableFlow

    // Extensions on Flow using viewModelScope.

    /**
     * Share the flow in [viewModelScope],
     * start when the first subscriber arrives,
     * and stop when the last subscriber leaves.
     */
    protected fun <T> Flow<T>.shareWhileSubscribed(): SharedFlow<T> =
        shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    protected fun <T> Flow<T>.stateWithInitialNullWhileSubscribed(): StateFlow<T?> =
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}
