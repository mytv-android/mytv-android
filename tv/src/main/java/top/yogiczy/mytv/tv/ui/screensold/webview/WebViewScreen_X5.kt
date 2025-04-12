package top.yogiczy.mytv.tv.ui.screensold.webview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import com.tencent.smtt.sdk.CookieManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import top.yogiczy.mytv.core.data.entities.channel.ChannelLine
import top.yogiczy.mytv.tv.ui.material.Visibility
import top.yogiczy.mytv.tv.ui.screensold.webview.components.WebViewPlaceholder
import top.yogiczy.mytv.core.data.utils.Logger
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen_X5(
    modifier: Modifier = Modifier,
    urlProvider: () -> Pair<String, String> = { Pair("", "") },
    onVideoResolutionChanged: (width: Int, height: Int) -> Unit = { _, _ -> },
) {
    val (url, httpUserAgent) = urlProvider()
    var placeholderVisible by remember { mutableStateOf(true) }
    var placeholderMessage by remember { mutableStateOf("正在加载...") }
    val logger = remember { Logger.create("WebViewScreen_X5") }

    // 处理webview://前缀
    val actualUrl = remember(url) {
        val processedUrl = if (url.startsWith("webview://")) {
            logger.i("检测到webview://前缀，正在处理WebView URL")
            url.substring("webview://".length)
        } else {
            url
        }
        logger.i("WebView加载URL: $processedUrl")
        processedUrl
    }
    
    val onUpdatePlaceholderVisible = { visible: Boolean, message: String ->
        placeholderVisible = visible
        placeholderMessage = message
    }
    var cookies:List<String> = emptyList()
    if (actualUrl.contains("yangshipin.cn")){
        cookies = settingsVM.iptvHybridYangshipinCookie.split(";")
    }
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .background(Color.Black.copy(alpha = 0.5f)),
            factory = {
                MyWebView_X5(it).apply {
                    webViewClient = MyClient_X5(
                        onPageStarted = { 
                            placeholderVisible = true
                            placeholderMessage = "正在加载网页，请稍候..."
                            logger.i("WebView开始加载页面")
                            // placeholderVisible = false
                        },
                        onPageFinished = { 
                            placeholderMessage = "网页页面加载完成，正在初始化..."
                            logger.i("WebView页面加载完成")
                            // placeholderVisible = false
                        },
                    )
                    val cookieManager = CookieManager.getInstance()
                    cookieManager.setAcceptCookie(true)
                    cookieManager.setAcceptThirdPartyCookies(this, true)
                    cookies.forEach { cookie ->
                        cookieManager.setCookie(".yangshipin.cn", cookie.trim())
                    }
                    cookieManager.flush()
                    
                    setBackgroundColor(Color.Black.toArgb())
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )

                    getSettings().setJavaScriptEnabled(true)
                    getSettings().setUseWideViewPort(true)
                    getSettings().setLoadWithOverviewMode(true)
                    getSettings().setDomStorageEnabled(true)
                    getSettings().setLoadsImagesAutomatically(false)
                    getSettings().setBlockNetworkImage(true)
                    getSettings().setUserAgentString(httpUserAgent)
                    getSettings().setCacheMode(WebSettings.LOAD_DEFAULT)
                    getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
                    getSettings().setSupportZoom(false)
                    getSettings().setDisplayZoomControls(false)
                    getSettings().setBuiltInZoomControls(false)
                    getSettings().setMixedContentMode(0)
                    getSettings().setMediaPlaybackRequiresUserGesture(false)
                    // getSettings().isHorizontalScrollBarEnabled =(alse
                    // getSettings().isVerticalScrollBarEnabled (false
                    isClickable = false
                    isFocusable = false
                    isLongClickable = false
                    isFocusableInTouchMode = false
                    getView().setOnLongClickListener { 
                        // 返回 true 禁用长按事件
                        true
                    }
                    getView().setOnClickListener { 
                        // 点击事件处理逻辑（如果需要）
                    }
                    getView().setOnTouchListener { _, _ -> 
                        // 禁用触摸事件
                        true
                    }
                    addJavascriptInterface(
                        MyWebViewInterface(
                            onVideoResolutionChanged = onVideoResolutionChanged,
                            onUpdatePlaceholderVisible = onUpdatePlaceholderVisible,
                        ), "Android"
                    )
                }
            },
            update = { it.loadUrl(actualUrl) },
        )

        Visibility({ placeholderVisible }) {
            WebViewPlaceholder(message = placeholderMessage)
        }
    }
}

class MyClient_X5(
    private val onPageStarted: () -> Unit,
    private val onPageFinished: () -> Unit,
) : WebViewClient() {
    private val logger = Logger.create("WebViewClient")
    
    // override fun shouldInterceptRequest(
    //     view: WebView?,
    //     request: WebResourceRequest?
    // ): WebResourceResponse? {
    //     if (request?.url.toString().endsWith(".css"))
    //         return WebResourceResponse("text/css", "UTF-8", null)
    //     return null
    // }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        logger.i("WebView页面开始加载: $url")
        onPageStarted()
        super.onPageStarted(view, url, favicon)
    }

    fun readAssetFile(context: Context, fileName: String): String {
        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charsets.UTF_8)
    }

    override fun onPageFinished(view: WebView, url: String) {
        onPageFinished()
        val scriptContent = readAssetFile(view.context, "webview_player_impl.js")
        logger.i("注入脚本到WebView")
        view.evaluateJavascript(scriptContent.trimIndent()
        ) {
            logger.i("脚本注入完成")
        }
        logger.i("WebView页面注入完成: $url")
    }
}

class MyWebView_X5(context: Context) : WebView(context) {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }
}
