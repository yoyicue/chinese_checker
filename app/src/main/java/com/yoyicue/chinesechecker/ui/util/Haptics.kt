package com.yoyicue.chinesechecker.ui.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback

enum class HapticKind { Light, Medium, Heavy, Success }

@Composable
fun rememberHaptic(): (HapticKind) -> Unit {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    return remember(context, haptic) {
        { kind: HapticKind ->
            // Try Compose haptic first
            runCatching {
                val type = when (kind) {
                    HapticKind.Light -> HapticFeedbackType.TextHandleMove
                    HapticKind.Medium -> HapticFeedbackType.LongPress
                    HapticKind.Heavy -> HapticFeedbackType.LongPress
                    HapticKind.Success -> HapticFeedbackType.LongPress
                }
                haptic.performHapticFeedback(type)
            }
            // Fallback to Vibrator API
            runCatching {
                val vib: Vibrator? = if (Build.VERSION.SDK_INT >= 31) {
                    val vm = context.getSystemService(VibratorManager::class.java)
                    vm?.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                }
                if (vib != null && vib.hasVibrator()) {
                    val duration = when (kind) {
                        HapticKind.Light -> 25L
                        HapticKind.Medium -> 45L
                        HapticKind.Heavy -> 60L
                        HapticKind.Success -> 80L
                    }
                    val amplitude = when (kind) {
                        HapticKind.Light -> 120
                        HapticKind.Medium -> 180
                        HapticKind.Heavy -> 255
                        HapticKind.Success -> 220
                    }
                    if (Build.VERSION.SDK_INT >= 26) {
                        vib.vibrate(VibrationEffect.createOneShot(duration, amplitude))
                    } else {
                        @Suppress("DEPRECATION")
                        vib.vibrate(duration)
                    }
                }
            }
        }
    }
}
