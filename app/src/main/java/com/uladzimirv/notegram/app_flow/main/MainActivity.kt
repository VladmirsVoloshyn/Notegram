package com.uladzimirv.notegram.app_flow.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uladzimirv.notegram.app_flow.main.contract.MainIntent
import com.uladzimirv.notegram.data.preferences.PreferencesRepository
import com.uladzimirv.notegram.ui.layout.archive.ArchiveScreen
import com.uladzimirv.notegram.ui.layout.note_ui.NoteScreen
import com.uladzimirv.notegram.ui.layout.main.MainScreen
import com.uladzimirv.notegram.ui.layout.qr_scan.ScanQrScreen
import com.uladzimirv.notegram.ui.layout.settings.SettingsScreen
import com.uladzimirv.notegram.ui.layout.trashbox.TrashboxScreen
import com.uladzimirv.notegram.ui.theme.AppTheme
import com.uladzimirv.notegram.ui.theme.pink
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
                hasDeletion = viewState.deleteState.note != null,
                topMenuOpened = viewState.topMenuState.show,
                intent = intent
            )

            key(viewState.settingsScreenState.theme) {
                MainScreen(
                    state = viewState,
                    intent = intent
                )

                ConfigureSystemBars(
                    isDarkMode = viewState.settingsScreenState.theme == PreferencesRepository.ThemePreference.DARK
                )
            }
            NoteScreen(
                state = viewState.noteState,
                deleteState = viewState.deleteState,
                intent = intent
            )
            ScanQrScreen(
                state = viewState.scannerState,
                intent = intent
            )
            TrashboxScreen(
                state = viewState.trashBoxState,
                deleteState = viewState.deleteState,
                intent = intent
            )
            ArchiveScreen(
                state = viewState.archiveState,
                deleteState = viewState.deleteState,
                intent = intent
            )
            SettingsScreen(
                state = viewState.settingsScreenState,
                pinState = viewState.pinCodeState,
                intent = intent
            )
        }
    }

    @Composable
    fun ConfigureSystemBars(isDarkMode: Boolean) {
        window.isNavigationBarContrastEnforced = false
        val view = window.decorView
        val insetsController = WindowCompat.getInsetsController(window, view)
        insetsController.isAppearanceLightStatusBars = !isDarkMode
    }


    @Composable
    private fun configureBackPress(
        addMenuVisible: Boolean,
        itemMenuVisible: Boolean,
        searchVisible: Boolean,
        hasQRData: Boolean,
        hasDeletion: Boolean,
        topMenuOpened: Boolean,
        intent: (MainIntent) -> Unit,
    ) {
        systemBackCallback?.remove()
        if (addMenuVisible || itemMenuVisible || searchVisible || hasQRData || hasDeletion || topMenuOpened) {
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
