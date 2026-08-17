package com.uladzimirv.notegram.util.vibration

import android.content.Context
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

fun Context.clickVibrate(
    duration: Long = 40,
    amplitude: Int = 40 // 0-255: Power of vibration
) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    val vibrationEffect = VibrationEffect.createOneShot(duration, amplitude)
    vibrator.vibrate(vibrationEffect)
}

fun Context.tickVibrate(duration: Long = 40) {
    if (Build.VERSION.SDK_INT >= 31) {
        val vibrator = this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val v: VibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);
        CombinedVibration.createParallel(v)
        vibrator.vibrate(CombinedVibration.createParallel(v))
    } else {
        @Suppress("DEPRECATION")
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(duration)
    }
}