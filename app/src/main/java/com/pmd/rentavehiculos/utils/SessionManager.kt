package com.pmd.rentavehiculos.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("RentaVehiculosPrefs", Context.MODE_PRIVATE)

    fun saveApiKey(apiKey: String) {
        prefs.edit().putString("API_KEY", apiKey).apply()
    }

    fun getApiKey(): String? {
        return prefs.getString("API_KEY", null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
