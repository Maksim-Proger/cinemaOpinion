package com.example.core.utils.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.core.R

class SoundManager(context: Context) {
    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(5)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private var clickSoundId: Int = 0
    private var isSoundLoaded = false

    init {
        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) isSoundLoaded = true
        }
        clickSoundId = soundPool.load(context, R.raw.click_sound, 1)
    }

    fun playClick() {
        if (isSoundLoaded) {  // ← играем только когда загружен
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun release() {
        soundPool.release()
    }
}