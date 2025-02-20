package top.yogiczy.mytv.core.data.entities.epg

/**
 * 频道当前节目/下一个节目
 */
data class EpgProgrammeRecent(
    /**
     * 当前正在播放
     */
    val now: EpgProgramme? = null,

    /**
     * 稍后播放
     */
    val next: EpgProgramme? = null,
) {
    companion object {
        val EXAMPLE = EpgProgrammeRecent(
            now = EpgProgramme(
                title = "2023/2024赛季中国男子篮球职业联赛季后赛12进8第五场",
                startAt = System.currentTimeMillis() - 1000 * 60 * 60 * 2,
                endAt = System.currentTimeMillis() + 1000 * 60 * 60 * 2,
            ),
            next = null,
        )
    }
}