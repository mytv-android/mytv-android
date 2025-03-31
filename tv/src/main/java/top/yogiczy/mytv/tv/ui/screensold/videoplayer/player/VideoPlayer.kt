package top.yogiczy.mytv.tv.ui.screensold.videoplayer.player

import android.view.SurfaceView
import android.view.TextureView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yogiczy.mytv.core.data.entities.channel.ChannelLine
import top.yogiczy.mytv.core.util.utils.humanizeAudioChannels
import top.yogiczy.mytv.core.util.utils.humanizeBitrate
import top.yogiczy.mytv.core.util.utils.humanizeLanguage
import top.yogiczy.mytv.tv.ui.utils.Configs
import kotlin.math.roundToInt

abstract class VideoPlayer(
    private val coroutineScope: CoroutineScope,
) {
    protected var metadata = Metadata()

    open fun initialize() {
        clearAllListeners()
    }

    open fun release() {
        clearAllListeners()
    }

    abstract fun prepare(line: ChannelLine)

    abstract fun play()

    abstract fun pause()

    abstract fun seekTo(position: Long)

    abstract fun setVolume(volume: Float)

    abstract fun getVolume(): Float

    open fun stop() {
        loadTimeoutJob?.cancel()
        interruptJob?.cancel()
        currentPosition = 0L
    }

    abstract fun selectVideoTrack(track: Metadata.Video?)

    abstract fun selectAudioTrack(track: Metadata.Audio?)

    abstract fun selectSubtitleTrack(track: Metadata.Subtitle?)

    abstract fun setVideoSurfaceView(surfaceView: SurfaceView)

    abstract fun setVideoTextureView(textureView: TextureView)

    private var loadTimeoutJob: Job? = null
    private var interruptJob: Job? = null
    private var currentPosition = 0L

    private val onResolutionListeners = mutableListOf<(width: Int, height: Int) -> Unit>()
    private val onErrorListeners = mutableListOf<(error: PlaybackException?) -> Unit>()
    private val onReadyListeners = mutableListOf<() -> Unit>()
    private val onBufferingListeners = mutableListOf<(buffering: Boolean) -> Unit>()
    private val onPreparedListeners = mutableListOf<() -> Unit>()
    private val onIsPlayingChanged = mutableListOf<(isPlaying: Boolean) -> Unit>()
    private val onDurationChanged = mutableListOf<(duration: Long) -> Unit>()
    private val onCurrentPositionChanged = mutableListOf<(position: Long) -> Unit>()
    private val onMetadataListeners = mutableListOf<(metadata: Metadata) -> Unit>()
    private val onInterruptListeners = mutableListOf<() -> Unit>()

    private fun clearAllListeners() {
        onResolutionListeners.clear()
        onErrorListeners.clear()
        onReadyListeners.clear()
        onBufferingListeners.clear()
        onPreparedListeners.clear()
        onIsPlayingChanged.clear()
        onDurationChanged.clear()
        onCurrentPositionChanged.clear()
        onMetadataListeners.clear()
        onInterruptListeners.clear()
    }

    protected fun triggerResolution(width: Int, height: Int) {
        onResolutionListeners.forEach { it(width, height) }
    }

    protected fun triggerError(error: PlaybackException?) {
        onErrorListeners.forEach { it(error) }
        if (error != PlaybackException.LOAD_TIMEOUT) {
            loadTimeoutJob?.cancel()
            loadTimeoutJob = null
        }
    }

    protected fun triggerReady() {
        onReadyListeners.forEach { it() }
        loadTimeoutJob?.cancel()
    }

    protected fun triggerBuffering(buffering: Boolean) {
        onBufferingListeners.forEach { it(buffering) }
    }

    protected fun triggerPrepared() {
        onPreparedListeners.forEach { it() }
        loadTimeoutJob?.cancel()
        loadTimeoutJob = coroutineScope.launch {
            delay(Configs.videoPlayerLoadTimeout)
            triggerError(PlaybackException.LOAD_TIMEOUT)
        }
        interruptJob?.cancel()
        interruptJob = null
        metadata = Metadata()
    }

    protected fun triggerIsPlayingChanged(isPlaying: Boolean) {
        onIsPlayingChanged.forEach { it(isPlaying) }
    }

    protected fun triggerDuration(duration: Long) {
        onDurationChanged.forEach { it(duration) }
    }

    protected fun triggerMetadata(metadata: Metadata) {
        onMetadataListeners.forEach { it(metadata) }
    }

    protected fun triggerCurrentPosition(position: Long) {
        if (currentPosition != position) {
            interruptJob?.cancel()
            interruptJob = coroutineScope.launch {
                delay(Configs.videoPlayerLoadTimeout)
                onInterruptListeners.forEach { it() }
            }
        }
        currentPosition = position
        onCurrentPositionChanged.forEach { it(position) }
    }

    fun onResolution(listener: (width: Int, height: Int) -> Unit) {
        onResolutionListeners.add(listener)
    }

    fun onError(listener: (error: PlaybackException?) -> Unit) {
        onErrorListeners.add(listener)
    }

    fun onReady(listener: () -> Unit) {
        onReadyListeners.add(listener)
    }

    fun onBuffering(listener: (buffering: Boolean) -> Unit) {
        onBufferingListeners.add(listener)
    }

    fun onPrepared(listener: () -> Unit) {
        onPreparedListeners.add(listener)
    }

    fun onIsPlayingChanged(listener: (isPlaying: Boolean) -> Unit) {
        onIsPlayingChanged.add(listener)
    }

    fun onDurationChanged(listener: (duration: Long) -> Unit) {
        onDurationChanged.add(listener)
    }

    fun onCurrentPositionChanged(listener: (position: Long) -> Unit) {
        onCurrentPositionChanged.add(listener)
    }

    fun onMetadata(listener: (metadata: Metadata) -> Unit) {
        onMetadataListeners.add(listener)
    }

    fun onInterrupt(listener: () -> Unit) {
        onInterruptListeners.add(listener)
    }

    data class PlaybackException(val errorCodeName: String, val errorCode: Int) :
        Exception(errorCodeName) {
        companion object {
            val UNSUPPORTED_TYPE = PlaybackException("ERROR_UNSUPPORTED_TYPE", 10002)
            val LOAD_TIMEOUT = PlaybackException("ERROR_LOAD_TIMEOUT", 10003)
        }
    }

    /** 元数据 */
    data class Metadata(
        val video: Video? = null,
        val audio: Audio? = null,
        val videoTracks: List<Video> = emptyList(),
        val audioTracks: List<Audio> = emptyList(),
        val subtitleTracks: List<Subtitle> = emptyList(),
    ) {
        data class Video(
            val index: Int? = null,
            val isSelected: Boolean? = null,
            val width: Int? = null,
            val height: Int? = null,
            val color: String? = null,
            val frameRate: Float? = null,
            val bitrate: Int? = null,
            val mimeType: String? = null,
            val decoder: String? = null,
        ) {
            override fun equals(other: Any?): Boolean {
                if (other !is Video) return false

                return width == other.width && height == other.height && frameRate == other.frameRate && bitrate == other.bitrate && mimeType == other.mimeType
            }

            override fun hashCode(): Int {
                var result = width ?: 0
                result = 31 * result + (height ?: 0)
                result = 31 * result + (frameRate?.hashCode() ?: 0)
                result = 31 * result + (bitrate ?: 0)
                result = 31 * result + (mimeType?.hashCode() ?: 0)
                return result
            }

            val shortLabel: String
                get() = listOfNotNull(
                    "${width.toString()}x${height.toString()}",
                    frameRate?.takeIf { it > 0 }?.let { "${it.roundToInt()}fps" },
                    bitrate?.takeIf { nnBitrate -> nnBitrate > 0 }?.humanizeBitrate()
                )
                    .joinToString(", ")
        }

        data class Audio(
            val index: Int? = null,
            val isSelected: Boolean? = null,
            val channels: Int? = null,
            val channelsLabel: String? = null,
            val sampleRate: Int? = null,
            val bitrate: Int? = null,
            val mimeType: String? = null,
            val language: String? = null,
            val decoder: String? = null,
        ) {
            override fun equals(other: Any?): Boolean {
                if (other !is Audio) return false

                return channels == other.channels && sampleRate == other.sampleRate && bitrate == other.bitrate && mimeType == other.mimeType && language == other.language
            }

            override fun hashCode(): Int {
                var result = channels ?: 0
                result = 31 * result + (sampleRate ?: 0)
                result = 31 * result + (bitrate ?: 0)
                result = 31 * result + (mimeType?.hashCode() ?: 0)
                result = 31 * result + (language?.hashCode() ?: 0)
                return result
            }

            val shortLabel: String
                get() = listOfNotNull(
                    channelsLabel ?: channels?.humanizeAudioChannels(),
                    bitrate?.takeIf { nnBitrate -> nnBitrate > 0 }?.humanizeBitrate(),
                    language?.humanizeLanguage(),
                )
                    .joinToString(", ")
        }

        data class Subtitle(
            val index: Int? = null,
            val isSelected: Boolean? = null,
            val bitrate: Int? = null,
            val mimeType: String? = null,
            var language: String? = null,
        ) {
            override fun equals(other: Any?): Boolean {
                if (other !is Subtitle) return false

                return bitrate == other.bitrate && mimeType == other.mimeType && language == other.language
            }

            override fun hashCode(): Int {
                var result = (bitrate ?: 0)
                result = 31 * result + (mimeType?.hashCode() ?: 0)
                result = 31 * result + (language?.hashCode() ?: 0)
                return result
            }

            val shortLabel: String
                get() = listOfNotNull(
                    language?.humanizeLanguage(),
                )
                    .joinToString(", ")
        }
    }
}