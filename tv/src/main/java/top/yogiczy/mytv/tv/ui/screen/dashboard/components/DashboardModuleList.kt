package top.yogiczy.mytv.tv.ui.screen.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.outlined.ViewCozy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.yogiczy.mytv.tv.ui.material.LazyRow
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.R

@Composable
fun DashboardModuleList(
    modifier: Modifier = Modifier,
    toLiveScreen: () -> Unit = {},
    toChannelsScreen: () -> Unit = {},
    toFavoritesScreen: () -> Unit = {},
    toSearchScreen: () -> Unit = {},
    toMultiViewScreen: () -> Unit = {},
    toPushScreen: () -> Unit = {},
    toSettingsScreen: () -> Unit = {},
    toAboutScreen: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(start = childPadding.start, end = childPadding.end),
    ) { runtime ->
        item {
            DashboardModuleItem(
                modifier = Modifier
                    .focusRequester(runtime.firstItemFocusRequester)
                    .handleKeyEvents(onLeft = { runtime.scrollToLast() }),
                imageVector = Icons.Outlined.Tv,
                title = stringResource(R.string.ui_dashboard_module_live),
                onSelected = toLiveScreen,
            )
        }

        item {
            DashboardModuleItem(
                imageVector = Icons.Outlined.GridView,
                title = stringResource(R.string.ui_dashboard_module_all_channels),
                onSelected = toChannelsScreen,
            )
        }

        item {
            DashboardModuleItem(
                imageVector = Icons.Outlined.FavoriteBorder,
                title = stringResource(R.string.ui_dashboard_module_favorites),
                onSelected = toFavoritesScreen,
            )
        }

        item {
            DashboardModuleItem(
                imageVector = Icons.Outlined.Search,
                title = stringResource(R.string.ui_dashboard_module_search),
                onSelected = toSearchScreen,
            )
        }

        item {
            DashboardModuleItem(
                imageVector = Icons.Outlined.ViewCozy,
                title = stringResource(R.string.ui_dashboard_module_multi_view),
                onSelected = toMultiViewScreen,
            )
        }

        item {
            DashboardModuleItem(
                imageVector = Icons.Outlined.CloudUpload,
                title = stringResource(R.string.ui_dashboard_module_push),
                onSelected = toPushScreen,
            )
        }

        item {
            DashboardModuleItem(
                imageVector = Icons.Outlined.Settings,
                title = stringResource(R.string.ui_dashboard_module_settings),
                onSelected = toSettingsScreen,
            )
        }

        item {
            DashboardModuleItem(
                modifier = Modifier
                    .focusRequester(runtime.lastItemFocusRequester)
                    .handleKeyEvents(onRight = { runtime.scrollToFirst() }),
                imageVector = Icons.Outlined.Info,
                title = stringResource(R.string.ui_dashboard_module_about),
                onSelected = toAboutScreen,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun DashboardModuleListPreview() {
    MyTvTheme {
        AppScreen {
            DashboardModuleList(
                modifier = Modifier.padding(vertical = 20.dp),
            )
        }
        // PreviewWithLayoutGrids { }
    }
}