package org.proven.rutesdeviatge.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.proven.rutesdeviatge.model.Difficulty

/**
 * Insígnia de color que resumeix la dificultat de la ruta.
 * S'utilitza a la postal i a l'històric.
 */
@Composable
fun DifficultyBadge(
    difficulty: Difficulty,
    modifier: Modifier = Modifier
) {
    val background: Color
    val content: Color

    when (difficulty) {
        Difficulty.FACIL -> {
            background = Color(0xFFD7F5DC)
            content = Color(0xFF1B5E20)
        }

        Difficulty.MODERADA -> {
            background = Color(0xFFFFF1C2)
            content = Color(0xFF7A5900)
        }

        Difficulty.EXIGENT -> {
            background = Color(0xFFFFDAD6)
            content = Color(0xFF8C1D18)
        }
    }

    Surface(
        modifier = modifier,
        color = background,
        contentColor = content,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = difficulty.label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/** Petita etiqueta per indicar si la ruta es va fer en solitari o acompanyat. */
@Composable
fun SoloBadge(
    isSolo: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = if (isSolo) "🎒 En solitari" else "👥 Amb companyia",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}
