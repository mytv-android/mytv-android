package top.yogiczy.mytv.tv.ui.screensold.datetime

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import kotlinx.coroutines.delay
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.Configs
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DatetimeScreen(
    modifier: Modifier = Modifier,
    showModeProvider: () -> Configs.UiTimeShowMode = { Configs.UiTimeShowMode.HIDDEN },
) {
    val childPadding = rememberChildPadding()

    var timeText by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            val timestamp = System.currentTimeMillis()

            visible = when (showModeProvider()) {
                Configs.UiTimeShowMode.HIDDEN -> false
                Configs.UiTimeShowMode.ALWAYS -> true

                Configs.UiTimeShowMode.EVERY_HOUR -> {
                    timestamp % 3600000 <= (Constants.UI_TIME_SCREEN_SHOW_DURATION + 1000) || timestamp % 3600000 >= 3600000 - Constants.UI_TIME_SCREEN_SHOW_DURATION
                }

                Configs.UiTimeShowMode.HALF_HOUR -> {
                    timestamp % 1800000 <= (Constants.UI_TIME_SCREEN_SHOW_DURATION + 1000) || timestamp % 1800000 >= 1800000 - Constants.UI_TIME_SCREEN_SHOW_DURATION
                }
            }

            if (visible) {
                timeText = when (showModeProvider()) {
                    Configs.UiTimeShowMode.ALWAYS -> SimpleDateFormat("HH:mm", Locale.getDefault())
                    else -> SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                }.format(timestamp)
            }

            delay(1000)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (visible) {
            Text(
                text = timeText,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = childPadding.top, end = childPadding.end),
            )
        }
    }
}


@Preview(device = "id:Android TV (720p)")
@Composable
private fun DatetimeScreenPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            DatetimeScreen(
                showModeProvider = { Configs.UiTimeShowMode.ALWAYS }
            )
        }
    }
}