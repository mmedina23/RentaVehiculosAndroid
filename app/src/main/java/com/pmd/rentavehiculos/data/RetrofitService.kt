package com.pmd.rentavehiculos.data

import com.pmd.rentavehiculos.data.model.Vehiculo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    //servicio
    @GET("vehiculos")
    suspend fun listaVehiculosDisponibles(
        @Header("x-llave-api") token: String,
        @Query("estado") estado:String
    ): List<Vehiculo>
}

//implementacion del servicio
object RetrofitServiceFactory{
    fun retrofitService(): RetrofitService{
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/v1/")//parte de url que es fija
            .addConverterFactory(GsonConverterFactory.create())//convierte el resultado de la peticion a objeto
            .client(provideClient())
            .build().create(RetrofitService::class.java)
    }

    private fun provideClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor() // esta pendiente de lo que entra y sale
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true)
            .build()

        return client
    }
}