package top.yogiczy.mytv.tv.ui.screen.agreement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.LocalTextStyle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.clickableNoIndication
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import top.yogiczy.mytv.tv.ui.utils.gridColumns
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents

@Composable
fun AgreementScreen(
    modifier: Modifier = Modifier,
    onAgree: () -> Unit = {},
    onDisagree: () -> Unit = {},
    onTouchTested: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()

    val messages = listOf(
        "欢迎使用${Constants.APP_TITLE}，请在使用前仔细阅读以下内容：",
        "1. 本软件为开源软件，仅供学习交流使用，禁止用于任何商业用途。",
        "2. 本软件不提供任何直播内容，所有直播内容均来自网络。",
        "3. 本软件完全基于您个人意愿使用，您应该对自己的使用行为和所有结果承担全部责任。",
        "4. 如果本软件存在侵犯您的合法权益的情况，请及时与作者联系，作者将会及时删除有关内容。",
        "如您继续使用本软件即代表您已完全理解并同意上述内容。",
    )

    AppScreen(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = childPadding.top),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("使用须知", style = MaterialTheme.typography.headlineMedium)

            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyLarge
            ) {
                LazyColumn(
                    modifier = Modifier.width(8.gridColumns()),
                    contentPadding = PaddingValues(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(messages) { Text(it) }

                    item {
                        val btnColors = ButtonDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp, bottom = childPadding.bottom),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Button(
                                modifier = Modifier
                                    .focusOnLaunched()
                                    .handleKeyEvents(onSelect = onAgree)
                                    .clickableNoIndication {
                                        onTouchTested()
                                        onAgree()
                                    },
                                colors = btnColors,
                                onClick = { },
                            ) {
                                Text("已阅读并同意")
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Button(
                                modifier = Modifier
                                    .handleKeyEvents(onSelect = onDisagree),
                                colors = btnColors,
                                onClick = { },
                            ) {
                                Text("退出应用")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun AgreementScreenPreview() {
    MyTvTheme {
        AgreementScreen()
        PreviewWithLayoutGrids { }
    }
}