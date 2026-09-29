package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.sin

enum class AmbientSoundType(val title: String, val emoji: String, val description: String) {
    RAIN("Rain & Distant Thunder", "🌧️", "Gentle rhythmic rain drops with subtle low rumble"),
    LOFI_VINYL("Lo-Fi Vinyl Crackle", "☕", "Warm analog hiss, vintage needle pops, and cozy cafe warmth"),
    COSMIC_HUM("Deep Space Drone", "🌌", "Warm meditative binaural frequency (110Hz + 114Hz)"),
    NIGHT_FOREST("Nocturnal Breeze", "🍃", "Subtle wind rustle and calming midnight whisper")
}

class AmbientSoundPlayer {
    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null
    private val sampleRate = 22050
    private val bufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    ).coerceAtLeast(4096)

    @Volatile
    var isPlaying: Boolean = false
        private set

    @Volatile
    var currentSound: AmbientSoundType = AmbientSoundType.RAIN
        private set

    @Volatile
    var volume: Float = 0.5f
        set(value) {
            field = value.coerceIn(0f, 1f)
            audioTrack?.setVolume(field)
        }

    fun play(sound: AmbientSoundType, scope: CoroutineScope) {
        if (isPlaying && currentSound == sound) return
        stop()

        currentSound = sound
        isPlaying = true

        try {
            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack?.setVolume(volume)
            audioTrack?.play()
        } catch (e: Exception) {
            isPlaying = false
            return
        }

        playbackJob = scope.launch(Dispatchers.Default) {
            val buffer = ShortArray(1024)
            val random = Random()
            var phase1 = 0.0
            var phase2 = 0.0
            var filteredNoise = 0.0

            while (isActive && isPlaying) {
                when (currentSound) {
                    AmbientSoundType.RAIN -> {
                        for (i in buffer.indices) {
                            val white = (random.nextDouble() * 2.0 - 1.0)
                            // Low pass filter for soft rain
                            filteredNoise = filteredNoise * 0.85 + white * 0.15
                            // Occasional drop droplet
                            val drop = if (random.nextDouble() < 0.003) (random.nextDouble() * 0.4) else 0.0
                            val sample = (filteredNoise * 0.45 + drop) * Short.MAX_VALUE
                            buffer[i] = sample.coerceIn(Short.MIN_VALUE.toDouble(), Short.MAX_VALUE.toDouble()).toInt().toShort()
                        }
                    }
                    AmbientSoundType.LOFI_VINYL -> {
                        for (i in buffer.indices) {
                            val white = (random.nextDouble() * 2.0 - 1.0)
                            filteredNoise = filteredNoise * 0.6 + white * 0.08
                            // Random crackle pop
                            val pop = if (random.nextDouble() < 0.004) {
                                (random.nextDouble() * 2.0 - 1.0) * 0.5
                            } else 0.0
                            val sample = (filteredNoise + pop) * Short.MAX_VALUE * 0.5
                            buffer[i] = sample.coerceIn(Short.MIN_VALUE.toDouble(), Short.MAX_VALUE.toDouble()).toInt().toShort()
                        }
                    }
                    AmbientSoundType.COSMIC_HUM -> {
                        val freq1 = 108.0
                        val freq2 = 112.0
                        val delta1 = (2.0 * Math.PI * freq1) / sampleRate
                        val delta2 = (2.0 * Math.PI * freq2) / sampleRate
                        for (i in buffer.indices) {
                            val s1 = sin(phase1)
                            val s2 = sin(phase2)
                            phase1 = (phase1 + delta1) % (2.0 * Math.PI)
                            phase2 = (phase2 + delta2) % (2.0 * Math.PI)
                            val sample = ((s1 + s2) * 0.35) * Short.MAX_VALUE
                            buffer[i] = sample.toInt().toShort()
                        }
                    }
                    AmbientSoundType.NIGHT_FOREST -> {
                        for (i in buffer.indices) {
                            val white = (random.nextDouble() * 2.0 - 1.0)
                            filteredNoise = filteredNoise * 0.93 + white * 0.07
                            // cricket chirp modulation
                            val chirp = if ((phase1 > 2.0 && phase1 < 2.3) && (i % 8 == 0)) {
                                sin(phase2 * 4500.0) * 0.12
                            } else 0.0
                            phase1 = (phase1 + 0.0003) % 4.0
                            phase2 += 0.001
                            val sample = (filteredNoise * 0.3 + chirp) * Short.MAX_VALUE
                            buffer[i] = sample.coerceIn(Short.MIN_VALUE.toDouble(), Short.MAX_VALUE.toDouble()).toInt().toShort()
                        }
                    }
                }

                audioTrack?.write(buffer, 0, buffer.size)
            }
        }
    }

    fun stop() {
        isPlaying = false
        playbackJob?.cancel()
        playbackJob = null
        try {
            audioTrack?.pause()
            audioTrack?.flush()
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
    }
}
