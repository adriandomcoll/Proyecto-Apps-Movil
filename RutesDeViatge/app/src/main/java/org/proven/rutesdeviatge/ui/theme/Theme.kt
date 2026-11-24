package org.proven.rutesdeviatge.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = OceanGreenLight,
    onPrimary = ColorOnPrimaryDark,
    primaryContainer = OceanGreen,
    onPrimaryContainer = OceanGreenContainer,
    secondary = SeaGreyLight,
    onSecondary = SeaGrey,
    secondaryContainer = SeaGrey,
    onSecondaryContainer = SeaGreyContainer,
    tertiary = HorizonBlueLight,
    onTertiary = HorizonBlue,
    tertiaryContainer = HorizonBlue,
    onTertiaryContainer = HorizonBlueContainer,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = OceanGreen,
    onPrimary = ColorOnPrimaryLight,
    primaryContainer = OceanGreenContainer,
    onPrimaryContainer = OnOceanGreenContainer,
    secondary = SeaGrey,
    onSecondary = ColorOnPrimaryLight,
    secondaryContainer = SeaGreyContainer,
    onSecondaryContainer = OnSeaGreyContainer,
    tertiary = HorizonBlue,
    onTertiary = ColorOnPrimaryLight,
    tertiaryContainer = HorizonBlueContainer,
    onTertiaryContainer = OnHorizonBlueContainer,
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightBackground,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline
)

@Composable
fun RutesDeViatgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desactivem el color dinàmic per mantenir la paleta pròpia de l'app.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
