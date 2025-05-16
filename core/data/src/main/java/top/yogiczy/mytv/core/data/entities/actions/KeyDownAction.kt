package top.yogiczy.mytv.core.data.entities.actions

enum class KeyDownAction(val value: Int, val label: String){
    ChangeCurrentChannelToPrev(0, "前一频道"),
    ChangeCurrentChannelToNext(1, "后一频道"),
    ChangeCurrentChannelLineIdxToPrev(2, "前一线路"),
    ChangeCurrentChannelLineIdxToNext(3, "后一线路"),
    ToIptvSourceScreen(4, "管理订阅源"),
    ToChannelScreen(5, "频道列表"),
    ToQuickOpScreen(6, "快捷设置"),
    ToEpgScreen(7, "节目单"),
    ToChannelLineScreen(8, "线路列表"),
    ToVideoPlayerControllerScreen(9, "播放控制");
    companion object {
        fun fromValue(value: Int): KeyDownAction {
            return entries.firstOrNull { it.value == value } ?: ChangeCurrentChannelToPrev
        }
    }
}