package com.pmd.rentavehiculos.data

import com.pmd.rentavehiculos.data.model.Vehiculo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

// Modelos para login
data class LoginRequest(
    val usuario_sistema: String,
    val contrasena: String
)

data class LoginResponse(
    val usuario: String,
    val rol: String,
    val token: String
)

interface RetrofitService {

    // Endpoint para obtener la lista de vehículos
    @GET("vehiculos")
    suspend fun listaVehiculosDisponibles(
        @Header("Authorization") token: String,  // Usamos el token del login aquí
        @Query("estado") estado: String
    ): List<Vehiculo>

    // Endpoint para iniciar sesión
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // Endpoint para cerrar sesión
    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Void
}

// Implementación del servicio Retrofit
object RetrofitServiceFactory {

    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"  // Asegúrate de que esta URL es la correcta

    fun retrofitService(): RetrofitService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // Convertimos la respuesta JSON a objetos Kotlin
            .client(provideClient())
            .build()
            .create(RetrofitService::class.java)
    }

    private fun provideClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY  // Esto nos permitirá ver las peticiones y respuestas en el log
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true)
            .build()
    }
}
