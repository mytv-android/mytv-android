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
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource

@Composable
fun AgreementScreen(
    modifier: Modifier = Modifier,
    onAgree: () -> Unit = {},
    onDisagree: () -> Unit = {},
    onTouchTested: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()

    val messages = listOf(
        "${stringResource(R.string.ui_welcome_sentence0)}${stringResource(R.string.app_name)}${stringResource(R.string.ui_welcome_sentence1)}",
        "${stringResource(R.string.ui_welcome_sentence2)}",
        "${stringResource(R.string.ui_welcome_sentence3)}",
        "${stringResource(R.string.ui_welcome_sentence4)}",
        "${stringResource(R.string.ui_welcome_sentence5)}",
        "${stringResource(R.string.ui_welcome_sentence6)}",
    )

    AppScreen(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = childPadding.top),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(stringResource(R.string.ui_agreement_title), style = MaterialTheme.typography.headlineMedium)

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
                                Text(stringResource(R.string.ui_agreement_agree))
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Button(
                                modifier = Modifier
                                    .handleKeyEvents(onSelect = onDisagree),
                                colors = btnColors,
                                onClick = { },
                            ) {
                                Text(stringResource(R.string.ui_agreement_disagree))
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