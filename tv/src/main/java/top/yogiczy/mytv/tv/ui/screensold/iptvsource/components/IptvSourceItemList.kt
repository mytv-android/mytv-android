package top.yogiczy.mytv.tv.ui.screensold.iptvsource.components

import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import kotlin.math.max
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSource
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSourceList
@Composable
fun IptvSourceItemList(
    modifier: Modifier = Modifier,
    iptvSourceListProvider: () -> IptvSourceList = { IptvSourceList() },
    currentIptvSourceProvider: () -> IptvSource = { IptvSource() },
    onSelected: (IptvSource) -> Unit = {},
    onUserAction: () -> Unit = {},
) {
    val iptvSourceList = IptvSourceList(iptvSourceListProvider())

    val listState = rememberLazyListState(max(0, iptvSourceList.indexOf(currentIptvSourceProvider())))

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { _ -> onUserAction() }
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        itemsIndexed(iptvSourceList) { index, iptvSource ->
            IptvSourceItem(
                lineProvider = { iptvSource },
                lineIdxProvider = { index },
                isSelectedProvider = { iptvSource == currentIptvSourceProvider() },
                onSelected = { onSelected(iptvSource) },
            )
        }
    }
}

@Preview
@Composable
private fun IptvSourceItemListPreview() {
    MyTvTheme {
        IptvSourceItemList(
            iptvSourceListProvider = { IptvSourceList.EXAMPLE },
            currentIptvSourceProvider = { IptvSource.EXAMPLE },
        )
    }
}