package top.yogiczy.mytv.core.data.entities.actions

enum class KeyDownAction(val value: Int){
    ChangeCurrentChannelToPrev(0),
    ChangeCurrentChannelToNext(1),
    ChangeCurrentChannelLineIdxToPrev(2),
    ChangeCurrentChannelLineIdxToNext(3),
    ToIptvSourceScreen(4),
    ToChannelScreen(5),
    ToQuickOpScreen(6),
    ToEpgScreen(7),
    ToChannelLineScreen(8),
    ToVideoPlayerControllerScreen(9);
    companion object {
        fun fromValue(value: Int): KeyDownAction {
            return entries.firstOrNull { it.value == value } ?: ChangeCurrentChannelToPrev
        }
    }
}