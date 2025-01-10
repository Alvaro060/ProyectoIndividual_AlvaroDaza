package com.alvarodazacruces.proyectoindividual_alvarodaza

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface InterfazRandomDogApi {
    @GET("woof.json")
    suspend fun getRandomDogMedia(): RespuestaRandomDog
}

data class RespuestaRandomDog(val url: String)

object RandomDogApi {
    private const val BASE_URL = "https://random.dog/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Instancia de la API
    val retrofitService: InterfazRandomDogApi by lazy {
        retrofit.create(InterfazRandomDogApi::class.java)
    }
}
