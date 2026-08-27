package com.uladzimirv.notegram.util

import com.uladzimirv.notegram.BuildConfig

const val ONE_DAY_IN_MILLS = 86400000L
const val TEN_DAYS_IN_MILLS = ONE_DAY_IN_MILLS * 10
val TRASHBOX_CLEAR_DELAY = if (BuildConfig.DEBUG) 60000L else TEN_DAYS_IN_MILLS