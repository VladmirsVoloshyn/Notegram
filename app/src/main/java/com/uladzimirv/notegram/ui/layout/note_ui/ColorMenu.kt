package com.uladzimirv.notegram.ui.layout.note_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.AppBottomSheet
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.bottom_bar.ColorContainer
import com.uladzimirv.notegram.ui.elements.top_bar.BottomSheetSecondaryTopBar
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSelectorBottomSheet(
    show: Boolean,
    selected: ColorPref,
    changeColor: (ColorPref) -> Unit,
    dismiss: () -> Unit
) {
    val sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    AppBottomSheet(
        backgroundColor = backgroundSecondary,
        sheetState = sheetState,
        showBottomSheet = show,
        onDismissRequest = {
            dismiss()
        },
        sheetGesturesEnabled = false
    ) {
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .padding(
                    top = 24.dp
                )
                .padding(
                    horizontal = 24.dp
                )
        ) {
            BottomSheetSecondaryTopBar(
                titleResId = R.string.s_select_color,
            ) {
                scope.launch {
                    sheetState.hide()
                    delay(100.milliseconds)
                    dismiss()
                }
            }
            Gap(16)
            LazyVerticalGrid(
                modifier = Modifier,
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = ColorPref.entries,
                    key = { it.name }
                ) { pref ->
                    ColorContainer(
                        selected = selected == pref,
                        pref = pref
                    ) {
                        changeColor(pref)
                    }
                }
            }
        }
    }
}
