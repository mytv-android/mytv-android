package top.yogiczy.mytv.core.util.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

object FsUtil {
    /**
     * 递归计算文件夹大小
     */
    fun getDirSizeFlow(dir: File): Flow<Long> = flow {
        if (dir.exists() && dir.isDirectory) {
            val totalSize = calculateDirSize(dir) { size -> emit(size) }
            emit(totalSize)
        } else emit(0L)
    }.flowOn(Dispatchers.IO)

    private suspend fun calculateDirSize(file: File, onFileSize: suspend (Long) -> Unit): Long {
        var totalSize = 0L
        file.listFiles()?.forEach { child ->
            if (child.isFile) {
                val size = child.length()
                totalSize += size
                onFileSize(size)
            } else if (child.isDirectory) {
                totalSize += calculateDirSize(child, onFileSize)
            }
        }
        return totalSize
    }
}