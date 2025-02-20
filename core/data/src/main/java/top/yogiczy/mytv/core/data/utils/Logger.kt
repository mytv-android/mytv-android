package top.yogiczy.mytv.core.data.utils

import android.util.Log
import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * 日志工具类
 */
class Logger private constructor(
    private val tag: String
) {
    private fun messageWithDuration(message: String, duration: Duration? = null): String {
        return if (duration != null) "$message \t [$duration]" else message
    }

    fun v(message: String, throwable: Throwable? = null, duration: Duration? = null) {
        val messageWithDuration = messageWithDuration(message, duration)
        Log.v(tag, messageWithDuration, throwable)
        addHistoryItem(HistoryItem(LevelType.DEBUG, tag, messageWithDuration, throwable?.message))
    }

    fun d(message: String, throwable: Throwable? = null, duration: Duration? = null) {
        val messageWithDuration = messageWithDuration(message, duration)
        Log.d(tag, messageWithDuration, throwable)
        addHistoryItem(HistoryItem(LevelType.DEBUG, tag, messageWithDuration, throwable?.message))
    }

    fun i(message: String, throwable: Throwable? = null, duration: Duration? = null) {
        val messageWithDuration = messageWithDuration(message, duration)
        Log.i(tag, messageWithDuration, throwable)
        addHistoryItem(HistoryItem(LevelType.INFO, tag, messageWithDuration, throwable?.message))
    }

    fun w(message: String, throwable: Throwable? = null, duration: Duration? = null) {
        val messageWithDuration = messageWithDuration(message, duration)
        Log.w(tag, messageWithDuration, throwable)
        addHistoryItem(HistoryItem(LevelType.WARN, tag, messageWithDuration, throwable?.message))
    }

    fun e(message: String, throwable: Throwable? = null, duration: Duration? = null) {
        val messageWithDuration = messageWithDuration(message, duration)
        Log.e(tag, messageWithDuration, throwable)
        addHistoryItem(HistoryItem(LevelType.ERROR, tag, messageWithDuration, throwable?.message))
    }

    fun wtf(message: String, throwable: Throwable? = null, duration: Duration? = null) {
        val messageWithDuration = messageWithDuration(message, duration)
        Log.wtf(tag, messageWithDuration, throwable)
        addHistoryItem(HistoryItem(LevelType.ERROR, tag, messageWithDuration, throwable?.message))
    }

    companion object {
        fun create(tag: String) = Logger(tag)

        private var _history = mutableListOf<HistoryItem>()
        val history: List<HistoryItem>
            get() = _history

        fun addHistoryItem(item: HistoryItem) {
            if (listOf(LevelType.INFO, LevelType.WARN, LevelType.ERROR).contains(item.level)) {
                _history.add(item)
                if (_history.size > Constants.LOG_HISTORY_MAX_SIZE) {
                    _history = _history.takeLast(Constants.LOG_HISTORY_MAX_SIZE).toMutableList()
                }
            }
        }
    }

    enum class LevelType {
        DEBUG, INFO, WARN, ERROR
    }

    @Serializable
    data class HistoryItem(
        val level: LevelType,
        val tag: String,
        val message: String,
        val cause: String? = null,
        val time: Long = System.currentTimeMillis(),
    )
}

/**
 * 注入日志
 */
abstract class Loggable(private val tag: String) {
    protected val log: Logger
        get() = Logger.create(tag)
}