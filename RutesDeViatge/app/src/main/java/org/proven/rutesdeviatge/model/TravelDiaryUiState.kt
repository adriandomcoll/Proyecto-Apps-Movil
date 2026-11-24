package org.proven.rutesdeviatge.model

/**
 * Estat complet de la interfície del formulari.
 *
 * Conté:
 *  - Les dades que l'usuari està escrivint (camps del formulari).
 *  - Els missatges d'error de validació.
 *  - L'històric de rutes carregat des de l'emmagatzematge intern.
 *
 * La interfície només llegeix aquest objecte; no modifica mai els camps
 * directament. Els canvis sempre passen pel ViewModel.
 */
data class TravelDiaryUiState(
    val routeName: String = "",
    val destination: String = "",
    val difficulty: Difficulty = Difficulty.MODERADA,
    val estimatedBudget: String = "",
    val date: String = "",
    val isSolo: Boolean = false,
    val notes: String = "",

    // Errors de validació. Si són null, el camp és correcte.
    val routeNameError: String? = null,
    val destinationError: String? = null,
    val budgetError: String? = null,
    val dateError: String? = null,

    val history: List<RouteEntry> = emptyList(),
    val isLoading: Boolean = true
) {
    /** Converteix el text del pressupost a Double. Retorna null si no és vàlid. */
    val budgetValue: Double?
        get() = estimatedBudget
            .trim()
            .replace(',', '.')
            .toDoubleOrNull()
            ?.takeIf { it > 0.0 }

    /**
     * El formulari és vàlid quan té nom, destí, un pressupost positiu i una data.
     * Aquest valor s'utilitza per activar o desactivar el botó de generar.
     */
    val isFormValid: Boolean
        get() = routeName.isNotBlank() &&
                destination.isNotBlank() &&
                budgetValue != null &&
                date.isNotBlank()
}
