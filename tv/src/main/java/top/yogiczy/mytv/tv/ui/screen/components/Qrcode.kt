package top.yogiczy.mytv.tv.ui.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.QrShapes
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.core.util.utils.actionView
import top.yogiczy.mytv.tv.ui.material.PopupHandleableApplication
import top.yogiczy.mytv.tv.ui.material.SimplePopup
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.clickableNoIndication


@Composable
fun Qrcode(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(
        modifier = modifier.background(Color.White, MaterialTheme.shapes.medium)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(10.dp),
            painter = rememberQrCodePainter(
                data = text,
                shapes = QrShapes(
                    ball = QrBallShape.circle(),
                    darkPixel = QrPixelShape.roundCorners(),
                    frame = QrFrameShape.roundCorners(.25f),
                ),
            ),
            contentDescription = text,
        )
    }
}

@Composable
fun QrcodePopup(
    modifier: Modifier = Modifier,
    visibleProvider: () -> Boolean,
    onDismissRequest: (() -> Unit)? = null,
    text: String,
    description: String? = null,
) {
    val context = LocalContext.current

    SimplePopup(
        visibleProvider = visibleProvider,
        onDismissRequest = onDismissRequest,
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Qrcode(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .width(200.dp)
                        .height(200.dp)
                        .clickableNoIndication { context.actionView(text) },
                    text = text,
                )

                Text(text, modifier = Modifier.padding(top = 10.dp))

                description?.let { nnDescription ->
                    Text(nnDescription)
                }
            }
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun QrcodePopupPreview() {
    MyTvTheme {
        PopupHandleableApplication {
            QrcodePopup(
                visibleProvider = { true },
                text = Constants.APP_REPO,
                description = "扫码前往代码仓库",
            )
        }
    }
}