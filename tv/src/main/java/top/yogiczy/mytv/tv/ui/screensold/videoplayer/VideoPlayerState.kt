package top.yogiczy.mytv.tv.ui.screensold.videoplayer

import android.view.SurfaceView
import android.view.TextureView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import top.yogiczy.mytv.core.data.entities.channel.ChannelLine
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.X5VideoPlayer
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.IjkVideoPlayer
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.Media3VideoPlayer
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.VideoPlayer
import top.yogiczy.mytv.tv.ui.utils.Configs

@Stable
class VideoPlayerState(
    val instance: VideoPlayer,
    private var defaultDisplayModeProvider: () -> VideoPlayerDisplayMode = { VideoPlayerDisplayMode.ORIGINAL },
) {
    /** 显示模式 */
    var displayMode by mutableStateOf(defaultDisplayModeProvider())

    /** 视频宽高比 */
    var aspectRatio by mutableFloatStateOf(16f / 9f)

    /** 错误 */
    var error by mutableStateOf<String?>(null)

    /** 正在缓冲 */
    var isBuffering by mutableStateOf(false)

    /** 正在播放 */
    var isPlaying by mutableStateOf(false)

    /** 总时长 */
    var duration by mutableLongStateOf(0L)

    /** 当前播放位置 */
    var currentPosition by mutableLongStateOf(0L)

    /** 当前音量 */
    private var _volume by mutableFloatStateOf(1f)
    var volume: Float
        get() = _volume
        set(value) {
            _volume = value
            instance.setVolume(value)
        }

    /** 元数据 */
    var metadata by mutableStateOf(VideoPlayer.Metadata())

    fun prepare(line: ChannelLine) {
        error = null
        metadata = VideoPlayer.Metadata()
        instance.prepare(line)
    }

    fun play() {
        instance.play()
    }

    fun pause() {
        instance.pause()
    }

    fun seekTo(position: Long) {
        instance.seekTo(position)
    }

    fun stop() {
        instance.stop()
    }

    fun selectVideoTrack(track: VideoPlayer.Metadata.Video?) {
        instance.selectVideoTrack(track)
    }

    fun selectAudioTrack(track: VideoPlayer.Metadata.Audio?) {
        instance.selectAudioTrack(track)
    }

    fun selectSubtitleTrack(track: VideoPlayer.Metadata.Subtitle?) {
        instance.selectSubtitleTrack(track)
    }

    fun setVideoSurfaceView(surfaceView: SurfaceView) {
        instance.setVideoSurfaceView(surfaceView)
    }

    fun setVideoTextureView(textureView: TextureView) {
        instance.setVideoTextureView(textureView)
    }

    private val onReadyListeners = mutableListOf<() -> Unit>()
    private val onErrorListeners = mutableListOf<() -> Unit>()
    private val onInterruptListeners = mutableListOf<() -> Unit>()
    private val onIsBufferingListeners = mutableListOf<(Boolean) -> Unit>()

    fun onReady(listener: () -> Unit) {
        onReadyListeners.add(listener)
    }

    fun onError(listener: () -> Unit) {
        onErrorListeners.add(listener)
    }

    fun onInterrupt(listener: () -> Unit) {
        onInterruptListeners.add(listener)
    }

    fun onIsBuffering(listener: (Boolean) -> Unit) {
        onIsBufferingListeners.add(listener)
    }

    fun initialize() {
        instance.initialize()
        instance.onResolution { width, height ->
            if (width > 0 && height > 0) aspectRatio = width.toFloat() / height
        }
        instance.onError { ex ->
            error = ex?.let { "${it.errorCodeName}(${it.errorCode})" }
                ?.apply { onErrorListeners.forEach { it.invoke() } }

        }
        instance.onReady {
            onReadyListeners.forEach { it.invoke() }
            error = null
            displayMode = defaultDisplayModeProvider()
        }
        instance.onBuffering {
            isBuffering = it
            if (it) error = null
            onIsBufferingListeners.forEach { cb -> cb(isBuffering) }
        }
        instance.onPrepared { }
        instance.onIsPlayingChanged { isPlaying = it }
        instance.onDurationChanged { duration = it }
        instance.onCurrentPositionChanged { currentPosition = it }
        instance.onMetadata { metadata = it }
        instance.onInterrupt { onInterruptListeners.forEach { it.invoke() } }
    }

    fun release() {
        onReadyListeners.clear()
        onErrorListeners.clear()
        onInterruptListeners.clear()
        onIsBufferingListeners.clear()
        instance.release()
    }
}

@Composable
fun rememberVideoPlayerState(
    defaultDisplayModeProvider: () -> VideoPlayerDisplayMode = { VideoPlayerDisplayMode.ORIGINAL },
): VideoPlayerState {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    val videoPlayerCore = settingsVM.videoPlayerCore
    val state = remember(videoPlayerCore) {
        val player = when (videoPlayerCore) {
            Configs.VideoPlayerCore.MEDIA3 -> Media3VideoPlayer(context, coroutineScope)
            Configs.VideoPlayerCore.IJK -> IjkVideoPlayer(context, coroutineScope)
            Configs.VideoPlayerCore.X5 -> X5VideoPlayer(context, coroutineScope)
        }

        VideoPlayerState(player, defaultDisplayModeProvider)
    }

    DisposableEffect(videoPlayerCore) {
        state.initialize()
        onDispose { state.release() }
    }

    DisposableEffect(lifecycleOwner, videoPlayerCore) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) state.play()
            else if (event == Lifecycle.Event.ON_STOP) {
                if (!Configs.appPipEnable) state.pause()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    return state
}

enum class VideoPlayerDisplayMode(val value: Int, val label: String) {
    /** 原始 */
    ORIGINAL(0, "原始"),

    /** 填充 */
    FILL(1, "填充"),

    /** 裁剪 */
    CROP(2, "裁剪"),

    /** 4:3 */
    FOUR_THREE(3, "4:3"),

    /** 16:9 */
    SIXTEEN_NINE(4, "16:9"),

    /** 2.35:1 */
    WIDE(5, "2.35:1");

    companion object {
        fun fromValue(value: Int): VideoPlayerDisplayMode {
            return entries.firstOrNull { it.value == value } ?: ORIGINAL
        }
    }
}