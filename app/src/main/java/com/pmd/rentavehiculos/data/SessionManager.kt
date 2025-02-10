package com.pmd.rentavehiculos.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    var token: String?
        get() = prefs.getString("TOKEN", null)
        set(value) {
            prefs.edit().putString("TOKEN", value).apply()
        }

    // Método para limpiar la sesión (por ejemplo, en logout)
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}