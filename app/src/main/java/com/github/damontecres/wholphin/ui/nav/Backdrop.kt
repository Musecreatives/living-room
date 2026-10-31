@file:OptIn(ExperimentalCoilApi::class)

package com.github.damontecres.wholphin.ui.nav

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.MaterialTheme
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.useExistingImageAsPlaceholder
import coil3.request.ImageRequest
import coil3.request.transitionFactory
import com.github.damontecres.wholphin.preferences.BackdropStyle
import com.github.damontecres.wholphin.services.BackdropResult
import com.github.damontecres.wholphin.ui.CrossFadeFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Shows the current backdrop images provided by [com.github.damontecres.wholphin.services.BackdropService]
 */
@Composable
fun Backdrop(
    drawerIsOpen: Boolean,
    backdropStyle: BackdropStyle,
    viewModel: ApplicationContentViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    enableTopScrim: Boolean = true,
    useExistingImageAsPlaceholder: Boolean = false,
    crossfadeDuration: Duration = 800.milliseconds,
) {
    val backdrop by viewModel.backdropService.backdropFlow.collectAsStateWithLifecycle()
    Backdrop(
        backdrop = backdrop,
        drawerIsOpen = drawerIsOpen,
        backdropStyle = backdropStyle,
        modifier = modifier,
        enableTopScrim = enableTopScrim,
        useExistingImageAsPlaceholder = useExistingImageAsPlaceholder,
        crossfadeDuration = crossfadeDuration,
    )
}

/**
 * Shows the current backdrop images provided by the [BackdropResult]
 */
@Composable
fun Backdrop(
    backdrop: BackdropResult,
    drawerIsOpen: Boolean,
    backdropStyle: BackdropStyle,
    modifier: Modifier = Modifier,
    enableTopScrim: Boolean = true,
    useExistingImageAsPlaceholder: Boolean = false,
    crossfadeDuration: Duration = 800.milliseconds,
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val baseBackgroundColor = MaterialTheme.colorScheme.background
    if (backdrop.hasColors &&
        (backdropStyle == BackdropStyle.BACKDROP_DYNAMIC_COLOR || backdropStyle == BackdropStyle.UNRECOGNIZED)
    ) {
        val animPrimary by animateColorAsState(
            backdrop.primaryColor,
            animationSpec = tween(1250),
            label = "dynamic_backdrop_primary",
        )
        val animSecondary by animateColorAsState(
            backdrop.secondaryColor,
            animationSpec = tween(1250),
            label = "dynamic_backdrop_secondary",
        )
        val animTertiary by animateColorAsState(
            backdrop.tertiaryColor,
            animationSpec = tween(1250),
            label = "dynamic_backdrop_tertiary",
        )
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawRect(color = baseBackgroundColor)
                        val start = if (isRtl) size.width else 0f
                        val end = if (isRtl) 0f else size.width
                        // Top Left (Vibrant/Muted)
                        drawRect(
                            brush =
                                Brush.radialGradient(
                                    colors = listOf(animSecondary, Color.Transparent),
                                    center = Offset(start, 0f),
                                    radius = size.width * 0.8f,
                                ),
                        )
                        // Bottom Right (DarkVibrant/DarkMuted)
                        drawRect(
                            brush =
                                Brush.radialGradient(
                                    colors = listOf(animPrimary, Color.Transparent),
                                    center = Offset(end, size.height),
                                    radius = size.width * 0.8f,
                                ),
                        )
                        // Bottom Left (Dark / Bridge)
                        drawRect(
                            brush =
                                Brush.radialGradient(
                                    colors =
                                        listOf(
                                            baseBackgroundColor,
                                            Color.Transparent,
                                        ),
                                    center = Offset(start, size.height),
                                    radius = size.width * 0.8f,
                                ),
                        )
                        // Top Right (Under Image - Vibrant/Bright)
                        drawRect(
                            brush =
                                Brush.radialGradient(
                                    colors = listOf(animTertiary, Color.Transparent),
                                    center = Offset(end, 0f),
                                    radius = size.width * 0.8f,
                                ),
                        )
                    },
        )
    }
    if (backdropStyle != BackdropStyle.BACKDROP_NONE) {
        Box(
            modifier = modifier.fillMaxSize(),
        ) {
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(backdrop.imageUrl)
                        .useExistingImageAsPlaceholder(useExistingImageAsPlaceholder)
                        .transitionFactory(CrossFadeFactory(crossfadeDuration))
                        .build(),
                contentDescription = null,
                // Living Room: full-bleed backdrop under left + bottom scrims (prototype .bd/.scrim)
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopEnd,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                        .drawWithContent {
                            val start = if (isRtl) size.width else 0f
                            val end = if (isRtl) 0f else size.width
                            drawContent()
                            if (drawerIsOpen) {
                                drawRect(
                                    brush = SolidColor(Color.Black),
                                    alpha = .75f,
                                )
                            }
                            // Subtle top scrim for system UI readability (clock, tabs)
                            if (enableTopScrim) {
                                drawRect(
                                    brush =
                                        Brush.verticalGradient(
                                            colorStops =
                                                arrayOf(
                                                    0f to Color.Black.copy(alpha = TOP_SCRIM_ALPHA),
                                                    TOP_SCRIM_END_FRACTION to Color.Transparent,
                                                ),
                                        ),
                                    blendMode = BlendMode.Multiply,
                                )
                            }
                            val ink = baseBackgroundColor
                            drawRect(
                                brush =
                                    Brush.horizontalGradient(
                                        0f to ink.copy(alpha = .95f),
                                        .38f to ink.copy(alpha = .72f),
                                        .72f to ink.copy(alpha = .08f),
                                        1f to ink.copy(alpha = .08f),
                                        startX = start,
                                        endX = end,
                                    ),
                            )
                            drawRect(
                                brush =
                                    Brush.verticalGradient(
                                        .34f to ink.copy(alpha = 0f),
                                        .58f to ink.copy(alpha = .55f),
                                        .94f to ink,
                                    ),
                            )
                        },
            )
        }
    }
}
