package com.pmd.rentavehiculos.Core

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.pmd.rentavehiculos.Entity.PersonaRequestRenta
import com.pmd.rentavehiculos.Entity.VehiculoRequestRenta
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val vehiculoRequestRentaType = object : NavType<VehiculoRequestRenta>(isNullableAllowed = true){
    override fun get(bundle: Bundle, key: String): VehiculoRequestRenta? {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            bundle.getParcelable(key, VehiculoRequestRenta::class.java)
        }else{
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): VehiculoRequestRenta {
        return Json.decodeFromString<VehiculoRequestRenta>(value)
    }

    override fun serializeAsValue(value: VehiculoRequestRenta): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: VehiculoRequestRenta) {
        bundle.putParcelable(key, value)
    }

}