package org.proven.rutesdeviatge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.proven.rutesdeviatge.data.TravelDiaryRepository
import org.proven.rutesdeviatge.model.Difficulty
import org.proven.rutesdeviatge.model.RouteEntry
import org.proven.rutesdeviatge.model.TravelDiaryUiState
import java.util.UUID

/**
 * ViewModel del diari de viatges.
 *
 * Guarda l'estat en un MutableStateFlow privat i exposa només un StateFlow
 * de només lectura. Cada canvi es fa amb .update { it.copy(...) }, de manera
 * que l'estat és immutable i Compose es redibuixa automàticament.
 */
class TravelDiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TravelDiaryRepository(application.filesDir)

    private val _uiState = MutableStateFlow(TravelDiaryUiState())
    val uiState: StateFlow<TravelDiaryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    /** Carrega l'històric des de filesDir en un fil d'execució secundari. */
    private fun loadHistory() {
        viewModelScope.launch {
            val routes = withContext(Dispatchers.IO) {
                repository.loadRoutes()
            }
            _uiState.update { state ->
                state.copy(history = routes, isLoading = false)
            }
        }
    }

    // ---------------------------------------------------------------------
    // Canvis dels camps del formulari
    // ---------------------------------------------------------------------

    fun onRouteNameChange(value: String) {
        _uiState.update { state ->
            state.copy(
                routeName = value,
                routeNameError = if (value.isBlank()) "El nom de la ruta és obligatori" else null
            )
        }
    }

    fun onDestinationChange(value: String) {
        _uiState.update { state ->
            state.copy(
                destination = value,
                destinationError = if (value.isBlank()) "El destí és obligatori" else null
            )
        }
    }

    fun onDifficultyChange(difficulty: Difficulty) {
        _uiState.update { state -> state.copy(difficulty = difficulty) }
    }

    fun onBudgetChange(value: String) {
        // Només permetem dígits i separadors decimals (coma o punt).
        val filtered = value.filter { it.isDigit() || it == ',' || it == '.' }
        val number = filtered.replace(',', '.').toDoubleOrNull()

        val error = when {
            filtered.isBlank() -> "Indica un pressupost"
            number == null -> "Introdueix un import vàlid"
            number <= 0.0 -> "El pressupost ha de ser més gran que 0"
            else -> null
        }

        _uiState.update { state ->
            state.copy(estimatedBudget = filtered, budgetError = error)
        }
    }

    fun onDateChange(value: String) {
        _uiState.update { state ->
            state.copy(
                date = value,
                dateError = if (value.isBlank()) "Selecciona la data de la ruta" else null
            )
        }
    }

    fun onSoloChange(value: Boolean) {
        _uiState.update { state -> state.copy(isSolo = value) }
    }

    fun onNotesChange(value: String) {
        _uiState.update { state -> state.copy(notes = value) }
    }

    // ---------------------------------------------------------------------
    // Accions principals
    // ---------------------------------------------------------------------

    /**
     * Valida el formulari, crea la ruta, la desa a l'històric i retorna el seu
     * id per poder navegar a la postal. Retorna null si el formulari no és vàlid.
     */
    fun generateRoute(): String? {
        val state = _uiState.value

        val validatedState = state.copy(
            routeNameError = if (state.routeName.isBlank()) "El nom de la ruta és obligatori" else null,
            destinationError = if (state.destination.isBlank()) "El destí és obligatori" else null,
            budgetError = when {
                state.estimatedBudget.isBlank() -> "Indica un pressupost"
                state.budgetValue == null -> "Introdueix un import més gran que 0"
                else -> null
            },
            dateError = if (state.date.isBlank()) "Selecciona la data de la ruta" else null
        )

        if (!validatedState.isFormValid) {
            _uiState.value = validatedState
            return null
        }

        val newRoute = RouteEntry(
            id = UUID.randomUUID().toString(),
            routeName = state.routeName.trim(),
            destination = state.destination.trim(),
            difficulty = state.difficulty,
            estimatedBudget = state.budgetValue ?: 0.0,
            date = state.date,
            isSolo = state.isSolo,
            notes = state.notes.trim()
        )

        val updatedHistory = listOf(newRoute) + state.history

        // Actualitzem la interfície i buidem el formulari per a la propera ruta.
        _uiState.update { current ->
            current.copy(
                routeName = "",
                destination = "",
                difficulty = Difficulty.MODERADA,
                estimatedBudget = "",
                date = "",
                isSolo = false,
                notes = "",
                routeNameError = null,
                destinationError = null,
                budgetError = null,
                dateError = null,
                history = updatedHistory
            )
        }

        persistHistory(updatedHistory)
        return newRoute.id
    }

    /** Escriu l'històric al fitxer JSON intern. */
    private fun persistHistory(routes: List<RouteEntry>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveRoutes(routes)
        }
    }

    /** Retorna una ruta concreta de l'històric (s'utilitza a la postal). */
    fun getRouteById(id: String): RouteEntry? =
        _uiState.value.history.firstOrNull { it.id == id }

    /** Text Markdown de la crònica, llest per exportar o compartir. */
    fun markdownFor(route: RouteEntry): String = route.toMarkdown()
}
