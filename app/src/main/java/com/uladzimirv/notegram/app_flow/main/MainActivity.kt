package com.uladzimirv.notegram.app_flow.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uladzimirv.notegram.app_flow.main.contract.MainIntent
import com.uladzimirv.notegram.ui.layout.add_text.TextScreen
import com.uladzimirv.notegram.ui.layout.main.MainScreen
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


            MainScreen(
                state = viewState.main,
                intent = intent
            )
            TextScreen(
                state = viewState.note,
                intent = intent
            )
        }
    }

}
