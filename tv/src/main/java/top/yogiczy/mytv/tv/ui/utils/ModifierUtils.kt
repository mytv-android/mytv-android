package top.yogiczy.mytv.tv.ui.utils

import android.os.Build
import android.view.KeyEvent
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import top.yogiczy.mytv.tv.ui.theme.LAYOUT_GRID_SPACING
import top.yogiczy.mytv.tv.ui.theme.LAYOUT_GRID_WIDTH
import kotlin.math.absoluteValue

fun Modifier.ifElse(
    condition: () -> Boolean, ifTrueModifier: Modifier, ifFalseModifier: Modifier = Modifier
): Modifier = then(if (condition()) ifTrueModifier else ifFalseModifier)

fun Modifier.ifElse(
    condition: Boolean, ifTrueModifier: Modifier, ifFalseModifier: Modifier = Modifier
): Modifier = ifElse({ condition }, ifTrueModifier, ifFalseModifier)

fun Modifier.focusOnLaunched(key: Any = Unit): Modifier = composed {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key) {
        focusRequester.saveRequestFocus()
    }
    focusRequester(focusRequester)
}

fun Modifier.focusOnLaunchedSaveable(key: Any = Unit): Modifier = composed {
    val focusRequester = remember { FocusRequester() }
    var hasFocused by rememberSaveable(key) { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!hasFocused) {
            focusRequester.saveRequestFocus()
            hasFocused = true
        }
    }
    focusRequester(focusRequester)
}

fun Modifier.handleKeyEvents(
    onKeyTap: Map<Int, (() -> Unit)?> = emptyMap(),
    onKeyLongTap: Map<Int, (() -> Unit)?> = emptyMap(),
): Modifier = composed {
    val keyDownMap = remember { mutableMapOf<Int, Boolean>() }

    onPreviewKeyEvent {
        when (it.nativeKeyEvent.action) {
            KeyEvent.ACTION_DOWN -> {
                if (it.nativeKeyEvent.repeatCount == 0) {
                    keyDownMap[it.nativeKeyEvent.keyCode] = true
                } else if (it.nativeKeyEvent.repeatCount == 1) {
                    keyDownMap.remove(it.nativeKeyEvent.keyCode)
                    onKeyLongTap[it.nativeKeyEvent.keyCode]?.invoke()
                }
            }

            KeyEvent.ACTION_UP -> {
                if (keyDownMap[it.nativeKeyEvent.keyCode] == true) {
                    keyDownMap.remove(it.nativeKeyEvent.keyCode)
                    onKeyTap[it.nativeKeyEvent.keyCode]?.invoke()
                }
            }
        }

        false
    }
}

fun Modifier.handleDragGestures(
    onSwipeUp: () -> Unit = {},
    onSwipeDown: () -> Unit = {},
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
) = composed {
    val currentOnSwipeUp by rememberUpdatedState(onSwipeUp)
    val currentOnSwipeDown by rememberUpdatedState(onSwipeDown)
    val currentOnSwipeLeft by rememberUpdatedState(onSwipeLeft)
    val currentOnSwipeRight by rememberUpdatedState(onSwipeRight)

    val speedThreshold = 100.dp
    val distanceThreshold = 10.dp

    val verticalTracker = VelocityTracker()
    var verticalDragOffset = 0f
    val horizontalTracker = VelocityTracker()
    var horizontalDragOffset = 0f


    pointerInput(Unit) {
        detectVerticalDragGestures(
            onDragEnd = {
                if (verticalDragOffset.absoluteValue > distanceThreshold.toPx()) {
                    if (verticalTracker.calculateVelocity().y > speedThreshold.toPx()) {
                        currentOnSwipeDown()
                    } else if (verticalTracker.calculateVelocity().y < -speedThreshold.toPx()) {
                        currentOnSwipeUp()
                    }
                }
            },
        ) { change, dragAmount ->
            verticalDragOffset += dragAmount
            verticalTracker.addPosition(change.uptimeMillis, change.position)
        }
    }
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragEnd = {
                    if (horizontalDragOffset.absoluteValue > distanceThreshold.toPx()) {
                        if (horizontalTracker.calculateVelocity().x > speedThreshold.toPx()) {
                            currentOnSwipeRight()
                        } else if (horizontalTracker.calculateVelocity().x < -speedThreshold.toPx()) {
                            currentOnSwipeLeft()
                        }
                    }
                },
            ) { change, dragAmount ->
                horizontalDragOffset += dragAmount
                horizontalTracker.addPosition(change.uptimeMillis, change.position)
            }
        }
}

fun Modifier.clickableNoIndication(
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) = composed {
    val currentOnClick by rememberUpdatedState(onClick)
    val currentOnLongClick by rememberUpdatedState(onLongClick)
    val currentOnDoubleClick by rememberUpdatedState(onDoubleClick)

    pointerInput(Unit) {
        detectTapGestures(
            onDoubleTap = currentOnDoubleClick?.let { { _ -> it() } },
            onLongPress = currentOnLongClick?.let { { _ -> it() } },
            onTap = currentOnClick?.let { { _ -> it() } },
        )
    }
}

fun Modifier.handleKeyEvents(
    onLeft: (() -> Unit)? = null,
    onLongLeft: (() -> Unit)? = null,
    onRight: (() -> Unit)? = null,
    onLongRight: (() -> Unit)? = null,
    onUp: (() -> Unit)? = null,
    onLongUp: (() -> Unit)? = null,
    onDown: (() -> Unit)? = null,
    onLongDown: (() -> Unit)? = null,
    onSelect: (() -> Unit)? = null,
    onLongSelect: (() -> Unit)? = null,
    onSettings: (() -> Unit)? = null,
    onNumber: ((Int) -> Unit)? = null,
) = handleKeyEvents(
    onKeyTap = mapOf(
        KeyEvent.KEYCODE_DPAD_LEFT to onLeft,
        KeyEvent.KEYCODE_DPAD_RIGHT to onRight,
        KeyEvent.KEYCODE_DPAD_UP to onUp,
        KeyEvent.KEYCODE_CHANNEL_UP to onUp,
        KeyEvent.KEYCODE_DPAD_DOWN to onDown,
        KeyEvent.KEYCODE_CHANNEL_DOWN to onDown,

        KeyEvent.KEYCODE_DPAD_CENTER to onSelect,
        KeyEvent.KEYCODE_ENTER to onSelect,
        KeyEvent.KEYCODE_NUMPAD_ENTER to onSelect,

        KeyEvent.KEYCODE_MENU to onSettings,
        KeyEvent.KEYCODE_SETTINGS to onSettings,
        KeyEvent.KEYCODE_HELP to onSettings,
        KeyEvent.KEYCODE_H to onSettings,

        KeyEvent.KEYCODE_L to onLongSelect,
        KeyEvent.KEYCODE_W to onLongUp,
        KeyEvent.KEYCODE_S to onLongDown,
        KeyEvent.KEYCODE_A to onLongLeft,
        KeyEvent.KEYCODE_D to onLongRight,

        KeyEvent.KEYCODE_0 to onNumber?.let<(Int) -> Unit, () -> Unit> { { it(0) } },
        KeyEvent.KEYCODE_1 to onNumber?.let<(Int) -> Unit, () -> Unit> { { it(1) } },
        KeyEvent.KEYCODE_2 to onNumber?.let<(Int) -> Unit, () -> Unit> { { it(2) } },
        KeyEvent.KEYCODE_3 to onNumber?.let<(Int) -> Unit, () -> Unit> { { it(3) } },
        KeyEvent.KEYCODE_4 to onNumber?.let<(Int) -> Unit, () -> Unit> { { it(4) } },
        KeyEvent.KEYCODE_5 to onNumber?.let<(Int) -> Unit, () -> Unit> { { it(5) } },
        KeyEvent.KEYCODE_6 to onNumber?.let<(Int) -> Unit, () -> Unit> { { it(6) } },
        KeyEvent.KEYCODE_7 to onNumber?.let<(Int) -> Unit, () -> Unit> { { it(7) } },
        KeyEvent.KEYCODE_8 to onNumber?.let<(Int) -> Unit, () -> Unit> { { it(8) } },
        KeyEvent.KEYCODE_9 to onNumber?.let<(Int) -> Unit, () -> Unit> { { it(9) } },
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            KeyEvent.KEYCODE_SYSTEM_NAVIGATION_LEFT to onLeft
            KeyEvent.KEYCODE_SYSTEM_NAVIGATION_RIGHT to onRight
            KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP to onUp
            KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN to onDown
        }
    },
    onKeyLongTap = mapOf(
        KeyEvent.KEYCODE_DPAD_LEFT to onLongLeft,
        KeyEvent.KEYCODE_DPAD_RIGHT to onLongRight,
        KeyEvent.KEYCODE_DPAD_UP to onLongUp,
        KeyEvent.KEYCODE_CHANNEL_UP to onLongUp,
        KeyEvent.KEYCODE_DPAD_DOWN to onLongDown,
        KeyEvent.KEYCODE_CHANNEL_DOWN to onLongDown,

        KeyEvent.KEYCODE_ENTER to onLongSelect,
        KeyEvent.KEYCODE_NUMPAD_ENTER to onLongSelect,
        KeyEvent.KEYCODE_DPAD_CENTER to onLongSelect,
    ),
)
    .clickableNoIndication(
        onClick = onSelect,
        onLongClick = onLongSelect,
        onDoubleClick = onSettings,
    )

fun Modifier.handleKeyEvents(
    isFocused: () -> Boolean,
    focusRequester: FocusRequester,
    onLeft: (() -> Unit)? = null,
    onLongLeft: (() -> Unit)? = null,
    onRight: (() -> Unit)? = null,
    onLongRight: (() -> Unit)? = null,
    onUp: (() -> Unit)? = null,
    onLongUp: (() -> Unit)? = null,
    onDown: (() -> Unit)? = null,
    onLongDown: (() -> Unit)? = null,
    onSelect: (() -> Unit)? = null,
    onLongSelect: (() -> Unit)? = null,
    onSettings: (() -> Unit)? = null,
    onNumber: ((Int) -> Unit)? = null,
) = handleKeyEvents(
    onLeft = onLeft?.let { { if (isFocused()) it() else focusRequester.saveRequestFocus() } },
    onLongLeft = onLongLeft?.let { { if (isFocused()) it() else focusRequester.saveRequestFocus() } },
    onRight = onRight?.let { { if (isFocused()) it() else focusRequester.saveRequestFocus() } },
    onLongRight = onLongRight?.let { { if (isFocused()) it() else focusRequester.saveRequestFocus() } },
    onUp = onUp?.let { { if (isFocused()) it() else focusRequester.saveRequestFocus() } },
    onLongUp = onLongUp?.let { { if (isFocused()) it() else focusRequester.saveRequestFocus() } },
    onDown = onDown?.let { { if (isFocused()) it() else focusRequester.saveRequestFocus() } },
    onLongDown = onLongDown?.let { { if (isFocused()) it() else focusRequester.saveRequestFocus() } },
    onSelect = onSelect?.let { { if (isFocused()) it() else focusRequester.saveRequestFocus() } },
    onLongSelect = onLongSelect?.let { { if (isFocused()) it() else focusRequester.saveRequestFocus() } },
    onSettings = onSettings?.let { { if (isFocused()) it() else focusRequester.saveRequestFocus() } },
    onNumber = onNumber?.let { { num -> if (isFocused()) it(num) else focusRequester.saveRequestFocus() } },
)

fun Modifier.backHandler(onBackPressed: () -> Unit) = this.onPreviewKeyEvent {
    if (it.key == Key.Back && it.type == KeyEventType.KeyUp) {
        onBackPressed()
        true
    } else {
        false
    }
}

fun Modifier.backHandler(
    condition: () -> Boolean,
    onBackPressed: () -> Unit,
) = this.onPreviewKeyEvent {
    if (it.key == Key.Back && it.type == KeyEventType.KeyUp && condition()) {
        onBackPressed()
        true
    } else {
        false
    }
}

fun Modifier.visible(visible: Boolean) = alpha(if (visible) 1f else 0f)

fun Int.gridColumns() = (LAYOUT_GRID_WIDTH * this + LAYOUT_GRID_SPACING * (this - 1)).dp
fun Float.gridColumns() = (LAYOUT_GRID_WIDTH * this + LAYOUT_GRID_SPACING * (this - 1)).dp

fun NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        popUpTo(route) { inclusive = true }
        launchSingleTop = true
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.saveFocusRestorer(onRestoreFailed: (() -> FocusRequester)? = null): Modifier {
    if (!Configs.uiFocusOptimize) return this

    return focusRestorer {
        if (onRestoreFailed == null) return@focusRestorer FocusRequester.Default

        val result = onRestoreFailed()
        runCatching {
            result.requestFocus()
            result
        }.getOrElse { FocusRequester.Default }
    }
}

fun FocusRequester.saveRequestFocus() = runCatching { requestFocus() }