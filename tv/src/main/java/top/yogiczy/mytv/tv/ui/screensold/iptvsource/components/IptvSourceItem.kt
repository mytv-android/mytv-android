package top.yogiczy.mytv.tv.ui.screensold.iptvsource.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.icons.outlined.DeleteOutline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.RadioButton
import androidx.tv.material3.Text
import kotlinx.coroutines.launch
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelList
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSource
import top.yogiczy.mytv.core.data.repositories.iptv.IptvRepository
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.tv.ui.material.CircularProgressIndicator
import top.yogiczy.mytv.tv.ui.material.Drawer
import top.yogiczy.mytv.tv.ui.material.DrawerPosition
import top.yogiczy.mytv.tv.ui.material.LocalPopupManager
import top.yogiczy.mytv.tv.ui.material.SimplePopup
import top.yogiczy.mytv.tv.ui.material.Tag
import top.yogiczy.mytv.tv.ui.material.TagDefaults
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScaffoldHeaderBtn
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.push.PushContent
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import top.yogiczy.mytv.tv.ui.utils.gridColumns
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.TagName
import top.yogiczy.mytv.tv.ui.utils.ifElse
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.IptvSourceDetail
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource

@Composable
fun IptvSourceItem(
    modifier: Modifier = Modifier,
    lineProvider: () -> IptvSource = { IptvSource() },
    lineIdxProvider: () -> Int = { 0 },
    isSelectedProvider: () -> Boolean = { false },
    iptvSourceDetailProvider: () -> IptvSourceDetail = { IptvSourceDetail.Loading },
    onSelected: () -> Unit = {},
) {
    val line = lineProvider()
    val lineIdx = lineIdxProvider()
    val isSelected = isSelectedProvider()
    val iptvSourceDetail = iptvSourceDetailProvider()
    ListItem(
        modifier = modifier
            .ifElse(isSelected, Modifier.focusOnLaunched())
            .handleKeyEvents(onSelect = onSelected),
        selected = false,
        onClick = {},
        headlineContent = { Text(line.name ?: "${stringResource(R.string.ui_channel_view_source)}${lineIdx + 1}", maxLines = 1) },
        overlineContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Tag(
                    line.TagName(),
                    colors = TagDefaults.colors(
                        containerColor = LocalContentColor.current.copy(0.1f)
                    ),
                )

                if (!line.transformJs.isNullOrEmpty()) {
                    Tag(
                        stringResource(R.string.ui_convert_js),
                        colors = TagDefaults.colors(
                            containerColor = LocalContentColor.current.copy(0.1f)
                        ),
                    )
                }
            }
        },
        supportingContent = { 
            Text(line.url, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (iptvSourceDetail is IptvSourceDetail.Ready) {
                Text(
                    stringResource(
                        R.string.ui_iptv_source_info,
                        iptvSourceDetail.channelGroupCount,
                        iptvSourceDetail.channelCount,
                        iptvSourceDetail.lineCount
                    ),
                )
            }
        },
        trailingContent = {
            when (iptvSourceDetail) {
                is IptvSourceDetail.Loading -> CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 3.dp,
                    color = LocalContentColor.current,
                    trackColor = MaterialTheme.colorScheme.surface.copy(0.1f),
                )

                is IptvSourceDetail.Error -> Icon(
                    Icons.Default.Error,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                )
                else -> RadioButton(selected = isSelected, onClick = {})
            }
        },
    )
}
@Preview
@Composable
private fun IptvSourceItemPreview() {
    MyTvTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            IptvSourceItem(
                lineProvider = { IptvSource.EXAMPLE },
                lineIdxProvider = { 0 },
                isSelectedProvider = { true },

            )

            IptvSourceItem(
                lineProvider = { IptvSource.EXAMPLE },
                lineIdxProvider = { 0 },
            )

        }
    }
}