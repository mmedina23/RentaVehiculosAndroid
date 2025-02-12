package com.pmd.rentavehiculos.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.pmd.rentavehiculos.modelo.Persona

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("session", Context.MODE_PRIVATE)

    var token: String?
        get() = prefs.getString("TOKEN", null)
        set(value) {
            prefs.edit().putString("TOKEN", value).apply()
        }

    var personaId: Int?
        get() {
            val id = prefs.getInt("PERSONA_ID", 0)
            return if (id == 0) null else id
        }
        set(value) {
            prefs.edit().putInt("PERSONA_ID", value ?: 0).apply()
        }

    // Persona guardada en SharedPreferences
    var persona: Persona?
        get() {
            val json = prefs.getString("PERSONA", null)
            return if (json != null) Gson().fromJson(json, Persona::class.java) else null
        }
        set(value) {
            val json = Gson().toJson(value)
            prefs.edit().putString("PERSONA", json).apply()
        }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}