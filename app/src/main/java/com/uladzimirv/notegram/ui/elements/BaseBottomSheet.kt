package com.uladzimirv.notegram.ui.elements

import android.text.style.LineBackgroundSpan
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy
import com.uladzimirv.notegram.ui.theme.backgroundPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BaseBottomSheet(
    showBottomSheet: Boolean = true,
    isFullSize: Boolean = true,
    shouldDismissOnBackPress: Boolean = true,
    sheetGesturesEnabled : Boolean = true,
    backgroundColor : Color = backgroundPrimary,
    shape: Shape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp),
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        ),
    onDismissRequest: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.let{
                if (isFullSize) it.fillMaxHeight() else Modifier
            },
            containerColor = backgroundColor,
            onDismissRequest = onDismissRequest,
            shape = shape,
            sheetState = sheetState,
            sheetGesturesEnabled = sheetGesturesEnabled,
            properties = ModalBottomSheetProperties(
                securePolicy = SecureFlagPolicy.SecureOff,
                shouldDismissOnBackPress = shouldDismissOnBackPress
            ),
            dragHandle = null
        ) {
            content()
        }
    }
}