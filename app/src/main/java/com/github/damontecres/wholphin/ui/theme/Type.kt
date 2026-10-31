package com.github.damontecres.wholphin.ui.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.tv.material3.Typography
import com.github.damontecres.wholphin.R

val AppTypography = Typography()

@OptIn(ExperimentalTextApi::class)
private fun variableFamily(
    res: Int,
    vararg weights: FontWeight,
) = FontFamily(
    weights.map { w ->
        Font(res, weight = w, variationSettings = FontVariation.Settings(FontVariation.weight(w.weight)))
    },
)

// ShaddaiTV theme: Manrope (the brand typeface) for everything, JetBrains Mono for numbers.
val ManropeFamily =
    variableFamily(
        R.font.manrope_variable,
        FontWeight.Normal,
        FontWeight.Medium,
        FontWeight.SemiBold,
        FontWeight.Bold,
        FontWeight.ExtraBold,
    )
val MonoFamily = variableFamily(R.font.jetbrains_mono_variable, FontWeight.Normal, FontWeight.Medium)

private fun TextStyle.with(
    family: FontFamily,
    weight: FontWeight,
) = copy(fontFamily = family, fontWeight = weight)

val LivingRoomTypography =
    AppTypography.run {
        Typography(
            displayLarge = displayLarge.with(ManropeFamily, FontWeight.Bold),
            displayMedium = displayMedium.with(ManropeFamily, FontWeight.Bold),
            displaySmall = displaySmall.with(ManropeFamily, FontWeight.Bold),
            headlineLarge = headlineLarge.with(ManropeFamily, FontWeight.Bold),
            headlineMedium = headlineMedium.with(ManropeFamily, FontWeight.SemiBold),
            headlineSmall = headlineSmall.with(ManropeFamily, FontWeight.SemiBold),
            titleLarge = titleLarge.with(ManropeFamily, FontWeight.SemiBold),
            titleMedium = titleMedium.with(ManropeFamily, FontWeight.SemiBold),
            titleSmall = titleSmall.with(ManropeFamily, FontWeight.SemiBold),
            bodyLarge = bodyLarge.with(ManropeFamily, FontWeight.Normal),
            bodyMedium = bodyMedium.with(ManropeFamily, FontWeight.Normal),
            bodySmall = bodySmall.with(ManropeFamily, FontWeight.Normal),
            labelLarge = labelLarge.with(ManropeFamily, FontWeight.SemiBold),
            labelMedium = labelMedium.with(ManropeFamily, FontWeight.Medium),
            labelSmall = labelSmall.with(ManropeFamily, FontWeight.Medium),
        )
    }
