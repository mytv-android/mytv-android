package top.yogiczy.mytv.tv.ui.utils

import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSource
import androidx.compose.runtime.Composable
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource

@Composable
fun IptvSource.TagName(): String {
    return if (sourceType == 1) stringResource(R.string.iptv_source_local) 
        else if (sourceType == 2) "XTREAM" 
        else stringResource(R.string.iptv_source_remote)
}

@Composable
fun getHybridWebViewUrlTagName(id: Int): String {
    return when (id) {
        0 -> stringResource(R.string.ui_hybrid_type_webview)
        1 -> stringResource(R.string.ui_hybrid_type_cctv)
        2 -> stringResource(R.string.ui_hybrid_type_yangshipin)
        3 -> stringResource(R.string.ui_hybrid_type_official_site)
        else -> stringResource(R.string.ui_hybrid_type_unknown)
    }
}