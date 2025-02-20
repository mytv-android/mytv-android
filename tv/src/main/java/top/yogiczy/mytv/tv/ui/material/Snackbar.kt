package top.yogiczy.mytv.tv.ui.material

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.tv.material3.WideButton
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents

@Composable
fun SnackbarUI(
    modifier: Modifier = Modifier,
    state: SnackbarUIState = rememberSnackbarUIState(),
) {
    val currentData by rememberUpdatedState(state.currentData)
    val visible by rememberUpdatedState(state.visible)

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible,
            modifier = modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp),
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
        ) {
            SnackbarContent(
                text = currentData.text,
                showLeadingIcon = currentData.showLeadingIcon,
                leadingIcon = currentData.leadingIcon,
                leadingLoading = currentData.leadingLoading,
                showTrailingIcon = currentData.showTrailingIcon,
                trailingIcon = currentData.trailingIcon,
                trailingLoading = currentData.trailingLoading,
                type = currentData.type,
            )
        }
    }
}

@Composable
private fun SnackbarContent(
    modifier: Modifier = Modifier,
    text: String,
    showLeadingIcon: Boolean = true,
    leadingIcon: ImageVector = Icons.Outlined.Info,
    leadingLoading: Boolean = false,
    showTrailingIcon: Boolean = false,
    trailingIcon: ImageVector = Icons.Outlined.Info,
    trailingLoading: Boolean = false,
    type: SnackbarType = SnackbarType.DEFAULT,
) {
    val color = when (type) {
        SnackbarType.DEFAULT -> SnackbarColorData(
            containerColors = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            iconContainerColors = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        SnackbarType.PRIMARY -> SnackbarColorData(
            containerColors = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            iconContainerColors = MaterialTheme.colorScheme.onPrimary,
        )

        SnackbarType.SECONDARY -> SnackbarColorData(
            containerColors = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            iconContainerColors = MaterialTheme.colorScheme.onSecondary,
        )

        SnackbarType.TERTIARY -> SnackbarColorData(
            containerColors = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            iconContainerColors = MaterialTheme.colorScheme.onTertiary,
        )

        SnackbarType.ERROR -> SnackbarColorData(
            containerColors = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            iconContainerColors = MaterialTheme.colorScheme.onError,
        )
    }

    Box(
        modifier = modifier
            .sizeIn(maxWidth = 556.dp)
            .background(color.containerColors, MaterialTheme.shapes.medium)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SnackbarContentIcon(
                showIcon = showLeadingIcon,
                icon = leadingIcon,
                iconColor = color.contentColor,
                iconContainerColors = color.iconContainerColors,
                loading = leadingLoading,
            )

            Text(text, color = color.contentColor)

            SnackbarContentIcon(
                showIcon = showTrailingIcon,
                icon = trailingIcon,
                iconColor = color.contentColor,
                iconContainerColors = color.iconContainerColors,
                loading = trailingLoading,
            )
        }
    }
}

@Composable
private fun SnackbarContentIcon(
    modifier: Modifier = Modifier,
    showIcon: Boolean,
    icon: ImageVector,
    iconColor: Color,
    iconContainerColors: Color,
    loading: Boolean = false,
) {
    if (showIcon) {
        Box(
            modifier = modifier
                .background(iconContainerColors, CircleShape)
                .padding(8.dp),
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = iconColor,
                    trackColor = Color.Transparent,
                    strokeWidth = 2.dp,
                )
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = iconColor,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SnackbarContentPreview() {
    MyTvTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SnackbarContent(
                text = "Single-line snackbar label",
                showLeadingIcon = false,
                showTrailingIcon = false,
            )

            SnackbarContent(
                text = "Single-line snackbar label",
                showLeadingIcon = true,
                showTrailingIcon = false,
            )

            SnackbarContent(
                text = "Single-line snackbar label",
                showLeadingIcon = true,
                leadingLoading = true,
                showTrailingIcon = false,
            )

            SnackbarContent(
                text = "Single-line snackbar label",
                showLeadingIcon = false,
                showTrailingIcon = true,
            )

            SnackbarContent(
                text = "Single-line snackbar label",
                showLeadingIcon = true,
                showTrailingIcon = true,
            )

            SnackbarContent(
                text = "Single-line snackbar label",
                showLeadingIcon = true,
                showTrailingIcon = false,
                type = SnackbarType.PRIMARY,
            )

            SnackbarContent(
                text = "Single-line snackbar label",
                showLeadingIcon = true,
                showTrailingIcon = false,
                type = SnackbarType.SECONDARY,
            )

            SnackbarContent(
                text = "Single-line snackbar label",
                showLeadingIcon = true,
                showTrailingIcon = false,
                type = SnackbarType.TERTIARY,
            )

            SnackbarContent(
                text = "Single-line snackbar label",
                showLeadingIcon = true,
                showTrailingIcon = false,
                type = SnackbarType.ERROR,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SnackbarContentLongPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            Box(modifier = Modifier.fillMaxSize()) {
                SnackbarContent(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 28.dp),
                    text = "Single-line snackbar label".repeat(4),
                    showLeadingIcon = true,
                    showTrailingIcon = false,
                )
            }
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SnackbarUIPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            Column(
                modifier = Modifier.padding(40.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                WideButton(
                    onClick = {},
                    modifier = Modifier.handleKeyEvents(onSelect = {
                        Snackbar.show("显示新snackbar")
                    }),
                ) {
                    Text("显示新snackbar")
                }

                WideButton(
                    onClick = {},
                    modifier = Modifier.handleKeyEvents(onSelect = {
                        Snackbar.show(
                            "显示snackbar(id=test)",
                            id = "test",
                        )
                    }),
                ) {
                    Text("显示snackbar(id=test)")
                }

                WideButton(
                    modifier = Modifier.handleKeyEvents(onSelect = {
                        Snackbar.show(
                            "改变snackbar(id=test)",
                            showLeadingIcon = false,
                            leadingLoading = true,
                            showTrailingIcon = true,
                            trailingLoading = true,
                            type = SnackbarType.PRIMARY,
                            id = "test",
                        )
                    }),
                    onClick = { },
                ) {
                    Text("改变snackbar(id=test)")
                }
            }
        }

        SnackbarUI()
    }
}