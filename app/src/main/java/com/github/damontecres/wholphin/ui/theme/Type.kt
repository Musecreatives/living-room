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

// Living Room theme: Sora for display/headline/title, Figtree for body/label, JetBrains Mono for numbers.
val SoraFamily = variableFamily(R.font.sora_variable, FontWeight.Medium, FontWeight.SemiBold, FontWeight.Bold)
val FigtreeFamily =
    variableFamily(R.font.figtree_variable, FontWeight.Normal, FontWeight.Medium, FontWeight.SemiBold, FontWeight.Bold)
val MonoFamily = variableFamily(R.font.jetbrains_mono_variable, FontWeight.Normal, FontWeight.Medium)

private fun TextStyle.with(
    family: FontFamily,
    weight: FontWeight,
) = copy(fontFamily = family, fontWeight = weight)

val LivingRoomTypography =
    AppTypography.run {
        Typography(
            displayLarge = displayLarge.with(SoraFamily, FontWeight.Bold),
            displayMedium = displayMedium.with(SoraFamily, FontWeight.Bold),
            displaySmall = displaySmall.with(SoraFamily, FontWeight.Bold),
            headlineLarge = headlineLarge.with(SoraFamily, FontWeight.Bold),
            headlineMedium = headlineMedium.with(SoraFamily, FontWeight.SemiBold),
            headlineSmall = headlineSmall.with(SoraFamily, FontWeight.SemiBold),
            titleLarge = titleLarge.with(SoraFamily, FontWeight.SemiBold),
            titleMedium = titleMedium.with(FigtreeFamily, FontWeight.SemiBold),
            titleSmall = titleSmall.with(FigtreeFamily, FontWeight.SemiBold),
            bodyLarge = bodyLarge.with(FigtreeFamily, FontWeight.Normal),
            bodyMedium = bodyMedium.with(FigtreeFamily, FontWeight.Normal),
            bodySmall = bodySmall.with(FigtreeFamily, FontWeight.Normal),
            labelLarge = labelLarge.with(FigtreeFamily, FontWeight.SemiBold),
            labelMedium = labelMedium.with(FigtreeFamily, FontWeight.Medium),
            labelSmall = labelSmall.with(FigtreeFamily, FontWeight.Medium),
        )
    }
