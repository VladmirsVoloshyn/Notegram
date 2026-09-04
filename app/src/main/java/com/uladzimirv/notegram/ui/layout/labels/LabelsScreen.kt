package com.uladzimirv.notegram.ui.layout.labels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationIntent
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.ui.elements.AppBottomSheet
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.bottom_menu.LabelEditBottomSheet
import com.uladzimirv.notegram.ui.elements.button.FloatingButton
import com.uladzimirv.notegram.ui.elements.layer.Layer
import com.uladzimirv.notegram.ui.elements.top_bar.SubScreenTopBar
import com.uladzimirv.notegram.ui.model.LabelUI
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.AppTheme.textPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.textSecondary
import com.uladzimirv.notegram.ui.theme.LabelColorSchema
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelsScreen(
    state: ApplicationViewState.LabelScreenState,
    intent: (ApplicationIntent) -> Unit
) {
    val sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    AppBottomSheet(
        backgroundColor = backgroundPrimary,
        sheetState = sheetState,
        showBottomSheet = state.show,
        onDismissRequest = {
            intent(ApplicationIntent.TopMenuIntent.OpenLabels(false))
            intent(ApplicationIntent.LabelIntent.DropLabel)
        },
        sheetGesturesEnabled = false
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .background(backgroundPrimary)
                    .padding(top = paddingValues.calculateTopPadding())
                    .fillMaxSize()
            ) {
                if (state.labels.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_label_thin),
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = textSecondary
                        )
                        Gap(10)
                        Text(
                            text = stringResource(R.string.s_no_labels),
                            modifier = Modifier,
                            color = textSecondary
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    val scope = rememberCoroutineScope()
                    SubScreenTopBar(
                        iconResId = R.drawable.ic_label_thin,
                        titleResId = R.string.s_labels
                    ) {
                        scope.launch {
                            sheetState.hide()
                            delay(100.milliseconds)
                            intent(ApplicationIntent.TopMenuIntent.OpenLabels(false))
                            intent(ApplicationIntent.LabelIntent.DropLabel)
                        }
                    }
                    Gap(16)

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.labels,
                            key = { it.id }
                        ) { label ->
                            LabelGridItem(
                                modifier = Modifier.animateItem(),
                                label = label
                            ) {
                                intent(ApplicationIntent.LabelIntent.SelectLabel(label.id))
                            }
                        }
                    }
                }


                Layer(
                    contentPadding = PaddingValues(bottom = 70.dp, end = 30.dp)
                ) {
                    FloatingButton(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        isClosed = true,
                    ) {
                        intent(ApplicationIntent.LabelIntent.AddLabel)
                    }
                }
                LabelEditBottomSheet(
                    label = state.label,
                    editName = { intent(ApplicationIntent.LabelIntent.EditName(it)) },
                    selectColor = { intent(ApplicationIntent.LabelIntent.EditColorPref(it)) },
                    delete = { intent(ApplicationIntent.LabelIntent.DeleteLabel(it)) }
                ) {
                    intent(ApplicationIntent.LabelIntent.DropLabel)
                }
            }
        }
    }
}

@Composable
fun LabelGridItem(
    modifier: Modifier,
    label: LabelUI,
    onSelect: () -> Unit
) {
    val colorSchema = LabelColorSchema.fromPref(label.colorPref)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.clickableNoRipple(onClick = onSelect)
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(64.dp),
                painter = painterResource(R.drawable.ic_label_filled),
                contentDescription = null,
                tint = colorSchema.background
            )
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp)
                    .width(40.dp),
                text = label.name,
                color = colorSchema.textColor,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                fontSize = 12.sp
            )
        }
        Text(
            modifier = Modifier,
            text = label.name,
            color = textPrimary,
            maxLines = 3,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }

}