package org.proven.rutesdeviatge.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.proven.rutesdeviatge.ui.screens.FormScreen
import org.proven.rutesdeviatge.ui.screens.HistoryScreen
import org.proven.rutesdeviatge.ui.screens.PostalScreen
import org.proven.rutesdeviatge.viewmodel.TravelDiaryViewModel

/**
 * Grafo de navegació de l'aplicació.
 *
 * form -> postal/{routeId} -> (tornar)
 * form -> history -> postal/{routeId} -> (tornar)
 *
 * El ViewModel es comparteix entre totes les pantalles perquè l'estat i
 * l'històric siguin sempre els mateixos.
 */
@Composable
fun AppNavHost(viewModel: TravelDiaryViewModel) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "form"
    ) {
        composable(route = "form") {
            FormScreen(
                uiState = uiState,
                onRouteNameChange = viewModel::onRouteNameChange,
                onDestinationChange = viewModel::onDestinationChange,
                onDifficultyChange = viewModel::onDifficultyChange,
                onBudgetChange = viewModel::onBudgetChange,
                onDateChange = viewModel::onDateChange,
                onSoloChange = viewModel::onSoloChange,
                onNotesChange = viewModel::onNotesChange,
                onGenerate = {
                    // Desem la ruta i només naveguem si la validació ha estat correcta.
                    val routeId = viewModel.generateRoute()
                    if (routeId != null) {
                        navController.navigate("postal/$routeId")
                    }
                },
                onOpenHistory = { navController.navigate("history") }
            )
        }

        composable(route = "history") {
            HistoryScreen(
                routes = uiState.history,
                isLoading = uiState.isLoading,
                onRouteClick = { routeId ->
                    navController.navigate("postal/$routeId")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "postal/{routeId}",
            arguments = listOf(
                navArgument("routeId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val routeId = backStackEntry.arguments?.getString("routeId")
            PostalScreen(
                route = uiState.history.firstOrNull { it.id == routeId },
                isLoading = uiState.isLoading,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
