package top.yogiczy.mytv.tv.ui.screen.dashboard.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Text
import kotlinx.coroutines.delay
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DashboardTime(modifier: Modifier = Modifier) {
    var timestamp by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            timestamp = System.currentTimeMillis()
        }
    }

    val timeFormat = SimpleDateFormat("MM/dd EEE HH:mm:ss", Locale.getDefault())
    Text(timeFormat.format(timestamp), modifier = modifier)
}

@Preview
@Composable
private fun DashboardTimePreview() {
    MyTvTheme {
        DashboardTime()
    }
}