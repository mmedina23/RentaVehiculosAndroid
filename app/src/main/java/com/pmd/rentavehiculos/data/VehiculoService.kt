package com.pmd.rentavehiculos.data


import com.pmd.rentavehiculos.data.model.Vehiculo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface VehiculoService {

    //servicio
    @GET("vehiculos")
    suspend fun listaVehiculosDisponibles(
        @Header("x-llave-api") token: String,
        @Query("estado") estado:String
    ): List<Vehiculo>


    @GET("vehiculos/{id}")
    suspend fun listaVehiculosPorId(
        @Header("x-llave-api") token: String,
        @Path("id") id:Int
    ): Vehiculo

    @PUT("vehiculos/{id}")
    suspend fun actualizarVehiculoPorId(
        @Header("x-llave-api") token: String,
        @Path("id") id: Int,
        @Body vehiculo: Vehiculo
    ): Vehiculo  // Usar Response<> para manejar el código de estado

    @DELETE("vehiculos/{id}")
    suspend fun eliminarVehiculosPorId(
        @Header("x-llave-api") token: String,
        @Path("id") id:Int
    ): Vehiculo

    @POST("vehiculos")
    suspend fun insertarVehiculo(
        @Header("x-llave-api") token: String,
        @Body vehiculo: Vehiculo
    ): Vehiculo // Devuelve el vehículo insertado


}


