package top.yogiczy.mytv.core.data

import android.content.Context
import android.os.Build
import android.provider.Settings
import top.yogiczy.mytv.core.data.utils.Globals
import top.yogiczy.mytv.core.data.utils.SP

object AppData {
    fun init(context: Context) {
        Globals.cacheDir = context.cacheDir
        Globals.fileDir = context.filesDir
        Globals.resources = context.resources
        Globals.deviceName = runCatching {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
                Settings.Secure.getString(context.contentResolver, "bluetooth_name")!!
            } else {
                Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME)!!
            }
        }
            .recover {
                val manufacturer = Build.MANUFACTURER!!
                val model = Build.MODEL!!
                "$manufacturer ${model.removePrefix(manufacturer)}"
            }
            .getOrDefault("未知设备")

        SP.init(context)
    }
}