package com.alvarodazacruces.proyectoindividual_alvarodaza

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Interfaz para la API de perros aleatorios
interface RandomDogApiService {
    @GET("woof.json")
    suspend fun getRandomDogMedia(): RandomDogResponse
}

// Data class para la respuesta de la API
data class RandomDogResponse(val url: String)

// Objeto para la configuración de Retrofit
object RandomDogApi {
    private const val BASE_URL = "https://random.dog/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Instancia de la API
    val retrofitService: RandomDogApiService by lazy {
        retrofit.create(RandomDogApiService::class.java)
    }
}
