package org.proven.rutesdeviatge.data

import org.json.JSONArray
import org.json.JSONObject
import org.proven.rutesdeviatge.model.RouteEntry
import java.io.File

/**
 * Accés a l'emmagatzematge intern privat de l'aplicació.
 *
 * filesDir és una carpeta privada de l'app (normalment
 * /data/data/org.proven.rutesdeviatge/files). Cap altra aplicació hi pot
 * accedir sense permisos especials. Si l'usuari desinstal·la l'app, aquesta
 * carpeta s'esborra automàticament.
 */
class TravelDiaryRepository(filesDir: File) {

    private val diaryFile = File(filesDir, FILE_NAME)

    /**
     * Llegeix el fitxer JSON i retorna la llista de rutes.
     * Si el fitxer no existeix o està malmès, retorna una llista buida perquè
     * l'aplicació no es tanqui.
     */
    fun loadRoutes(): List<RouteEntry> {
        if (!diaryFile.exists()) return emptyList()

        return try {
            val array = JSONArray(diaryFile.readText())
            (0 until array.length()).mapNotNull { index ->
                val json: JSONObject = array.optJSONObject(index) ?: return@mapNotNull null
                RouteEntry.fromJson(json)
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    /**
     * Sobreescriu el fitxer JSON amb la llista completa de rutes.
     * Es guarda amb sagnat (toString(2)) perquè sigui fàcil de llegir i revisar.
     */
    fun saveRoutes(routes: List<RouteEntry>) {
        val array = JSONArray()
        routes.forEach { route -> array.put(route.toJson()) }
        diaryFile.writeText(array.toString(2))
    }

    /** Ruta física del fitxer, útil per mostrar-la o depurar-la. */
    fun storagePath(): String = diaryFile.absolutePath

    companion object {
        const val FILE_NAME = "diari_viatges.json"
    }
}
