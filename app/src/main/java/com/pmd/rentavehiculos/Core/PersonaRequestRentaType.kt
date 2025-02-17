package com.pmd.rentavehiculos.Core

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.pmd.rentavehiculos.Entity.PersonaRequestRenta
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val personaRequestRentaType = object : NavType<PersonaRequestRenta>(isNullableAllowed = true){
    override fun get(bundle: Bundle, key: String): PersonaRequestRenta? {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            bundle.getParcelable(key, PersonaRequestRenta::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): PersonaRequestRenta {
        return Json.decodeFromString<PersonaRequestRenta>(value)
    }

    override fun serializeAsValue(value: PersonaRequestRenta): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: PersonaRequestRenta) {
        bundle.putParcelable(key, value)
    }
}