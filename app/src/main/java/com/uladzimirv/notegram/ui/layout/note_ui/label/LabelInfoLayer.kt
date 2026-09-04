package com.uladzimirv.notegram.ui.layout.note_ui.label

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.domain.model.label.LabelId
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.button.AddItem
import com.uladzimirv.notegram.ui.elements.layer.Layer
import com.uladzimirv.notegram.ui.layout.labels.LabelGridItem
import com.uladzimirv.notegram.ui.model.LabelUI

@Composable
fun LabelInfoLayer(
    label: LabelUI?,
    closeLayer: () -> Unit,
    removeLabel: (LabelId) -> Unit
) {
    Layer(
        interceptTouch = closeLayer,
        layerVisible = label != null
    ) {
        label?.let {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LabelGridItem(
                    modifier = Modifier,
                    label = label,
                    onSelect = {}
                )
                Gap(12)
                AddItem(
                    iconResId = R.drawable.ic_arrow_back,
                    titleResId = R.string.s_remove_label,
                    isVisible = true
                ) {
                    removeLabel(label.id)
                }
            }
        }
    }
}