package com.pmd.rentavehiculos.Core

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pmd.rentavehiculos.Entity.PersonaRequestRenta
import com.pmd.rentavehiculos.Entity.RentaVehiculoNavigation
import com.pmd.rentavehiculos.Entity.VehiculoRequestRenta
import com.pmd.rentavehiculos.Screens.Admin.vehiculoRentadoDetalle
import com.pmd.rentavehiculos.Screens.Cliente.AdmindScreen

import com.pmd.rentavehiculos.Screens.Cliente.ClienteScreen
import com.pmd.rentavehiculos.Screens.Cliente.ListaMisVehiculos
import com.pmd.rentavehiculos.Screens.Cliente.RentarVehiculoFunction
import com.pmd.rentavehiculos.Screens.LoginFuncion
import kotlin.reflect.typeOf
import com.pmd.rentavehiculos.Screens.SplashScreen as splashScreenFunction


@Composable
fun NavigationWrapper() {
    val navController = rememberNavController();


    NavHost(navController = navController, startDestination = SplashScreen){
        composable<SplashScreen> {
            splashScreenFunction(navController)
        }

        composable<Login> {
            LoginFuncion(
                NavigationClient =  {token, personaId, Persona -> navController.navigate(Cliente(token = token, personaId = personaId, Persona = Persona))},
                NavigationAdmind = {token, personaId, Persona -> navController.navigate(Admind(token = token, personaId = personaId, Persona = Persona))}
            )
        }

        composable<Cliente>(typeMap = mapOf(typeOf<PersonaRequestRenta>() to personaRequestRentaType)) { backStackEntry ->
            val cliente:Cliente = backStackEntry.toRoute()
            val token = cliente.token
            val personaId = cliente.personaId
            val Persona:PersonaRequestRenta = cliente.Persona
            ClienteScreen(token = token, personaId = personaId, Persona,
                    NavigationRentarVehiculo = {personaId, vehiculoId, token, precioDiaVehiculo, Persona, Vehiculo -> navController.navigate(RentarVehiculo(personaId = personaId, vehiculoId = vehiculoId, token = token, precioDiaVehiculo = precioDiaVehiculo.toDouble(), Persona = Persona, Vehiculo = Vehiculo))},
                NavigationMisVehiculos = {token, personaId -> navController.navigate(MisVehiculos(token = token, personaId = personaId))}
            )
        }

        composable<Admind>(typeMap = mapOf(typeOf<PersonaRequestRenta>() to personaRequestRentaType)) { backStackEntry ->
            val admind:Admind = backStackEntry.toRoute()
            val Persona:PersonaRequestRenta = admind.Persona
            AdmindScreen(token = admind.token, Persona = Persona, personaId = admind.personaId,
                NavigationVehiculoRentado = {token -> navController.navigate(VehiculoRentadoDetalle(token = token))})
        }

        composable<RentarVehiculo> (typeMap = mapOf(typeOf<PersonaRequestRenta>() to personaRequestRentaType, typeOf<VehiculoRequestRenta>() to vehiculoRequestRentaType)){ backStackEntry ->
            val rentaVehiculoNavigation: RentaVehiculoNavigation = backStackEntry.toRoute()
            val personaId = rentaVehiculoNavigation.personaId
            val vehiculoId = rentaVehiculoNavigation.vehiculoId
            val token = rentaVehiculoNavigation.token
            val Persona:PersonaRequestRenta = rentaVehiculoNavigation.Persona
            val Vehiculo:VehiculoRequestRenta = rentaVehiculoNavigation.Vehiculo
            val precioDiaVehiculo:Double = rentaVehiculoNavigation.precioDiaVehiculo.toDouble()?:0.0
            RentarVehiculoFunction(personaId = personaId, vehiculoId = vehiculoId, token = token, precioDiaVehiculo = precioDiaVehiculo, Persona = Persona, Vehiculo = Vehiculo)
        }

        composable<MisVehiculos> { backStackEntry ->
            val misVehiculos:MisVehiculos = backStackEntry.toRoute()
            val token = misVehiculos.token
            val personaId = misVehiculos.personaId
            ListaMisVehiculos(token = token, personaId = personaId)
        }

        composable<VehiculoRentadoDetalle> { backStackEntry ->
            val vehiculoRentadoDetalle:VehiculoRentadoDetalle = backStackEntry.toRoute()
            val token:String = vehiculoRentadoDetalle.token
            vehiculoRentadoDetalle(token = token)
        }
    }
}