package top.yogiczy.mytv.tv.ui.screensold.videoplayer.player

import android.content.Context
import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.SurfaceView
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yogiczy.mytv.core.data.entities.channel.ChannelLine
import top.yogiczy.mytv.core.util.utils.toHeaders
import top.yogiczy.mytv.tv.ui.utils.Configs
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaMeta
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import top.yogiczy.mytv.core.data.utils.Logger
import top.yogiczy.mytv.core.data.utils.Loggable


class IjkVideoPlayer(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
) : VideoPlayer(coroutineScope),
    IMediaPlayer.OnPreparedListener,
    IMediaPlayer.OnVideoSizeChangedListener,
    IMediaPlayer.OnErrorListener {

    private val logger = Logger.create("IjkVideoPlayer")
    private val player by lazy {
        IjkMediaPlayer().apply {
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_timeout", 0)
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1)
            setOption(
                IjkMediaPlayer.OPT_CATEGORY_FORMAT,
                "timeout",
                Configs.videoPlayerLoadTimeout
            )
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L)
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 1024 * 10)
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "fastseek")
        }
    }
    private var cacheSurfaceView: SurfaceView? = null
    private var cacheSurfaceTexture: Surface? = null
    private var updateJob: Job? = null

    private fun setOption() {
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-all-videos", 1)
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-hevc", 1)
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0)
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5)
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1)
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1)
    }

    override fun prepare(line: ChannelLine) {
        player.reset()
        val headers = Configs.videoPlayerHeaders.toHeaders() + mapOf(
            "User-Agent" to (line.httpUserAgent ?: Configs.videoPlayerUserAgent),
            "Referer" to (line.httpReferrer ?: ""),
            "Origin" to (line.httpOrigin ?: ""),
        ).filterValues { it.isNotEmpty() }
        
        // 使用应用内日志系统
        logger.i("播放地址: ${line.playableUrl}")
        logger.i("请求头: $headers")
        
        player.setDataSource(
            line.playableUrl,
            headers
        )
        setOption()
        player.prepareAsync()

        triggerPrepared()
    }

    override fun play() {
        player.start()
    }

    override fun pause() {
        player.pause()
    }

    override fun seekTo(position: Long) {
        player.seekTo(position)
    }

    override fun setVolume(volume: Float) {
    }

    override fun getVolume(): Float {
        return 1f
    }

    override fun stop() {
        player.stop()
        updateJob?.cancel()
        super.stop()
    }

    override fun selectVideoTrack(track: Metadata.Video?) {}

    override fun selectAudioTrack(track: Metadata.Audio?) {}

    override fun selectSubtitleTrack(track: Metadata.Subtitle?) {}

    override fun setVideoSurfaceView(surfaceView: SurfaceView) {
        cacheSurfaceView = surfaceView
        cacheSurfaceTexture?.release()
        cacheSurfaceTexture = null
    }

    override fun setVideoTextureView(textureView: TextureView) {
        cacheSurfaceView = null
        textureView.surfaceTextureListener = object : SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surfaceTexture: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                cacheSurfaceTexture = Surface(surfaceTexture)
                player.setSurface(cacheSurfaceTexture)
            }

            override fun onSurfaceTextureSizeChanged(
                surfaceTexture: SurfaceTexture,
                width: Int,
                height: Int
            ) {
            }

            override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                return true
            }

            override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
            }
        }
    }

    override fun initialize() {
        super.initialize()
        player.setOnPreparedListener(this)
        player.setOnVideoSizeChangedListener(this)
        player.setOnErrorListener(this)
    }

    override fun release() {
        player.setOnPreparedListener(null)
        player.setOnVideoSizeChangedListener(null)
        player.setOnErrorListener(null)
        player.stop()
        player.release()
        cacheSurfaceTexture?.release()
        super.release()
    }

    override fun onPrepared(player: IMediaPlayer) {
        cacheSurfaceView?.let { player.setDisplay(it.holder) }
        cacheSurfaceTexture?.let { player.setSurface(it) }

        val info = player.mediaInfo
        metadata = Metadata(
            video = Metadata.Video(
                width = info.mMeta.mVideoStream?.mWidth,
                height = info.mMeta.mVideoStream?.mHeight,
                frameRate = info.mMeta.mVideoStream?.mFpsNum?.toFloat(),
                bitrate = info.mMeta.mVideoStream?.mBitrate?.toInt(),
                mimeType = info.mMeta.mVideoStream?.mCodecName,
                decoder = info.mVideoDecoderImpl,
            ),

            audio = Metadata.Audio(
                channels = when (info.mMeta.mAudioStream?.mChannelLayout) {
                    IjkMediaMeta.AV_CH_LAYOUT_MONO -> 1
                    IjkMediaMeta.AV_CH_LAYOUT_STEREO,
                    IjkMediaMeta.AV_CH_LAYOUT_2POINT1,
                    IjkMediaMeta.AV_CH_LAYOUT_STEREO_DOWNMIX -> 2

                    IjkMediaMeta.AV_CH_LAYOUT_2_1,
                    IjkMediaMeta.AV_CH_LAYOUT_SURROUND -> 3

                    IjkMediaMeta.AV_CH_LAYOUT_3POINT1,
                    IjkMediaMeta.AV_CH_LAYOUT_4POINT0,
                    IjkMediaMeta.AV_CH_LAYOUT_2_2,
                    IjkMediaMeta.AV_CH_LAYOUT_QUAD -> 4

                    IjkMediaMeta.AV_CH_LAYOUT_4POINT1,
                    IjkMediaMeta.AV_CH_LAYOUT_5POINT0 -> 5

                    IjkMediaMeta.AV_CH_LAYOUT_HEXAGONAL,
                    IjkMediaMeta.AV_CH_LAYOUT_5POINT1,
                    IjkMediaMeta.AV_CH_LAYOUT_6POINT0 -> 6

                    IjkMediaMeta.AV_CH_LAYOUT_6POINT1,
                    IjkMediaMeta.AV_CH_LAYOUT_7POINT0 -> 7

                    IjkMediaMeta.AV_CH_LAYOUT_7POINT1,
                    IjkMediaMeta.AV_CH_LAYOUT_7POINT1_WIDE,
                    IjkMediaMeta.AV_CH_LAYOUT_7POINT1_WIDE_BACK,
                    IjkMediaMeta.AV_CH_LAYOUT_OCTAGONAL -> 8

                    else -> 0
                },
                channelsLabel = when (info.mMeta.mAudioStream?.mChannelLayout) {
                    IjkMediaMeta.AV_CH_LAYOUT_MONO -> "单声道"
                    IjkMediaMeta.AV_CH_LAYOUT_STEREO -> "立体声"
                    IjkMediaMeta.AV_CH_LAYOUT_2POINT1 -> "2.1 声道"
                    IjkMediaMeta.AV_CH_LAYOUT_2_1 -> "立体声"
                    IjkMediaMeta.AV_CH_LAYOUT_SURROUND -> "环绕声"
                    IjkMediaMeta.AV_CH_LAYOUT_3POINT1 -> "3.1 环绕声"
                    IjkMediaMeta.AV_CH_LAYOUT_4POINT0 -> "4.0 四声道"
                    IjkMediaMeta.AV_CH_LAYOUT_4POINT1 -> "4.1 环绕声"
                    IjkMediaMeta.AV_CH_LAYOUT_2_2 -> "四声道"
                    IjkMediaMeta.AV_CH_LAYOUT_QUAD -> "四声道"
                    IjkMediaMeta.AV_CH_LAYOUT_5POINT0 -> "5.0 环绕声"
                    IjkMediaMeta.AV_CH_LAYOUT_5POINT1 -> "5.1 环绕声"
                    IjkMediaMeta.AV_CH_LAYOUT_6POINT0 -> "6.0 环绕声"
                    IjkMediaMeta.AV_CH_LAYOUT_6POINT1 -> "6.1 环绕声"
                    IjkMediaMeta.AV_CH_LAYOUT_7POINT0 -> "7.0 环绕声"
                    IjkMediaMeta.AV_CH_LAYOUT_7POINT1 -> "7.1 环绕声"
                    IjkMediaMeta.AV_CH_LAYOUT_7POINT1_WIDE -> "宽域 7.1 环绕声"
                    IjkMediaMeta.AV_CH_LAYOUT_7POINT1_WIDE_BACK -> "后置 7.1 环绕声"
                    IjkMediaMeta.AV_CH_LAYOUT_HEXAGONAL -> "六角环绕声"
                    IjkMediaMeta.AV_CH_LAYOUT_OCTAGONAL -> "八角环绕声"
                    IjkMediaMeta.AV_CH_LAYOUT_STEREO_DOWNMIX -> "立体声下混音"
                    else -> null
                },
                sampleRate = info.mMeta.mAudioStream?.mSampleRate,
                bitrate = info.mMeta.mAudioStream?.mBitrate?.toInt(),
                mimeType = info.mMeta.mAudioStream?.mCodecName,
                decoder = info.mAudioDecoderImpl,
            ),
        )

        triggerMetadata(metadata)
        triggerReady()
        triggerBuffering(false)
        triggerDuration(player.duration)

        updateJob?.cancel()
        updateJob = coroutineScope.launch {
            while (true) {
                triggerIsPlayingChanged(player.isPlaying)
                triggerCurrentPosition(player.currentPosition)
                delay(500)
            }
        }
    }

    override fun onError(player: IMediaPlayer, what: Int, extra: Int): Boolean {
        triggerError(PlaybackException("IJK_ERROR_WHAT_$what", extra))
        return true
    }

    override fun onVideoSizeChanged(
        player: IMediaPlayer,
        width: Int,
        height: Int,
        sarNum: Int,
        sarDen: Int
    ) {
        triggerResolution(width, height)
    }
}