package com.pmd.rentavehiculos.data

import com.pmd.rentavehiculos.data.model.Vehiculo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {


    interface RetrofitService {
        @GET("vehiculos")
        suspend fun lisVehiculos(
            @Query("")estado:String
        ): List<Vehiculo>
    }

    object RetrofitServiceFactory{
        fun RetroFitService(): RetrofitService{
            return  Retrofit.Builder()
                .baseUrl("http://10.0.2:8080/api/v1/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitService::class.java)
        }
    }


}