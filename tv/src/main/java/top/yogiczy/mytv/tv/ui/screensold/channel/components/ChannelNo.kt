package top.yogiczy.mytv.tv.ui.screensold.channel.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun ChannelNumber(
    modifier: Modifier = Modifier,
    channelNumberProvider: () -> String = { "" },
) {
    Text(
        modifier = modifier,
        text = channelNumberProvider(),
        style = MaterialTheme.typography.displayMedium,
    )
}

@Preview
@Composable
private fun ChannelNumberPreview() {
    MyTvTheme {
        ChannelNumber(channelNumberProvider = { "01" })
    }
}