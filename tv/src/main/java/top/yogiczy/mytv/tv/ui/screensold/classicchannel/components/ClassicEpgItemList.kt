package top.yogiczy.mytv.tv.ui.screensold.classicchannel.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import kotlinx.collections.immutable.toPersistentList
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.epg.Epg
import top.yogiczy.mytv.core.data.entities.epg.EpgProgramme
import top.yogiczy.mytv.core.data.entities.epg.EpgProgrammeList
import top.yogiczy.mytv.core.data.entities.epg.EpgProgrammeReserveList
import top.yogiczy.mytv.tv.ui.screensold.epg.components.EpgDayItemList
import top.yogiczy.mytv.tv.ui.screensold.epg.components.EpgProgrammeItemList
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.gridColumns
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ClassicEpgItemList(
    modifier: Modifier = Modifier,
    programmeListModifier: Modifier = Modifier,
    epgProvider: () -> Epg? = { null },
    epgProgrammeReserveListProvider: () -> EpgProgrammeReserveList = { EpgProgrammeReserveList() },
    supportPlaybackProvider: () -> Boolean = { false },
    currentPlaybackEpgProgrammeProvider: () -> EpgProgramme? = { null },
    onEpgProgrammePlayback: (EpgProgramme) -> Unit = {},
    onEpgProgrammeReserve: (EpgProgramme) -> Unit = {},
    onUserAction: () -> Unit = {},
) {
    val epg = epgProvider() ?: return
    val dateFormat = SimpleDateFormat("E MM-dd", Locale.getDefault())
    val programDayGroup = epg.programmeList.groupBy { dateFormat.format(it.startAt) }
    var currentDay by remember { mutableStateOf(dateFormat.format(System.currentTimeMillis())) }


    Row(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface.copy(0.7f))
            .padding(start = 12.dp, end = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        EpgProgrammeItemList(
            modifier = programmeListModifier,
            epgProgrammeListProvider = {
                EpgProgrammeList(programDayGroup.getOrElse(currentDay) { listOf(EpgProgramme.EMPTY) })
            },
            epgProgrammeReserveListProvider = epgProgrammeReserveListProvider,
            supportPlaybackProvider = supportPlaybackProvider,
            currentPlaybackProvider = currentPlaybackEpgProgrammeProvider,
            onPlayback = onEpgProgrammePlayback,
            onReserve = onEpgProgrammeReserve,
            focusOnLive = false,
            onUserAction = onUserAction,
        )

        if (programDayGroup.size > 1) {
            EpgDayItemList(
                modifier = Modifier.width(80.dp),
                dayListProvider = { programDayGroup.keys.toPersistentList() },
                currentDayProvider = { currentDay },
                onDaySelected = { currentDay = it },
                onUserAction = onUserAction,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun ClassicEpgItemListPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            ClassicEpgItemList(
                programmeListModifier = Modifier.width(6.gridColumns()),
                epgProvider = { Epg.example(Channel.EXAMPLE) },
            )
        }
    }
}