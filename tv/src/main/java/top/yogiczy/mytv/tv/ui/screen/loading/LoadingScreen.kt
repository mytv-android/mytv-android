package top.yogiczy.mytv.tv.ui.screen.loading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.tv.material3.Icon
import top.yogiczy.mytv.tv.ui.material.CircularProgressIndicator
import top.yogiczy.mytv.tv.ui.material.SnackbarUICustom
import top.yogiczy.mytv.tv.ui.material.SnackbarType
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
    visibleProvider: () -> Boolean = { true },
) {
    val text = remember(mainUiState) {
        when (mainUiState) {
            is MainUiState.Ready -> "就绪"
            is MainUiState.Loading -> mainUiState.message ?: "加载中..."
            is MainUiState.Error -> mainUiState.message ?: "加载失败"
        }
    }
    val snackbarType = remember(mainUiState) {
        when (mainUiState) {
            is MainUiState.Ready -> SnackbarType.DEFAULT
            is MainUiState.Loading -> SnackbarType.DEFAULT
            is MainUiState.Error -> SnackbarType.ERROR
        }
    }
    val icon = Icons.Default.Error
    val isLoading = remember(mainUiState) {
        when (mainUiState) {
            is MainUiState.Ready -> true
            is MainUiState.Loading -> true
            is MainUiState.Error -> false
        }
    }
    SnackbarUICustom(
        modifier = modifier,
        visibleProvider = visibleProvider,
        textProvider = {text},
        positionProvider = {Alignment.BottomEnd},
        showLeadingIconProvider = {true},
        leadingIconProvider = {icon},
        leadingLoadingProvider = {isLoading},
        typeProvider = {snackbarType}
    )
}
