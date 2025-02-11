package com.pmd.rentavehiculos.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("session", Context.MODE_PRIVATE)

    var token: String?
        get() = prefs.getString("TOKEN", null)
        set(value) {
            prefs.edit().putString("TOKEN", value).apply()
        }

    // Almacenar el ID en SharedPreferences
    var personaId: Int?
        get() {
            // Si no existe, devuelve null o un valor por defecto
            // Nota: SharedPreferences no permite guardar null para enteros, así que puedes usar 0 como valor no válido.
            val id = prefs.getInt("PERSONA_ID", 0)
            return if (id == 0) null else id
        }
        set(value) {
            // Si value es null, se puede borrar la entrada o guardarlo como 0
            prefs.edit().putInt("PERSONA_ID", value ?: 0).apply()
        }

    // Método para limpiar la sesión (por ejemplo, en logout)
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}