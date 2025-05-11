package top.yogiczy.mytv.tv.ui.screen.loading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.LocalTextStyle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.tv.material3.Icon
import top.yogiczy.mytv.tv.ui.material.CircularProgressIndicator
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.main.MainUiState
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import top.yogiczy.mytv.tv.ui.utils.gridColumns
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    onShowDialog: () -> Unit = {},
    toDashboardScreen: () -> Unit = {},
) {
    onShowDialog()
    toDashboardScreen()
}

@Composable
fun LoadingBar(
    modifier: Modifier = Modifier,
    mainUiState: MainUiState,
) {
    val childPadding = rememberChildPadding()
    val white = Color(245, 245, 245)
    Card(
        modifier = modifier
            .width(300.dp)
            .height(30.dp)
            .padding(start = childPadding.paddingValues.calculateLeftPadding(LocalLayoutDirection.current)), 
        colors = CardDefaults.colors(
            containerColor = white
        ),
        shape = CardDefaults.shape(
            shape = RoundedCornerShape(15),
        ),
        onClick = {},
    ){
        when (mainUiState) {
            is MainUiState.Ready -> LoadingStateLoading()
            is MainUiState.Loading -> LoadingStateLoading(messageProvider = { mainUiState.message })
            is MainUiState.Error -> LoadingStateError(messageProvider = { mainUiState.message })
        }
    }
}

@Composable
private fun LoadingState(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    messageProvider: () -> String? = { null },
) {

    Box(modifier = modifier
                    .fillMaxSize()
                    .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            title()
            val message = messageProvider()
            if (message != null) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    modifier = Modifier.sizeIn(maxWidth = 2.gridColumns()),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun LoadingStateLoading(
    modifier: Modifier = Modifier,
    messageProvider: () -> String? = { null }
) {
    LoadingState(
        modifier = modifier,
        title = {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = Color.Black,
                trackColor = MaterialTheme.colorScheme.surface.copy(0.1f),
                strokeWidth = 3.dp,
            )
        },
        messageProvider = messageProvider
    )
}

@Composable
private fun LoadingStateError(
    modifier: Modifier = Modifier,
    messageProvider: () -> String? = { null },
) {
    LoadingState(
        modifier = modifier,
        title = { 
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "错误",
                modifier = Modifier.size(22.dp),
                tint = MaterialTheme.colorScheme.error
            )
        },
        messageProvider = messageProvider
    )
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun LoadingStateLoadingPreview() {
    MyTvTheme {
        AppScreen {
            LoadingStateLoading(
                messageProvider = { "获取远程直播源(4/10)".repeat(10) }
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun LoadingStateErrorPreview() {
    MyTvTheme {
        AppScreen {
            LoadingStateError(
                messageProvider = { "获取远程直播源(4/10)".repeat(10) }
            )
        }
    }
}