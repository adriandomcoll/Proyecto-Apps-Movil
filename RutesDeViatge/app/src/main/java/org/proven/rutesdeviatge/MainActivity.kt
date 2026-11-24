package org.proven.rutesdeviatge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import org.proven.rutesdeviatge.navigation.AppNavHost
import org.proven.rutesdeviatge.ui.theme.RutesDeViatgeTheme
import org.proven.rutesdeviatge.viewmodel.TravelDiaryViewModel

/**
 * Punt d'entrada de l'aplicació.
 *
 * El ViewModel es crea amb viewModel() i es passa al grafo de navegació.
 * Així sobreviu als canvis de configuració (per exemple, girar el mòbil) i
 * totes les pantalles comparteixen el mateix estat.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RutesDeViatgeTheme {
                val travelDiaryViewModel: TravelDiaryViewModel = viewModel()
                AppNavHost(viewModel = travelDiaryViewModel)
            }
        }
    }
}
