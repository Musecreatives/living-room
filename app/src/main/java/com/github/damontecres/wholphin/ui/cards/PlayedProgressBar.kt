package com.github.damontecres.wholphin.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import com.github.damontecres.wholphin.ui.Cards

/**
 * Resume progress on a card: inset rounded accent bar over a faint track (prototype .prog)
 */
@Composable
fun PlayedProgressBar(
    percent: Double,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
                .fillMaxWidth()
                .height(Cards.playedPercentHeight)
                .background(Color.White.copy(alpha = .28f), CircleShape),
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth((percent / 100).toFloat().coerceIn(0f, 1f))
                .background(MaterialTheme.colorScheme.primary, CircleShape),
        )
    }
}
