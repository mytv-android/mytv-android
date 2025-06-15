package top.yogiczy.mytv.tv.ui.screensold.epg.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.DenseListItem
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.entities.epg.EpgProgramme
import top.yogiczy.mytv.core.data.entities.epg.EpgProgramme.Companion.isLive
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunchedSaveable
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.ifElse
import java.text.SimpleDateFormat
import java.util.Locale
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource

@Composable
fun EpgProgrammeItem(
    modifier: Modifier = Modifier,
    epgProgrammeProvider: () -> EpgProgramme = { EpgProgramme() },
    supportPlaybackProvider: () -> Boolean = { false },
    isPlaybackProvider: () -> Boolean = { false },
    hasReservedProvider: () -> Boolean = { false },
    onPlayback: () -> Unit = {},
    onReserve: () -> Unit = {},
    focusOnLive: Boolean = true,
) {
    val programme = epgProgrammeProvider()
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    var isFocused by remember { mutableStateOf(false) }

    DenseListItem(
        modifier = modifier
            .ifElse(programme.isLive() && focusOnLive, Modifier.focusOnLaunchedSaveable())
            .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
            .handleKeyEvents(
                onSelect = {
                    if (programme.endAt < System.currentTimeMillis() && supportPlaybackProvider()) onPlayback()
                    else if (programme.startAt > System.currentTimeMillis()) onReserve()
                }
            ),
        colors = ListItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.inverseSurface.copy(0.1f),
            selectedContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        selected = programme.isLive(),
        onClick = {},
        headlineContent = {
            Text("${timeFormat.format(programme.startAt)}    ${programme.title}", maxLines = if (isFocused) Int.MAX_VALUE else 1)
        },
        trailingContent = {
            if (programme.isLive()) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
            } else if (isPlaybackProvider()) {
                Text(stringResource(R.string.ui_channel_info_now_replay))
            } else if (programme.endAt < System.currentTimeMillis() && supportPlaybackProvider()) {
                Text(stringResource(R.string.ui_channel_info_replay))
            } else if (programme.startAt > System.currentTimeMillis()) {
                if (hasReservedProvider()) Text(stringResource(R.string.ui_channel_info_reserved))
                else Text(stringResource(R.string.ui_channel_info_reserve))
            }
        },
        supportingContent = {
            Text(programme.description, maxLines = if (isFocused) Int.MAX_VALUE else 1)
        }
    )
}

@Preview
@Composable
private fun EpgProgrammeItemPreview() {
    MyTvTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            EpgProgrammeItem(
                epgProgrammeProvider = { EpgProgramme.EXAMPLE },
            )
            EpgProgrammeItem(
                epgProgrammeProvider = {
                    EpgProgramme.EXAMPLE.copy(
                        startAt = System.currentTimeMillis() - 200000,
                        endAt = System.currentTimeMillis() - 100000,
                    )
                },
            )
            EpgProgrammeItem(
                epgProgrammeProvider = {
                    EpgProgramme.EXAMPLE.copy(
                        startAt = System.currentTimeMillis() + 100000,
                        endAt = System.currentTimeMillis() + 200000,
                    )
                },
            )
            EpgProgrammeItem(
                epgProgrammeProvider = {
                    EpgProgramme.EXAMPLE.copy(
                        startAt = System.currentTimeMillis() + 100000,
                        endAt = System.currentTimeMillis() + 200000,
                    )
                },
                hasReservedProvider = { true },
            )
        }
    }
}