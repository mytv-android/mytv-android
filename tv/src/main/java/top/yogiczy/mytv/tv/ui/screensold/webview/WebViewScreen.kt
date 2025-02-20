package top.yogiczy.mytv.tv.ui.screensold.webview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
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
import top.yogiczy.mytv.tv.ui.material.Visibility
import top.yogiczy.mytv.tv.ui.screensold.webview.components.WebViewPlaceholder

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    urlProvider: () -> String = { "https://tv.cctv.com/live/index.shtml" },
    onVideoResolutionChanged: (width: Int, height: Int) -> Unit = { _, _ -> },
) {
    val url = urlProvider()
    var placeholderVisible by remember { mutableStateOf(true) }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .background(Color.Black),
            factory = {
                MyWebView(it).apply {
                    webViewClient = MyClient(
                        onPageStarted = { placeholderVisible = true },
                        onPageFinished = { placeholderVisible = false },
                    )

                    setBackgroundColor(Color.Black.toArgb())
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )

                    settings.javaScriptEnabled = true
                    settings.useWideViewPort = true
                    settings.loadWithOverviewMode = true
                    settings.domStorageEnabled = true
                    settings.databaseEnabled = true
                    settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                    settings.loadsImagesAutomatically = true
                    settings.blockNetworkImage = false
                    settings.userAgentString =
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36 Edg/126.0.0.0"
                    settings.cacheMode = WebSettings.LOAD_DEFAULT
                    settings.javaScriptCanOpenWindowsAutomatically = true
                    settings.setSupportZoom(false)
                    settings.displayZoomControls = false
                    settings.builtInZoomControls = false
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    settings.mediaPlaybackRequiresUserGesture = false

                    isHorizontalScrollBarEnabled = false
                    isVerticalScrollBarEnabled = false
                    isClickable = false
                    isFocusable = false
                    isFocusableInTouchMode = false

                    addJavascriptInterface(
                        MyWebViewInterface(
                            onVideoResolutionChanged = onVideoResolutionChanged,
                        ), "Android"
                    )
                }
            },
            update = { it.loadUrl(url) },
        )

        Visibility({ placeholderVisible }) { WebViewPlaceholder() }
    }
}

class MyClient(
    private val onPageStarted: () -> Unit,
    private val onPageFinished: () -> Unit,
) : WebViewClient() {
    // override fun shouldInterceptRequest(
    //     view: WebView?,
    //     request: WebResourceRequest?
    // ): WebResourceResponse? {
    //     if (request?.url.toString().endsWith(".css"))
    //         return WebResourceResponse("text/css", "UTF-8", null)
    //     return null
    // }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        onPageStarted()
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView, url: String) {
        view.evaluateJavascript(
            """
            ;(async () => {
                function delay(ms) {
                    return new Promise(resolve => setTimeout(resolve, ms));
                }
                
                while(true) {
                  const containerEl = document.querySelector('[id^=vodbox]') || document.querySelector('#player')
                  if(!containerEl) {
                      await delay(100)
                      continue
                  }
                  
                  document.body.style = 'width: 100vw; height: 100vh; margin: 0; min-width: 0; background: #000;'
                  
                  containerEl.style = 'width: 100%; height: 100%;'
                  document.body.append(containerEl)

                  ;[...document.body.children].forEach((el) => {
                    if(el.tagName.toLowerCase() == 'div' && !el.id.startsWith('vodbox') && !el.id.startsWith('player')) {
                        el.remove()
                    }
                  })
                  
                  const mask = document.createElement('div')
                  mask.addEventListener('click', () => {})
                  mask.style = 'width: 100%; height: 100%; position: absolute; top: 0; left: 0; z-index: 1000;'
                  document.body.append(mask)
                  
                  const videoEl = document.querySelector('video')
                  videoEl.volume = 1
                  videoEl.autoplay = true
                  
                  break
                }
                
               await delay(1000)
               const videoEl = document.querySelector('video')
               if(videoEl.paused) videoEl.play()
               
               while(true) {
                 await delay(1000)
                 if(videoEl.videoWidth * videoEl.videoHeight == 0) continue
                 
                 Android.changeVideoResolution(videoEl.videoWidth ,videoEl.videoHeight)
                 break
               }
            })()
        """.trimIndent()
        ) {
            onPageFinished()
        }
    }
}

class MyWebView(context: Context) : WebView(context) {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }
}

class MyWebViewInterface(
    private val onVideoResolutionChanged: (width: Int, height: Int) -> Unit = { _, _ -> },
) {
    @JavascriptInterface
    fun changeVideoResolution(width: Int, height: Int) {
        onVideoResolutionChanged(width, height)
    }
}