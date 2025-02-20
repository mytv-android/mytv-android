package top.yogiczy.mytv.core.data.entities.epg

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * 节目预约列表
 */
@Serializable
@Immutable
data class EpgProgrammeReserveList(
    val value: List<EpgProgrammeReserve> = emptyList(),
) : List<EpgProgrammeReserve> by value
