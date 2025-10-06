package com.yoyicue.chinesechecker.ui.util

import android.media.ToneGenerator
import android.media.AudioManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

enum class SfxKind { Select, Invalid, Move, Win }

@Composable
fun rememberSfx(): (SfxKind) -> Unit {
    val tone = remember {
        try { ToneGenerator(AudioManager.STREAM_MUSIC, 80) } catch (t: Throwable) { null }
    }
    DisposableEffect(Unit) {
        onDispose { runCatching { tone?.release() } }
    }
    return remember(tone) {
        { kind: SfxKind ->
            val tg = tone
            if (tg != null) {
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
