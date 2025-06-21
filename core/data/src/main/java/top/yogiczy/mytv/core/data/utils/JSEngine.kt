package top.yogiczy.mytv.core.data.utils

import android.text.TextUtils
import android.util.Base64
import com.whl.quickjs.wrapper.JSCallFunction
import com.whl.quickjs.wrapper.JSArray
import com.whl.quickjs.wrapper.JSObject
import com.whl.quickjs.wrapper.QuickJSContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.LinkedHashMap
import java.util.concurrent.ConcurrentHashMap
import top.yogiczy.mytv.core.data.R
import top.yogiczy.mytv.core.data.repositories.FileCacheRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class JSEngine : Loggable("JSEngine"){
    // val spiderMap: ConcurrentHashMap<String, Spider> = ConcurrentHashMap()
    // val jsStrMap: LinkedHashMap<String, String> = LinkedHashMap()
    
    suspend fun executeJSString(jsCode: String, data: String) : String =
        withContext(Dispatchers.IO) {
            val context = try {
                    QuickJSContext.create()
                } catch (e: Exception) {
                    log.e("QuickJSContext.create() 异常: ${e.message}", e)
                    throw e
                }
            val globalObj = context.getGlobalObject()
            val mytv = context.createNewJSObject()
            mytv.setProperty("fetch", JSCallFunction { args ->
                val url = args.getOrNull(0)?.toString() ?: throw Exception("URL is required")
                var body = ""
                try {
                    val client = okhttp3.OkHttpClient()
                    val request = okhttp3.Request.Builder().url(url).build()
                    val response = client.newCall(request).execute()
                    body = response.body?.string() ?: ""
                    response.close()
                } catch (e: Exception) {
                    throw Exception("Failed to fetch data from $url: ${e.message}", e)
                }
                return@JSCallFunction body
            })
            mytv.setProperty("post", JSCallFunction { args ->
                val url = args.getOrNull(0)?.toString() ?: throw Exception("URL is required")
                val headersArray = args.getOrNull(1) as? JSArray
                val headers: Map<String, String> = if (headersArray != null) {
                    val map = mutableMapOf<String, String>()
                    for (i in 0 until headersArray.length()) {
                        val item = headersArray.get(i) as? String
                        if (item != null && item.contains(":")) {
                            val (key, value) = item.split(":", limit = 2).map { it.trim() }
                            map[key] = value
                        }
                    }
                    map
                } else {
                    emptyMap()
                }
                val body = args.getOrNull(2)?.toString() ?: throw Exception("Body is required")
                var responseBody = ""
                try {
                    val client = okhttp3.OkHttpClient()
                    val requestBody = okhttp3.RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(), body
                    )
                    val request = okhttp3.Request.Builder().url(url).apply {
                        headers.forEach { (key, value) ->
                            addHeader(key, value)
                        }
                    }.post(requestBody).build()
                    val response = client.newCall(request).execute()
                    responseBody = response.body?.string() ?: ""
                    response.close()
                } catch (e: Exception) {
                    throw Exception("Failed to post data to $url: ${e.message}", e)
                }
                return@JSCallFunction responseBody
            })
            mytv.setProperty("get", JSCallFunction { args ->
                val url = args.getOrNull(0)?.toString() ?: throw Exception("URL is required")
                val headersArray = args.getOrNull(1) as? JSArray
                val headers: Map<String, String> = if (headersArray != null) {
                    val map = mutableMapOf<String, String>()
                    for (i in 0 until headersArray.length()) {
                        val item = headersArray.get(i) as? String
                        if (item != null && item.contains(":")) {
                            val (key, value) = item.split(":", limit = 2).map { it.trim() }
                            map[key] = value
                        }
                    }
                    map
                } else {
                    emptyMap()
                }
                var responseBody = ""
                try {
                    val client = okhttp3.OkHttpClient()
                    val request = okhttp3.Request.Builder().url(url).apply {
                        headers.forEach { (key, value) ->
                            addHeader(key, value)
                        }
                    }.build()
                    val response = client.newCall(request).execute()
                    responseBody = response.body?.string() ?: ""
                    response.close()
                } catch (e: Exception) {
                    throw Exception("Failed to get data from $url: ${e.message}", e)
                }
                return@JSCallFunction responseBody
            })
            mytv.setProperty("fileExists", JSCallFunction { args ->
                val filePath = args.getOrNull(0)?.toString() ?: throw Exception("File path is required")
                return@JSCallFunction kotlinx.coroutines.runBlocking {
                    JSEngineCacheRepository(filePath).exists()   
                }
            })
            mytv.setProperty("readFile", JSCallFunction { args ->
                val filePath = args.getOrNull(0)?.toString() ?: throw Exception("File path is required")
                return@JSCallFunction kotlinx.coroutines.runBlocking {
                    JSEngineCacheRepository(filePath).get()
                }
            })
            mytv.setProperty("writeFile", JSCallFunction { args ->
                val filePath = args.getOrNull(0)?.toString() ?: throw Exception("File path is required")
                val content = args.getOrNull(1)?.toString() ?: throw Exception("Content is required")
                return@JSCallFunction kotlinx.coroutines.runBlocking {
                    JSEngineCacheRepository(filePath).set(content)
                }
            })
            globalObj.setProperty("mytv", mytv)
            val jsStrCrypto = Globals.resources.openRawResource(R.raw.crypto_js).bufferedReader()
            .use { it.readText() }
            val jScode = """
                ${jsStrCrypto}

                ${jsCode}

                (function () {
                    const data = "${data}";
                    return main(data);
                })();
            """.trimIndent()
            val result = runCatching {
                context.evaluate(jScode) as String
            }
            context.destroy()
            if (result.isFailure) {
                log.e("执行JS代码失败: ${result.exceptionOrNull()}")
                throw result.exceptionOrNull() ?: throw Exception("执行JS代码失败")
            }
            mytv.release()
            return@withContext result.getOrNull() ?: ""
        }
    suspend fun executeJSString(jsCode: String) : String =
        withContext(Dispatchers.IO) {
            val context = try {
                QuickJSContext.create()
            } catch (e: Exception) {
                log.e("QuickJSContext.create() 异常: ${e.message}", e)
                throw e
            }
            val result = runCatching {
                context.evaluate(jsCode) as String
            }
            context.destroy()
            if (result.isFailure) {
                log.e("执行JS代码失败: ${result.exceptionOrNull()}")
                throw result.exceptionOrNull() ?: throw Exception("执行JS代码失败")
            }
            return@withContext result.getOrNull() ?: ""
        }
}

class JSEngineCacheRepository(
    private val fileName: String,
    private val isFullPath: Boolean = false,
) : FileCacheRepository(fileName, isFullPath) {

    private val log = Logger.create("JSEngineCacheRepository")

    suspend fun exists(): Boolean = withContext(Dispatchers.IO) {
        return@withContext cacheExists()
    }
    suspend fun get(): String? = withContext(Dispatchers.IO) {
        return@withContext getCacheData()
    }
    suspend fun set(data: String) = withContext(Dispatchers.IO) {
        return@withContext setCacheData(data)
    }
}