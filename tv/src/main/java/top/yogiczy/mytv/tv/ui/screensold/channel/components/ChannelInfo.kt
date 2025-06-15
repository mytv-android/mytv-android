package top.yogiczy.mytv.tv.ui.screensold.channel.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.LocalTextStyle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelLine
import top.yogiczy.mytv.core.data.entities.epg.EpgProgramme
import top.yogiczy.mytv.core.data.entities.epg.EpgProgrammeRecent
import top.yogiczy.mytv.core.data.utils.ChannelUtil
import top.yogiczy.mytv.core.util.utils.isIPv6
import top.yogiczy.mytv.tv.ui.material.Tag
import top.yogiczy.mytv.tv.ui.material.TagDefaults
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.getHybridWebViewUrlTagName
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource

@Composable
fun ChannelInfo(
    modifier: Modifier = Modifier,
    channelProvider: () -> Channel = { Channel() },
    channelLineIdxProvider: () -> Int = { 0 },
    recentEpgProgrammeProvider: () -> EpgProgrammeRecent? = { null },
    isInTimeShiftProvider: () -> Boolean = { false },
    currentPlaybackEpgProgrammeProvider: () -> EpgProgramme? = { null },
) {
    val channel = channelProvider()
    val channelLineIdx = channelLineIdxProvider()
    val line = channel.lineList[channelLineIdx]
    val recentEpgProgramme = recentEpgProgrammeProvider()
    val isInTimeShift = isInTimeShiftProvider()
    val currentPlaybackEpgProgramme = currentPlaybackEpgProgrammeProvider()

    Column(modifier) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = channel.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.alignByBaseline(),
                maxLines = 1,
            )

            Spacer(modifier = Modifier.width(6.dp))

            // FIXME 频道名称过长导致Tag异常
            Row(
                // FIXME 没对齐，临时解决
                modifier = Modifier.padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                val tagColors = TagDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.6f)
                )

                if (isInTimeShift) {
                    Tag(stringResource(R.string.ui_channel_info_time_shift), colors = tagColors)
                }

                if (currentPlaybackEpgProgramme != null) {
                    Tag(stringResource(R.string.ui_channel_info_replay), colors = tagColors)
                }

                if (channel.lineList.size > 1) {
                    Tag("${channelLineIdx + 1}/${channel.lineList.size}", colors = tagColors)
                }

                if (line.hybridType == ChannelLine.HybridType.WebView) {
                    Tag(
                        getHybridWebViewUrlTagName(ChannelUtil.getHybridWebViewUrlProvider(line.url)),
                        colors = tagColors
                    )
                } else {
                    Tag(if (line.url.isIPv6()) "IPV6" else "IPV4", colors = tagColors)
                }
            }
        }

        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodyLarge,
            LocalContentColor provides LocalContentColor.current.copy(alpha = 0.8f),
        ) {
            if (currentPlaybackEpgProgramme == null) {
                Text(stringResource(R.string.ui_channel_info_now_play) + ": ${recentEpgProgramme?.now?.title ?: stringResource(R.string.ui_excellent_program)}", maxLines = 1)
                Text(stringResource(R.string.ui_channel_info_next_play) + ": ${recentEpgProgramme?.next?.title ?: stringResource(R.string.ui_excellent_program)}", maxLines = 1)
            } else {
                Text(stringResource(R.string.ui_channel_info_now_replay) + ": ${currentPlaybackEpgProgramme.title}", maxLines = 1)
            }
        }
    }
}

@Preview
@Composable
private fun ChannelInfoPreview() {
    MyTvTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ChannelInfo(
                channelProvider = { Channel.EXAMPLE },
                channelLineIdxProvider = { 1 },
                recentEpgProgrammeProvider = { EpgProgrammeRecent.EXAMPLE },
                isInTimeShiftProvider = { true },
            )

            ChannelInfo(
                channelProvider = { Channel.EXAMPLE },
                channelLineIdxProvider = { 0 },
                recentEpgProgrammeProvider = { EpgProgrammeRecent.EXAMPLE },
                currentPlaybackEpgProgrammeProvider = { EpgProgramme(title = "回放电视节目") },
            )
        }
    }
}