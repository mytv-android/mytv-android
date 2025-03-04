package top.yogiczy.mytv.tv.utlis

import android.content.Context
import android.content.Intent
import com.koushikdutta.async.AsyncServer
import com.koushikdutta.async.http.body.JSONObjectBody
import com.koushikdutta.async.http.body.MultipartFormDataBody
import com.koushikdutta.async.http.body.StringBody
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import top.yogiczy.mytv.core.data.entities.epgsource.EpgSource
import top.yogiczy.mytv.core.data.entities.epgsource.EpgSourceList
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSource
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSourceList
import top.yogiczy.mytv.core.data.repositories.epg.EpgRepository
import top.yogiczy.mytv.core.data.repositories.iptv.IptvRepository
import top.yogiczy.mytv.core.data.utils.ChannelAlias
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.core.data.utils.Globals
import top.yogiczy.mytv.core.data.utils.Loggable
import top.yogiczy.mytv.core.data.utils.Logger
import top.yogiczy.mytv.core.util.utils.ApkInstaller
import top.yogiczy.mytv.tv.HttpServerService
import top.yogiczy.mytv.tv.R
import top.yogiczy.mytv.tv.sync.CloudSync
import top.yogiczy.mytv.tv.sync.CloudSyncData
import top.yogiczy.mytv.tv.ui.material.Snackbar
import top.yogiczy.mytv.tv.ui.material.SnackbarType
import top.yogiczy.mytv.tv.ui.utils.Configs
import tv.danmaku.ijk.media.player.BuildConfig
import java.io.File
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException


object HttpServer : Loggable("HttpServer") {
    private const val SERVER_PORT = 10481
    private val uploadedApkFile by lazy {
        File(Globals.cacheDir, "uploaded_apk.apk").apply { deleteOnExit() }
    }

    val serverUrl by lazy { "http://${getLocalIpAddress()}:${SERVER_PORT}" }

    fun start(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val server = AsyncHttpServer()

                server.addAction("OPTIONS", ".+") { _, response ->
                    wrapResponse(response)
                    response.send("ok")
                }

                server.listen(AsyncServer.getDefault(), SERVER_PORT)

                server.get("/") { _, response ->
                    handleRawResource(response, context, "text/html", R.raw.web_push)
                }
                server.get("/web_push_css.css") { _, response ->
                    handleRawResource(response, context, "text/css", R.raw.web_push_css)
                }
                server.get("/web_push_js.js") { _, response ->
                    handleRawResource(response, context, "text/javascript", R.raw.web_push_js)
                }

                server.get("/advance") { _, response ->
                    handleAssets(response, context, "text/html", "remote-configs/index.html")
                }

                server.get("/remote-configs/(.*)") { request, response ->
                    val contentType = when (request.path.split(".").last()) {
                        "css" -> "text/css"
                        "js" -> "text/javascript"
                        "html" -> "text/html"
                        "json" -> "application/json"
                        "svg" -> "image/svg+xml"
                        "png" -> "image/png"
                        else -> "text/plain"
                    }

                    handleAssets(response, context, contentType, request.path.removePrefix("/"))
                }

                server.get("/api/info") { _, response ->
                    handleGetInfo(response)
                }

                server.post("/api/iptv-source/push") { request, response ->
                    handleIptvSourcePush(request, response)
                }

                server.post("/api/epg-source/push") { request, response ->
                    handleEpgSourcePush(request, response)
                }

                server.get("/api/channel-alias") { _, response ->
                    handleGetChannelAlias(response)
                }

                server.post("/api/channel-alias") { request, response ->
                    handleUpdateChannelAlias(request, response)
                }

                server.get("/api/configs") { _, response ->
                    handleConfigsGet(response)
                }

                server.post("/api/configs") { request, response ->
                    handleConfigsPush(request, response)
                }

                server.post("/api/upload/apk") { request, response ->
                    handleUploadApk(request, response, context)
                }

                server.get("/api/cloud-sync/data") { _, response ->
                    handleCloudSyncDataGet(response)
                }

                server.post("/api/cloud-sync/data") { request, response ->
                    handleCloudSyncDataPost(request, response)
                }

                server.get("/api/about") { _, response ->
                    handleAboutGet(response)
                }

                server.get("/api/logs") { _, response ->
                    handleLogsGet(response)
                }

                server.get("/api/file/content") { request, response ->
                    handleFileContentGet(request, response)
                }

                server.post("/api/file/content") { request, response ->
                    handleFileContentPost(request, response)
                }

                server.post("/api/file/content-with-dir") { request, response ->
                    handleFileContentWithDirPost(request, response)
                }

                server.post("/api/upload/allinone") { request, response ->
                    handleUploadAllInOne(request, response)
                }

                log.i("设置服务已启动: $serverUrl")
            } catch (ex: Exception) {
                log.e("设置服务启动失败: ${ex.message}", ex)
                launch(Dispatchers.Main) {
                    Snackbar.show("设置服务启动失败", type = SnackbarType.ERROR)
                }
            }
        }
    }

    private fun wrapResponse(response: AsyncHttpServerResponse) = response.apply {
        headers.set("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS")
        headers.set("Access-Control-Allow-Origin", "*")
        headers.set("Access-Control-Allow-Headers", "Origin, Content-Type, X-Auth-Token")
    }

    private fun responseSuccess(response: AsyncHttpServerResponse) {
        wrapResponse(response).apply {
            setContentType("application/json")
            send("{\"code\": 0}")
        }
    }

    private fun handleRawResource(
        response: AsyncHttpServerResponse,
        context: Context,
        contentType: String,
        id: Int,
    ) {
        wrapResponse(response).apply {
            setContentType(contentType)
            send(context.resources.openRawResource(id).readBytes().decodeToString())
        }
    }

    private fun handleAssets(
        response: AsyncHttpServerResponse,
        context: Context,
        contentType: String,
        filename: String,
    ) {
        wrapResponse(response).apply {
            setContentType(contentType)
            send(context.assets.open(filename).reader().readText())
        }
    }

    private fun handleGetInfo(response: AsyncHttpServerResponse) {
        wrapResponse(response).apply {
            setContentType("application/json")
            send(
                Globals.json.encodeToString(
                    AppInfo(
                        appTitle = Constants.APP_TITLE,
                        appRepo = Constants.APP_REPO,
                        logHistory = Logger.history,
                    )
                )
            )
        }
    }

    private fun handleIptvSourcePush(
        request: AsyncHttpServerRequest,
        response: AsyncHttpServerResponse,
    ) {
        val body = request.getBody<JSONObjectBody>().get()
        val name = body.get("name").toString()
        val type = body.get("type").toString()
        val url = body.get("url").toString()
        val filePath = body.get("filePath").toString()
        val content = body.get("content").toString()

        var newIptvSource: IptvSource? = null

        when (type) {
            "url" -> {
                newIptvSource = IptvSource(name, url)
            }

            "file" -> {
                newIptvSource = IptvSource(name, filePath, true)
            }

            "content" -> {
                val file =
                    File(Globals.fileDir, "iptv_source_local_${System.currentTimeMillis()}.txt")
                file.writeText(content)
                newIptvSource = IptvSource(name, file.path, true)
            }
        }

        newIptvSource?.let {
            Configs.iptvSourceList = IptvSourceList(Configs.iptvSourceList + it)
            Configs.iptvSourceCurrent = it
        }

        responseSuccess(response)
    }

    private fun handleEpgSourcePush(
        request: AsyncHttpServerRequest,
        response: AsyncHttpServerResponse,
    ) {
        val body = request.getBody<JSONObjectBody>().get()
        val name = body.get("name").toString()
        val url = body.get("url").toString()

        EpgSource(name, url).let {
            Configs.epgSourceList = EpgSourceList(Configs.epgSourceList + it)
            Configs.epgSourceCurrent = it
        }

        responseSuccess(response)
    }

    private fun handleGetChannelAlias(response: AsyncHttpServerResponse) {
        wrapResponse(response).apply {
            send(runCatching { ChannelAlias.aliasFile.readText() }.getOrElse { "" })
        }
    }

    private fun handleUpdateChannelAlias(
        request: AsyncHttpServerRequest,
        response: AsyncHttpServerResponse,
    ) {
        val alias = request.getBody<StringBody>().get()

        ChannelAlias.aliasFile.writeText(alias)
        runBlocking {
            IptvRepository.clearAllCache()
            EpgRepository.clearAllCache()
        }

        responseSuccess(response)
    }

    private fun handleConfigsGet(response: AsyncHttpServerResponse) {
        wrapResponse(response).apply {
            setContentType("application/json")
            send(Globals.json.encodeToString(Configs.toPartial()))
        }
    }

    private fun handleConfigsPush(
        request: AsyncHttpServerRequest,
        response: AsyncHttpServerResponse,
    ) {
        val body = request.getBody<JSONObjectBody>().get()
        val configs = Globals.json.decodeFromString<Configs.Partial>(body.toString())
        Configs.fromPartial(configs)

        responseSuccess(response)
    }

    private fun handleUploadApk(
        request: AsyncHttpServerRequest,
        response: AsyncHttpServerResponse,
        context: Context,
    ) {
        val body = request.getBody<MultipartFormDataBody>()

        val os = uploadedApkFile.outputStream()
        val contentLength = request.headers["Content-Length"]?.toLong() ?: 1
        var hasReceived = 0L

        body.setMultipartCallback { part ->
            if (part.isFile) {
                body.setDataCallback { _, bb ->
                    val byteArray = bb.allByteArray
                    hasReceived += byteArray.size
                    Snackbar.show(
                        "正在接收文件: ${(hasReceived * 100f / contentLength).toInt()}%",
                        leadingLoading = true,
                        id = "uploading_apk",
                    )
                    os.write(byteArray)
                }
            }
        }

        body.setEndCallback {
            Snackbar.show("正在准备安装，请稍后...", leadingLoading = true, duration = 10_000)
            body.dataEmitter.close()
            os.flush()
            os.close()
            ApkInstaller.installApk(context, uploadedApkFile.path)
        }

        responseSuccess(response)
    }

    private fun handleUploadAllInOne(
        request: AsyncHttpServerRequest,
        response: AsyncHttpServerResponse,
    ) {
        val body = request.getBody<MultipartFormDataBody>()

        val contentLength = request.headers["Content-Length"]?.toLong() ?: 1
        var hasReceived = 0L

        val allinoneFile = File(Globals.fileDir, "uploads/allinone").apply { parentFile?.mkdirs() }

        body.setMultipartCallback { part ->
            if (part.isFile) {
                with(allinoneFile.outputStream()) {

                    body.setDataCallback { _, bb ->
                        val byteArray = bb.allByteArray
                        hasReceived += byteArray.size
                        Snackbar.show(
                            "正在接收文件: ${(hasReceived * 100f / contentLength).toInt()}%",
                            leadingLoading = true,
                            id = "uploading_file",
                        )
                        write(byteArray)
                    }
                }
            }
        }

        body.setEndCallback {
            body.dataEmitter.close()
            Configs.feiyangAllInOneFilePath = allinoneFile.absolutePath
            Snackbar.show("文件接收完成", id = "uploading_file")
        }

        responseSuccess(response)
    }

    private fun handleCloudSyncDataGet(response: AsyncHttpServerResponse) {
        wrapResponse(response).apply {
            setContentType("application/json")
            send(Globals.json.encodeToString(runBlocking { CloudSync.getData() }))
        }
    }

    private fun handleCloudSyncDataPost(
        request: AsyncHttpServerRequest,
        response: AsyncHttpServerResponse
    ) {
        val body = request.getBody<JSONObjectBody>().get()
        val data = Globals.json.decodeFromString<CloudSyncData>(body.toString())
        runBlocking { data.apply() }

        responseSuccess(response)
    }

    private fun handleAboutGet(response: AsyncHttpServerResponse) {
        wrapResponse(response).apply {
            setContentType("application/json")
            send(
                Globals.json.encodeToString(
                    AppAbout(
                        applicationId = BuildConfig.APPLICATION_ID,
                        flavor = BuildConfig.FLAVOR,
                        buildType = BuildConfig.BUILD_TYPE,
                        versionCode = BuildConfig.VERSION_CODE,
                        versionName = BuildConfig.VERSION_NAME,
                        deviceName = Globals.deviceName,
                        deviceId = Globals.deviceId,
                    )
                )
            )
        }
    }

    private fun handleLogsGet(response: AsyncHttpServerResponse) {
        wrapResponse(response).apply {
            setContentType("application/json")
            send(Globals.json.encodeToString(Logger.history))
        }
    }

    private fun handleFileContentGet(
        request: AsyncHttpServerRequest,
        response: AsyncHttpServerResponse
    ) {
        val path = request.query.getString("path")

        val file = File(path).takeIf { it.exists() } ?: run {
            return response.code(404).send("File not found")
        }

        wrapResponse(response).send(file.readText())
    }

    private fun handleFileContentPost(
        request: AsyncHttpServerRequest,
        response: AsyncHttpServerResponse
    ) {
        val body = request.getBody<JSONObjectBody>().get()
        val path = body.get("path").toString()
        val content = body.get("content").toString()

        val file = File(path)

        if (file.exists()) {
            file.writeText(content)
        } else {
            file.parentFile?.mkdirs()
            file.writeText(content)
        }

        responseSuccess(response)
    }

    private fun handleFileContentWithDirPost(
        request: AsyncHttpServerRequest,
        response: AsyncHttpServerResponse
    ) {
        val body = request.getBody<JSONObjectBody>().get()
        val dir = body.get("dir").toString()
        val filename = body.get("filename").toString()
        val content = body.get("content").toString()

        val file = when (dir) {
            "cache" -> File(Globals.cacheDir, filename)
            "file" -> File(Globals.fileDir, filename)
            else -> return response.code(400).send("Invalid dir")
        }

        if (file.exists()) {
            file.writeText(content)
        } else {
            file.parentFile?.mkdirs()
            file.writeText(content)
        }

        wrapResponse(response).send(file.path)
    }

    private fun getLocalIpAddress(): String {
        val defaultIp = "0.0.0.0"

        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        val ip = inetAddress.hostAddress ?: defaultIp
                        if ((ip.startsWith("192.168.") || ip.startsWith("10."))
                            && !ip.endsWith(".1") && !ip.endsWith(".0")
                        )
                            return ip
                    }
                }
            }
            return defaultIp
        } catch (ex: SocketException) {
            log.e("IP Address: ${ex.message}", ex)
            return defaultIp
        }
    }

    fun startService(context: Context) {
        runCatching {
            context.startService(Intent(context, HttpServerService::class.java))
        }.onFailure { it.printStackTrace() }
    }

    fun stopService(context: Context) {
        runCatching {
            context.stopService(Intent(context, HttpServerService::class.java))
        }.onFailure { it.printStackTrace() }
    }
}

@Serializable
private data class AppInfo(
    val appTitle: String,
    val appRepo: String,
    val logHistory: List<Logger.HistoryItem>,
)

@Serializable
private data class AppAbout(
    val applicationId: String,
    val flavor: String,
    val buildType: String,
    val versionCode: Int,
    val versionName: String,
    val deviceName: String,
    val deviceId: String,
)