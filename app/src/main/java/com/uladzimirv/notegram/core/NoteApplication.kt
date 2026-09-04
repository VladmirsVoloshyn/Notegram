package com.uladzimirv.notegram.core

import android.app.Application
import com.uladzimirv.notegram.data.preferences.PreferencesRepository
import com.uladzimirv.notegram.domain.manager.mock.MockManager
import com.uladzimirv.notegram.ui.theme.AppTheme
import com.uladzimirv.notegram.ui.theme.Theme
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NoteApplication : Application() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository
    @Inject
    lateinit var mockManager: MockManager

    override fun onCreate() {
        super.onCreate()
        // mockManager.mock()
        AppTheme.init(preferencesRepository)
    }
}