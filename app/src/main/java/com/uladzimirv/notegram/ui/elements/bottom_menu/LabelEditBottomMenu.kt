package com.uladzimirv.notegram.ui.elements.bottom_menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.domain.model.label.LabelId
import com.uladzimirv.notegram.ui.elements.AppBottomSheet
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.text.HeaderText
import com.uladzimirv.notegram.ui.layout.main.com.ColorPref
import com.uladzimirv.notegram.ui.layout.main.com.LabelColorPref
import com.uladzimirv.notegram.ui.layout.note_ui.TitleEdit
import com.uladzimirv.notegram.ui.model.LabelUI
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.AppTheme.buttonPrimary
import com.uladzimirv.notegram.ui.theme.LabelColorSchema
import com.uladzimirv.notegram.ui.theme.NoteColorSchema
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelEditBottomSheet(
    label: LabelUI?,
    selectColor: (LabelColorPref) -> Unit,
    editName: (String) -> Unit,
    delete: (LabelId) -> Unit,
    dismiss: () -> Unit,
) {

    val sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    AppBottomSheet(
        backgroundColor = backgroundSecondary,
        sheetState = sheetState,
        showBottomSheet = label != null,
        onDismissRequest = {
            dismiss()
        },
        sheetGesturesEnabled = false
    ) {
        val shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = backgroundSecondary,
                    shape = shape
                )
                .padding(24.dp)

        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HeaderText(R.string.s_edit_label)
                    val scope = rememberCoroutineScope()
                    Icon(
                        modifier = Modifier
                            .clickableNoRipple {
                                scope.launch {
                                    sheetState.hide()
                                    delay(100.milliseconds)
                                    dismiss()
                                }
                            }
                            .size(24.dp),
                        tint = buttonPrimary,
                        painter = painterResource(R.drawable.ic_cross),
                        contentDescription = null
                    )
                }
                label?.let {
                    Label(
                        modifier = Modifier.fillMaxWidth(),
                        labelUI = label,
                    )
                }
                TitleEdit(
                    title = label?.name.orEmpty(),
                    colorSchema = NoteColorSchema.fromPref(ColorPref.COMMON),
                    onChange = editName
                )
                Gap(16)
                val common = LabelColorSchema.common()
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = LabelColorSchema.schemasList,
                            key = { it.background.hashCode() }
                        ) { schema ->
                            ColorGridItem(
                                labelColorSchema = schema,
                                selected = label?.colorPref == schema.pref
                            ) {
                                selectColor(schema.pref)
                            }
                        }
                        item {
                            ColorGridItem(
                                labelColorSchema = common,
                                selected = label?.colorPref == common.pref
                            ) {
                                selectColor(common.pref)
                            }
                        }
                    }
                }
                Gap(12)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        modifier = Modifier
                            .clickableNoRipple {
                                delete(label?.id.orEmpty())
                            }
                            .size(24.dp),
                        painter = painterResource(R.drawable.ic_delete),
                        tint = buttonPrimary,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun Label(
    modifier: Modifier = Modifier,
    labelUI: LabelUI
) {
    val colorSchema = LabelColorSchema.fromPref(labelUI.colorPref)
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.align(Alignment.Center)
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
                text = labelUI.name,
                color = colorSchema.textColor,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ColorGridItem(
    labelColorSchema: LabelColorSchema,
    selected: Boolean,
    select: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(color = labelColorSchema.background, shape = CircleShape)
            .border(width = 1.dp, color = labelColorSchema.accent, shape = CircleShape)
            .size(48.dp)
            .clickableNoRipple(onClick = select)
    ) {
        if (selected) {
            Icon(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.Center),
                painter = painterResource(R.drawable.ic_selector),
                contentDescription = null,
                tint = labelColorSchema.accent
            )
        }
    }
}
