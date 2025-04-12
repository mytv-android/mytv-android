package top.yogiczy.mytv.tv.ui.screensold.videoplayer.player

import android.view.SurfaceView
import android.view.TextureView
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import top.yogiczy.mytv.core.data.utils.Logger
import top.yogiczy.mytv.core.data.utils.Loggable
import top.yogiczy.mytv.core.data.entities.channel.ChannelLine

import com.tencent.smtt.sdk.TbsVideo

class X5VideoPlayer(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
) : VideoPlayer(coroutineScope){

    private val logger = Logger.create("X5VideoPlayer")
    
    override fun prepare(line: ChannelLine) {
        if(TbsVideo.canUseTbsPlayer(context)){
            TbsVideo.openVideo(context, line.playableUrl)
            triggerPrepared()
        } else {
            logger.i("X5VideoPlayer 当前不可用")
            triggerError(PlaybackException("TBS_ERROR_VIDEOPLAYER_NOTAVAILABLE",10000))
        }
    }
    override fun play() {
        logger.i("play")
    }
    override fun pause() {
        logger.i("pause")
    }

    override fun stop() {
        logger.i("stop")
    }
    override fun seekTo(position: Long) {
        logger.i("seekTo")
    }
    override fun setVolume(volume: Float) {
        logger.i("setVolume")
    }
    override fun getVolume(): Float {
        logger.i("getVolume")
        return 1f
    }
    override fun selectVideoTrack(track: Metadata.Video?){
        
    }

    override fun selectAudioTrack(track: Metadata.Audio?){
        
    }

    override fun selectSubtitleTrack(track: Metadata.Subtitle?){
        
    }

    override fun setVideoSurfaceView(surfaceView: SurfaceView){
        
    }

    override fun setVideoTextureView(textureView: TextureView){
        
    }
}
