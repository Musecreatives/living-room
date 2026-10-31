package com.github.damontecres.wholphin.ui.theme.colors

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.darkColorScheme
import com.github.damontecres.wholphin.ui.theme.ThemeColors

// ShaddaiTV (enum name kept as LIVING_ROOM so saved preferences still match): broadcast ink and the
// TV-tag blue from shaddai-enterprises/brand/shaddaitv/tokens.css. Dark only; light schemes reuse it.
val LivingRoomThemeColors =
    object : ThemeColors {
        val ink0 = Color(0xFF0A1622) // --stv-ink
        val ink1 = Color(0xFF0F1D2C)
        val ink2 = Color(0xFF152738)
        val ink3 = Color(0xFF1D3247)
        val line = Color(0xFF2B4058)
        val fg = Color(0xFFF7F9FC) // --stv-paper
        val fg2 = Color(0xFFC9D4DF)
        val fg3 = Color(0xFF9FB0C2) // --stv-muted-on-ink
        val accent = Color(0xFF2E75C4) // --stv-blue
        val accentInk = Color(0xFFFFFFFF)
        val accentDeep = Color(0xFF1B4A7A) // --stv-blue-deep
        val good = Color(0xFF62D39D)
        val goodInk = Color(0xFF06140D)
        val rec = Color(0xFFD42D28) // --stv-live
        val recInk = Color(0xFFFFFFFF)

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
