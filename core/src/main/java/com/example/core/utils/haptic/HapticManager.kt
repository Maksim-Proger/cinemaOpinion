package com.example.core.utils.haptic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class HapticManager(context: Context) {

    private val vibrator: Vibrator? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

    fun playLaunchPulse() {
        val device = vibrator ?: return
        if (!device.hasVibrator()) return

        device.vibrate(
            VibrationEffect.createWaveform(
                longArrayOf(0, 20, 110, 45),
                intArrayOf(0, 110, 0, 200),
                -1
            )
        )
    }

}