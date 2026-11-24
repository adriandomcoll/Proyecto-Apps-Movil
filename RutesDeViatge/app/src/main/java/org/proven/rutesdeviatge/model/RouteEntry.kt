package org.proven.rutesdeviatge.model

import org.json.JSONObject
import java.util.Locale

/**
 * Nivells de dificultat que pot tenir una ruta.
 * El text (label) és el que es mostra a la interfície i el que es guarda al JSON.
 */
enum class Difficulty(val label: String) {
    FACIL("Fàcil"),
    MODERADA("Moderada"),
    EXIGENT("Exigent");

    companion object {
        /** Recupera una dificultat a partir del text guardat al fitxer JSON. */
        fun fromLabel(label: String): Difficulty =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: MODERADA
    }
}

/**
 * Model immutable d'una ruta del diari de viatges.
 *
 * És una data class: totes les seves propietats són val i, per tant, no es pot
 * modificar una ruta existent. Quan cal canviar alguna dada es crea una còpia
 * amb .copy(), tal com demana l'arquitectura MVVM.
 */
data class RouteEntry(
    val id: String,
    val routeName: String,
    val destination: String,
    val difficulty: Difficulty,
    val estimatedBudget: Double,
    val date: String,
    val isSolo: Boolean,
    val notes: String
) {
    /** Pressupost amb format català/espanyol: 250,00 €. */
    val formattedBudget: String
        get() = String.format(Locale.getDefault(), "%.2f €", estimatedBudget)

    /**
     * Converteix la ruta a un objecte JSON per poder-la guardar a
     * filesDir/diari_viatges.json.
     */
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("routeName", routeName)
        put("destination", destination)
        put("difficulty", difficulty.label)
        put("estimatedBudget", estimatedBudget)
        put("date", date)
        put("isSolo", isSolo)
        put("notes", notes)
    }

    /**
     * Genera el text de la crònica en format Markdown.
     * S'utilitza tant per exportar el fitxer com per compartir-lo amb altres apps.
     */
    fun toMarkdown(): String = buildString {
        appendLine("# $routeName")
        appendLine()
        appendLine("> $destination · $date")
        appendLine()
        appendLine("- **Dificultat:** ${difficulty.label}")
        appendLine("- **Pressupost estimat:** $formattedBudget")
        appendLine("- **Viatge en solitari:** ${if (isSolo) "Sí" else "No"}")
        appendLine()
        appendLine("## Crònica de la ruta")
        appendLine()
        appendLine(notes.ifBlank { "Encara no hi ha notes per a aquesta ruta." })
        appendLine()
        appendLine("---")
        appendLine("*Generat amb Diari de Viatges*")
    }

    companion object {
        /** Reconstrueix una ruta llegida del fitxer JSON. */
        fun fromJson(json: JSONObject): RouteEntry = RouteEntry(
            id = json.optString("id"),
            routeName = json.optString("routeName"),
            destination = json.optString("destination"),
            difficulty = Difficulty.fromLabel(json.optString("difficulty")),
            estimatedBudget = json.optDouble("estimatedBudget", 0.0),
            date = json.optString("date"),
            isSolo = json.optBoolean("isSolo", false),
            notes = json.optString("notes")
        )
    }
}
