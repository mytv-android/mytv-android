package top.yogiczy.mytv.tv.ui.screen.settings.subcategories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.core.data.entities.actions.KeyDownAction
import top.yogiczy.mytv.tv.ui.material.SimplePopup
import top.yogiczy.mytv.tv.ui.rememberChildPadding

@Composable
fun SettingsUiControlSettingScreen(
    modifier: Modifier = Modifier,
    keyDownEventUpProvider: () -> KeyDownAction = { KeyDownAction.ChangeCurrentChannelLineIdxToNext},
    onkeyDownEventUpChanged: (KeyDownAction) -> Unit = {},
    keyDownEventDownProvider: () -> KeyDownAction = { KeyDownAction.ChangeCurrentChannelLineIdxToPrev},
    onkeyDownEventDownChanged: (KeyDownAction) -> Unit = {},
    keyDownEventLeftProvider: () -> KeyDownAction = { KeyDownAction.ChangeCurrentChannelToPrev},
    onkeyDownEventLeftChanged: (KeyDownAction) -> Unit = {},
    keyDownEventRightProvider: () -> KeyDownAction = { KeyDownAction.ChangeCurrentChannelToNext},
    onkeyDownEventRightChanged: (KeyDownAction) -> Unit = {},
    keyDownEventSelectProvider: () -> KeyDownAction = { KeyDownAction.ToIptvSourceScreen},
    onkeyDownEventSelectChanged: (KeyDownAction) -> Unit = {},
    keyDownEventLongSelectProvider: () -> KeyDownAction = { KeyDownAction.ToChannelScreen},
    onkeyDownEventLongSelectChanged: (KeyDownAction) -> Unit = {},
    keyDownEventLongUpProvider: () -> KeyDownAction = { KeyDownAction.ToQuickOpScreen},
    onkeyDownEventLongUpChanged: (KeyDownAction) -> Unit = {},
    keyDownEventLongDownProvider: () -> KeyDownAction = { KeyDownAction.ToEpgScreen},
    onkeyDownEventLongDownChanged: (KeyDownAction) -> Unit = {},
    keyDownEventLongLeftProvider: () -> KeyDownAction = { KeyDownAction.ToChannelLineScreen},
    onkeyDownEventLongLeftChanged: (KeyDownAction) -> Unit = {},
    keyDownEventLongRightProvider: () -> KeyDownAction = { KeyDownAction.ToVideoPlayerControllerScreen},
    onkeyDownEventLongRightChanged: (KeyDownAction) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    
    var visible by remember { mutableStateOf(false) }
    var popupConfig by remember { mutableStateOf<KeyActionConfig>(KeyActionConfig()) }

    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("设置 / 控制 / 按键行为") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            SettingsUiControlSettingItem(
                titleProvider = {"上键"},
                firstItemFocusRequester = firstItemFocusRequester,
                valueProvider = keyDownEventUpProvider,
                onValueChanged = {
                    popupConfig = KeyActionConfig(
                        provider = keyDownEventUpProvider,
                        onValueChanged = onkeyDownEventUpChanged,
                    )
                    visible = true
                }
            )
        }

        item {
            SettingsUiControlSettingItem(
                titleProvider = {"下键"},
                firstItemFocusRequester = firstItemFocusRequester,
                valueProvider = keyDownEventDownProvider,
                onValueChanged = {
                    popupConfig = KeyActionConfig(
                        provider = keyDownEventDownProvider,
                        onValueChanged = onkeyDownEventDownChanged,
                    )
                    visible = true
                }
            )
        }

        item {
            SettingsUiControlSettingItem(
                titleProvider = {"左键"},
                firstItemFocusRequester = firstItemFocusRequester,
                valueProvider = keyDownEventLeftProvider,
                onValueChanged = {
                    popupConfig = KeyActionConfig(
                        provider = keyDownEventLeftProvider,
                        onValueChanged = onkeyDownEventLeftChanged,
                    )
                    visible = true
                }
            )
        }

        item {
            SettingsUiControlSettingItem(
                titleProvider = {"右键"},
                firstItemFocusRequester = firstItemFocusRequester,
                valueProvider = keyDownEventRightProvider,
                onValueChanged = {
                    popupConfig = KeyActionConfig(
                        provider = keyDownEventRightProvider,
                        onValueChanged = onkeyDownEventRightChanged,
                    )
                    visible = true
                }
            )
        }

        item {
            SettingsUiControlSettingItem(
                titleProvider = {"选择键"},
                firstItemFocusRequester = firstItemFocusRequester,
                valueProvider = keyDownEventSelectProvider,
                onValueChanged = {
                    popupConfig = KeyActionConfig(
                        provider = keyDownEventSelectProvider,
                        onValueChanged = onkeyDownEventSelectChanged,
                    )
                    visible = true
                }
            )
        }

        item {
            SettingsUiControlSettingItem(
                titleProvider = {"长按选择键"},
                firstItemFocusRequester = firstItemFocusRequester,
                valueProvider = keyDownEventLongSelectProvider,
                onValueChanged = {
                    popupConfig = KeyActionConfig(
                        provider = keyDownEventLongSelectProvider,
                        onValueChanged = onkeyDownEventLongSelectChanged,
                    )
                    visible = true
                }
            )
        }

        item {
            SettingsUiControlSettingItem(
                titleProvider = {"长按上键"},
                firstItemFocusRequester = firstItemFocusRequester,
                valueProvider = keyDownEventLongUpProvider,
                onValueChanged = {
                    popupConfig = KeyActionConfig(
                        provider = keyDownEventLongUpProvider,
                        onValueChanged = onkeyDownEventLongUpChanged,
                    )
                    visible = true
                }
            )
        }

        item {
            SettingsUiControlSettingItem(
                titleProvider = {"长按下键"},
                firstItemFocusRequester = firstItemFocusRequester,
                valueProvider = keyDownEventLongDownProvider,
                onValueChanged = {
                    popupConfig = KeyActionConfig(
                        provider = keyDownEventLongDownProvider,
                        onValueChanged = onkeyDownEventLongDownChanged,
                    )
                    visible = true
                }
            )
        }

        item {
            SettingsUiControlSettingItem(
                titleProvider = {"长按左键"},
                firstItemFocusRequester = firstItemFocusRequester,
                valueProvider = keyDownEventLongLeftProvider,
                onValueChanged = {
                    popupConfig = KeyActionConfig(
                        provider = keyDownEventLongLeftProvider,
                        onValueChanged = onkeyDownEventLongLeftChanged,
                    )
                    visible = true
                }
            )
        }

        item {
            SettingsUiControlSettingItem(
                titleProvider = {"长按右键"},
                firstItemFocusRequester = firstItemFocusRequester,
                valueProvider = keyDownEventLongRightProvider,
                onValueChanged = {
                    popupConfig = KeyActionConfig(
                        provider = keyDownEventLongRightProvider,
                        onValueChanged = onkeyDownEventLongRightChanged,
                    )
                    visible = true
                }
            )
        }
    }
    SimplePopup(
        visibleProvider = { visible },
        onDismissRequest = { visible = false },
    ) {
        SettingsUiControlSettingSubMenu(
            modifier = Modifier,
            valueProvider = popupConfig.provider,
            onValueChanged = { action ->
                popupConfig.onValueChanged(action)
                visible = false
            },
        )
    }
        
}

@Composable
fun SettingsUiControlSettingItem(
    modifier: Modifier = Modifier,
    titleProvider: () -> String,
    firstItemFocusRequester: FocusRequester = remember { FocusRequester() },
    valueProvider: () -> KeyDownAction,
    onValueChanged: (KeyDownAction) -> Unit = {},
){
    val currentValue = valueProvider()
    SettingsListItem(
        modifier = Modifier.focusRequester(firstItemFocusRequester),
        headlineContent = titleProvider(),
        trailingContent =  currentValue.label,
        onSelect = {
            onValueChanged(currentValue) 
        },
        link = true,
    )
}

@Composable
fun SettingsUiControlSettingSubMenu(
    modifier: Modifier = Modifier,
    valueProvider: () -> KeyDownAction = { KeyDownAction.ChangeCurrentChannelLineIdxToNext },
    onValueChanged: (KeyDownAction) -> Unit = {},
) {
    val currentValue = valueProvider()
    val valueList = listOf(
        KeyDownAction.ChangeCurrentChannelToPrev,
        KeyDownAction.ChangeCurrentChannelToNext,
        KeyDownAction.ChangeCurrentChannelLineIdxToPrev,
        KeyDownAction.ChangeCurrentChannelLineIdxToNext,
        KeyDownAction.ToIptvSourceScreen,
        KeyDownAction.ToChannelScreen,
        KeyDownAction.ToQuickOpScreen,
        KeyDownAction.ToEpgScreen,
        KeyDownAction.ToChannelLineScreen,
        KeyDownAction.ToVideoPlayerControllerScreen
    )
    val childPadding = rememberChildPadding()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            modifier = Modifier,
            contentPadding = childPadding.copy(top = 10.dp).paddingValues,
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(valueList) { value ->
                ListItem(
                    modifier = Modifier
                        .handleKeyEvents(onSelect = { onValueChanged(value) }),
                    headlineContent = {
                        Text(
                            value.label,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    },
                    trailingContent = {
                        if (currentValue == value) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                            )
                        }
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
                    ),
                    selected = false,
                    onClick = {},
                )
            }
        }
    }
}

data class KeyActionConfig(
    val provider: () -> KeyDownAction = { KeyDownAction.ChangeCurrentChannelLineIdxToNext },
    val onValueChanged: (KeyDownAction) -> Unit = {}
)