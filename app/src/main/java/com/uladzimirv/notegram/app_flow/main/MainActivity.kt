package com.uladzimirv.notegram.app_flow.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uladzimirv.notegram.app_flow.main.contract.MainIntent
import com.uladzimirv.notegram.ui.layout.note_ui.NoteScreen
import com.uladzimirv.notegram.ui.layout.main.MainScreen
import com.uladzimirv.notegram.ui.layout.qr_scan.ScanQrScreen
import com.uladzimirv.notegram.util.compsoe.collectInLaunchedEffectWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var systemBackCallback: OnBackPressedCallback? = null

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        systemBackCallback?.remove()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val intentChannel = remember { Channel<MainIntent>(Channel.UNLIMITED) }

            LaunchedEffect(Unit) {
                withContext(Dispatchers.Main.immediate) {
                    intentChannel
                        .consumeAsFlow()
                        .onEach(viewModel::processIntent).collect()
                }
            }

            val intent = remember {
                { intent: MainIntent ->
                    intentChannel.trySend(intent).getOrThrow()
                }
            }

            viewModel.singleEvent.collectInLaunchedEffectWithLifecycle { event ->

            }

            val viewState by viewModel.viewState.collectAsStateWithLifecycle()

            configureBackPress(
                addMenuVisible = viewState.main.isAddMenuOpened,
                itemMenuVisible = viewState.main.selectedNote != null,
                searchVisible = viewState.main.isSearchBarActive,
                hasQRData = viewState.scannerState.qrScannerResult != null,
                intent = intent
            )

            MainScreen(
                state = viewState,
                intent = intent
            )
            NoteScreen(
                state = viewState.noteState,
                intent = intent
            )
            ScanQrScreen(
                state = viewState.scannerState,
                intent = intent
            )
        }
    }


    @Composable
    private fun configureBackPress(
        addMenuVisible: Boolean,
        itemMenuVisible: Boolean,
        searchVisible: Boolean,
        hasQRData: Boolean,
        intent: (MainIntent) -> Unit,
    ) {
        systemBackCallback?.remove()
        if (addMenuVisible || itemMenuVisible || searchVisible || hasQRData) {
            systemBackCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    intent(MainIntent.MainScreenIntent.CloseSheets)
                }
            }
            systemBackCallback?.let { callback ->
                onBackPressedDispatcher.addCallback(callback)
            }
        } else systemBackCallback?.remove()
    }

}
