package com.uladzimirv.notegram.ui.layout.note_ui.add_label

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.domain.model.label.LabelId
import com.uladzimirv.notegram.ui.elements.AppBottomSheet
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.empty_screen_state.EmptyScreenMessage
import com.uladzimirv.notegram.ui.elements.layer.Layer
import com.uladzimirv.notegram.ui.elements.text.HeaderText
import com.uladzimirv.notegram.ui.elements.top_bar.BottomSheetSecondaryTopBar
import com.uladzimirv.notegram.ui.elements.top_bar.SubScreenTopBar
import com.uladzimirv.notegram.ui.layout.labels.LabelGridItem
import com.uladzimirv.notegram.ui.model.LabelUI
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.AppTheme.buttonPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.textPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.textSecondary
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLabelBottomSheet(
    show: Boolean,
    labels: ImmutableList<LabelUI>,
    addLabel: (LabelId) -> Unit,
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
        sheetGesturesEnabled = true
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .padding(horizontal = 16.dp)
        ) {
            val scope = rememberCoroutineScope()
            BottomSheetSecondaryTopBar(
                titleResId = R.string.s_add_label,
            ) {
                scope.launch {
                    sheetState.hide()
                    delay(100.milliseconds)
                    dismiss()
                }
            }
            Gap(16)
            if (labels.isNotEmpty()) {
                LazyVerticalGrid(
                    modifier = Modifier,
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = labels,
                        key = { it.id }
                    ) { label ->
                        LabelGridItem(
                            modifier = Modifier.animateItem(),
                            label = label
                        ) {
                            addLabel(label.id)
                        }
                    }
                }
            }
        }
        Layer(
            layerVisible = labels.isEmpty(),
            contentAlignment = Alignment.TopCenter,
            contentPadding = PaddingValues(top = 120.dp)
        ) {
            EmptyScreenMessage(
                iconResId = R.drawable.ic_note,
                messageResId = R.string.s_all_labels_added
            )
        }
    }
}