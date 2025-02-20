package top.yogiczy.mytv.tv.ui.screensold.quickop.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents

@Composable
fun QuickOpBtn(
    modifier: Modifier = Modifier,
    title: @Composable RowScope.() -> Unit,
    onSelect: () -> Unit = {},
    onLongSelect: () -> Unit = {},
) {
    Button(
        modifier = modifier
            .handleKeyEvents(onSelect = onSelect, onLongSelect = onLongSelect),
        onClick = {},
        shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.medium),
        content = title,
    )
}

@Preview
@Composable
private fun QuickOpBtnPreview() {
    MyTvTheme {
        QuickOpBtn(title = { Text("Button") })
    }
}