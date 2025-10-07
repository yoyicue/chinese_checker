package com.yoyicue.chinesechecker.ui.util

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlin.math.roundToInt

enum class SfxKind { Select, Invalid, Move, Win }

@Composable
fun rememberSfx(volume: Float): (SfxKind) -> Unit {
    val clamped = volume.coerceIn(0f, 1f)
    val scaledVolume = (clamped * 100f).roundToInt().coerceIn(0, 100)
    val tone = remember(scaledVolume) {
        if (scaledVolume <= 0) null else runCatching {
            ToneGenerator(AudioManager.STREAM_MUSIC, scaledVolume)
        }.getOrNull()
    }
    DisposableEffect(tone) {
        onDispose { runCatching { tone?.release() } }
    }
    return remember(tone, scaledVolume) {
        if (tone == null || scaledVolume <= 0) {
            { _: SfxKind -> }
        } else {
            val tg = tone
            { kind: SfxKind ->
                val (type, dur) = when (kind) {
                    SfxKind.Select -> ToneGenerator.TONE_PROP_BEEP to 40
                    SfxKind.Invalid -> ToneGenerator.TONE_PROP_NACK to 80
                    SfxKind.Move -> ToneGenerator.TONE_PROP_BEEP2 to 100
                    SfxKind.Win -> ToneGenerator.TONE_PROP_ACK to 200
                }
                runCatching { tg.startTone(type, dur) }
            }
        }
    }
}
