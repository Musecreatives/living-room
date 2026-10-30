package com.github.damontecres.wholphin.ui.theme.colors

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.darkColorScheme
import com.github.damontecres.wholphin.ui.theme.ThemeColors

// Living Room: near-black ink scale with one cyan accent, matching jellyfin-tv/prototype.
// Dark only; the light schemes reuse the dark values.
val LivingRoomThemeColors =
    object : ThemeColors {
        val ink0 = Color(0xFF06080C)
        val ink1 = Color(0xFF0C1017)
        val ink2 = Color(0xFF141A24)
        val ink3 = Color(0xFF1D2531)
        val line = Color(0xFF2A3341)
        val fg = Color(0xFFEEF2F7)
        val fg2 = Color(0xFFB3BDCB)
        val fg3 = Color(0xFF8691A2)
        val accent = Color(0xFF2FB0E4)
        val accentInk = Color(0xFF04141C)
        val accentDeep = Color(0xFF0B5F80)
        val good = Color(0xFF62D39D)
        val goodInk = Color(0xFF06140D)
        val rec = Color(0xFFEF6B63)
        val recInk = Color(0xFF2A0604)

        override val darkSchemeMaterial: ColorScheme =
            androidx.compose.material3.darkColorScheme(
                primary = accent,
                onPrimary = accentInk,
                primaryContainer = ink3,
                onPrimaryContainer = fg,
                secondary = fg2,
                onSecondary = ink0,
                secondaryContainer = ink2,
                onSecondaryContainer = fg,
                tertiary = good,
                onTertiary = goodInk,
                tertiaryContainer = ink3,
                onTertiaryContainer = fg,
                error = rec,
                onError = recInk,
                errorContainer = Color(0xFF5C1712),
                onErrorContainer = fg,
                background = ink0,
                onBackground = fg,
                surface = ink1,
                onSurface = fg,
                surfaceVariant = ink3,
                onSurfaceVariant = fg2,
                outline = fg3,
                outlineVariant = line,
                scrim = Color.Black,
                inverseSurface = fg,
                inverseOnSurface = ink0,
                inversePrimary = accentDeep,
            )

        override val darkScheme =
            darkColorScheme(
                primary = accent,
                onPrimary = accentInk,
                primaryContainer = ink3,
                onPrimaryContainer = fg,
                secondary = fg2,
                onSecondary = ink0,
                secondaryContainer = ink2,
                onSecondaryContainer = fg,
                tertiary = good,
                onTertiary = goodInk,
                tertiaryContainer = ink3,
                onTertiaryContainer = fg,
                error = rec,
                onError = recInk,
                errorContainer = Color(0xFF5C1712),
                onErrorContainer = fg,
                background = ink0,
                onBackground = fg,
                surface = ink1,
                onSurface = fg,
                surfaceVariant = ink3,
                onSurfaceVariant = fg2,
                scrim = Color.Black,
                inverseSurface = fg,
                inverseOnSurface = ink0,
                inversePrimary = accentDeep,
                border = fg,
                borderVariant = line,
            )

        override val lightSchemeMaterial = darkSchemeMaterial
        override val lightScheme = darkScheme
    }
