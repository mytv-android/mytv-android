package top.yogiczy.mytv.core.data.entities.epg

import kotlinx.serialization.Serializable
import java.util.Calendar
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * 频道节目
 */
@Serializable
data class EpgProgramme(
    /**
     * 开始时间（时间戳）
     */
    val startAt: Long = 0,

    /**
     * 结束时间（时间戳）
     */
    val endAt: Long = 0,

    /**
     * 节目名称
     */
    val title: String = "",
) {
    companion object {
        /**
         * 是否正在直播
         */
        fun EpgProgramme.isLive() = System.currentTimeMillis() in startAt..<endAt

        /**
         * 节目进度
         */
        fun EpgProgramme.progress(current: Long = System.currentTimeMillis()) =
            (current - startAt).toFloat() / (endAt - startAt)

        fun EpgProgramme.remainingMinutes(current: Long = System.currentTimeMillis()) =
            ceil((endAt - current) / 60_000f).roundToInt()

        val EXAMPLE = EpgProgramme(
            startAt = System.currentTimeMillis() - 3600 * 1000,
            endAt = System.currentTimeMillis() + 3600 * 1000,
            title = "节目标题",
        )

        val EMPTY by lazy {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            EpgProgramme(
                startAt = calendar.timeInMillis,
                endAt = calendar.timeInMillis + (24 * 3600 - 1) * 1000,
                title = "精彩节目",
            )
        }
    }
}