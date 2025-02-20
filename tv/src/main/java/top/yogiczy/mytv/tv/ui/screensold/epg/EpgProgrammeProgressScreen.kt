package top.yogiczy.mytv.tv.ui.screensold.epg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import top.yogiczy.mytv.core.data.entities.epg.EpgProgramme
import top.yogiczy.mytv.core.data.entities.epg.EpgProgramme.Companion.progress
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun EpgProgrammeProgressScreen(
    modifier: Modifier = Modifier,
    currentEpgProgrammeProvider: () -> EpgProgramme? = { null },
    videoPlayerCurrentPositionProvider: () -> Long = { System.currentTimeMillis() },
) {
    val currentEpgProgramme = currentEpgProgrammeProvider() ?: return
    val videoPlayerCurrentPosition = videoPlayerCurrentPositionProvider()

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .height(3.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(0.8f)),
        ) {
            Box(
                modifier = Modifier
                    .height(3.dp)
                    .fillMaxWidth(currentEpgProgramme.progress(videoPlayerCurrentPosition))
                    .background(MaterialTheme.colorScheme.inverseSurface),
            ) {}
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun EpgProgrammeProgressScreenPreview() {
    MyTvTheme {
        EpgProgrammeProgressScreen(
            currentEpgProgrammeProvider = { EpgProgramme.EXAMPLE },
        )
    }
}