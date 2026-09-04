package com.uladzimirv.notegram.ui.layout.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uladzimirv.notegram.R
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationIntent
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.data.preferences.PreferencesRepository
import com.uladzimirv.notegram.ui.elements.AppBottomSheet
import com.uladzimirv.notegram.ui.elements.Gap
import com.uladzimirv.notegram.ui.elements.top_bar.SubScreenTopBar
import com.uladzimirv.notegram.ui.layout.pin_code.PinCodeScreen
import com.uladzimirv.notegram.ui.theme.AppTheme.backgroundSecondary
import com.uladzimirv.notegram.ui.theme.AppTheme.buttonPrimary
import com.uladzimirv.notegram.ui.theme.AppTheme.textPrimary
import com.uladzimirv.notegram.util.compsoe.clickableNoRipple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: ApplicationViewState.SettingsScreenState,
    pinState: ApplicationViewState.PinCodeScreenState,
    intent: (ApplicationIntent) -> Unit
) {
    val sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    AppBottomSheet(
        backgroundColor = backgroundSecondary,
        sheetState = sheetState,
        showBottomSheet = state.show,
        onDismissRequest = {
            intent(ApplicationIntent.TopMenuIntent.OpenSettings(false))
        },
        sheetGesturesEnabled = false,
    ) {
        key(state.theme) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(backgroundSecondary)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        val scope = rememberCoroutineScope()
                        SubScreenTopBar(
                            iconResId = R.drawable.ic_settings,
                            titleResId = R.string.s_main_menu_settings
                        ) {
                            scope.launch {
                                sheetState.hide()
                                delay(100.milliseconds)
                                intent(ApplicationIntent.TopMenuIntent.OpenSettings(false))
                            }
                        }
                        Gap(12)
                        AppThemeSelector(
                            theme = state.theme
                        ) {
                            intent(ApplicationIntent.SettingsIntent.ChangeTheme(it))
                        }
                        Gap(16)
                        PrivacySection(
                            hasPinCode = state.hasPinCode,
                            deletePinCode = {
                                intent(
                                    ApplicationIntent.SettingsIntent.ShowPinCode(
                                        show = true,
                                        purpose = ApplicationViewState.PinCodeScreenState.PinCodePurpose.DeletePinCode
                                    )
                                )
                            },
                            createPin = {
                                intent(
                                    ApplicationIntent.SettingsIntent.ShowPinCode(
                                        show = true,
                                        purpose = ApplicationViewState.PinCodeScreenState.PinCodePurpose.CreateNew(
                                            state.hasPinCode
                                        )
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }

        PinCodeScreen(
            state = pinState,
            show = pinState.callPlace == ApplicationViewState.PinCodeScreenState.PinCodeCallPlace.SETTINGS,
            saveNew = { intent(ApplicationIntent.PinCodeIntent.SavePinCode(it)) },
            send = { intent(ApplicationIntent.PinCodeIntent.ProtectedAccess(it)) },
            drop = { intent(ApplicationIntent.PinCodeIntent.DropAttempt) },
            onUnlock = {
                when (pinState.purpose) {
                    is ApplicationViewState.PinCodeScreenState.PinCodePurpose.CreateNew -> {
                        intent(
                            ApplicationIntent.SettingsIntent.ShowPinCode(
                                show = true,
                                purpose = ApplicationViewState.PinCodeScreenState.PinCodePurpose.CreateNew(
                                    state.hasPinCode
                                )
                            )
                        )
                    }

                    ApplicationViewState.PinCodeScreenState.PinCodePurpose.DeletePinCode -> {
                        intent(ApplicationIntent.PinCodeIntent.DeletePinCode)
                    }

                    else -> {}
                }
            },
            onDismissRequest = {
                intent(
                    ApplicationIntent.SettingsIntent.ShowPinCode(
                        show = false,
                        purpose = ApplicationViewState.PinCodeScreenState.PinCodePurpose.Close
                    )
                )
            }
        )
    }
}

@Composable
fun PrivacySection(
    hasPinCode: Boolean,
    createPin: () -> Unit,
    deletePinCode: () -> Unit
) {
    Column {
        SettingItem(
            iconResId = R.drawable.ic_privacy,
            titleResId = R.string.s_settings_privacy
        ) {}
        Gap(12)
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (!hasPinCode) {
                SettingItem(
                    iconResId = R.drawable.ic_lock,
                    titleResId = R.string.s_settings_create_pin_code,
                    onClick = createPin
                )
            } else {
                SettingItem(
                    iconResId = R.drawable.ic_unlock,
                    titleResId = R.string.s_settings_delete_pin_code,
                    onClick = deletePinCode
                )
            }
        }

    }
}

@Composable
fun AppThemeSelector(
    theme: PreferencesRepository.ThemePreference,
    setTheme: (PreferencesRepository.ThemePreference) -> Unit
) {
    Column {
        SettingItem(
            iconResId = R.drawable.ic_theme_palette,
            titleResId = R.string.s_settings_theme
        ) {}
        Gap(18)
        Column() {
            SelectorRow(
                title = R.string.s_settings_theme_light,
                selected = theme == PreferencesRepository.ThemePreference.LIGHT
            ) {
                setTheme(PreferencesRepository.ThemePreference.LIGHT)
            }
            Gap(8)
            SelectorRow(
                title = R.string.s_settings_theme_dark,
                selected = theme == PreferencesRepository.ThemePreference.DARK
            ) {
                setTheme(PreferencesRepository.ThemePreference.DARK)
            }

            Gap(8)
            SelectorRow(
                title = R.string.s_settings_theme_system,
                selected = theme == PreferencesRepository.ThemePreference.SYSTEM
            ) {
                setTheme(PreferencesRepository.ThemePreference.SYSTEM)
            }
        }
    }
}

@Composable
fun SettingItem(
    iconResId: Int,
    titleResId: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableNoRipple(
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = buttonPrimary,
            modifier = Modifier.size(18.dp)
        )
        Gap(6)
        Text(
            text = stringResource(titleResId),
            fontSize = 18.sp,
            color = textPrimary
        )
    }
}

@Composable
fun SelectorRow(
    title: Int,
    selected: Boolean,
    check: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableNoRipple(onClick = check)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val resId = remember(selected) {
            if (selected) R.drawable.ic_check_circle
            else R.drawable.ic_circle
        }
        Icon(
            painter = painterResource(resId),
            contentDescription = null,
            tint = buttonPrimary,
            modifier = Modifier.size(18.dp)
        )
        Gap(6)
        Text(
            text = stringResource(title),
            fontSize = 18.sp,
            color = textPrimary
        )
    }
}