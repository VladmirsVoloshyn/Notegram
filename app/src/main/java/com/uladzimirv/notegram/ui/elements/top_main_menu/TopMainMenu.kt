package com.uladzimirv.notegram.ui.elements.top_main_menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uladzimirv.notegram.BuildConfig
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.logo.AppLogo
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.AppTheme.borderPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.borderTertiary
import com.uladzimirv.notegram.ui.theme.AppTheme.textSecondary
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple

@Composable
fun TopMainMenu(
    show: Boolean,
    topPadding: Dp,
    openTrashbox: () -> Unit,
    openArchive: () -> Unit,
    openSettings: () -> Unit,
    dismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = show,
        enter = expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(durationMillis = 600)
        ),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(durationMillis = 200)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickableNoRipple(onClick = dismiss)
        ) {
            val shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            Box(
                modifier = Modifier
                    .shadow(elevation = 24.dp, shape = shape)
                    .fillMaxWidth()
                    .clickable {}
                    .wrapContentHeight()
                    .background(
                        color = backgroundSecondary,
                        shape = shape
                    )
                    .border(
                        width = 1.dp,
                        color = borderPrimary,
                        shape = shape
                    )
                    .padding(24.dp)
                    .padding(top = topPadding)

            ) {
                Column {
                    AppLogo(named = true)
                    Gap(16)
                    HorizontalDivider(
                        color = borderTertiary
                    )
                    Gap(8)
                    MainMenuItem(
                        iconResId = R.drawable.ic_archive,
                        titleResId = R.string.s_main_menu_archive
                    ) {
                        openArchive()
                        dismiss()
                    }
                    Gap(8)
                    HorizontalDivider(
                        color = borderTertiary
                    )
                    Gap(8)
                    MainMenuItem(
                        iconResId = R.drawable.ic_delete,
                        titleResId = R.string.s_main_menu_trashbox,
                    ) {
                        openTrashbox()
                        dismiss()
                    }
                    Gap(8)
                    HorizontalDivider(
                        color = borderTertiary
                    )
                    Gap(8)
                    MainMenuItem(
                        iconResId = R.drawable.ic_settings,
                        titleResId = R.string.s_main_menu_settings
                    ) {
                        openSettings()
                        dismiss()
                    }
                    Gap(8)
                    HorizontalDivider(
                        color = borderTertiary
                    )
                    Gap(32)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(
                            R.string.s_build_config_version_name,
                            BuildConfig.VERSION_NAME
                        ),
                        color = textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }


}