package com.alvarodazacruces.proyectoindividual_alvarodaza

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Interfaz que define los endpoints de la API RandomDog.
interface InterfazRandomDogApi {
    // Definimos el endpoint que obtiene un perro aleatorio en formato JSON.
    @GET("woof.json")
    suspend fun getRandomDogMedia(): RespuestaRandomDog // Devuelve una respuesta del tipo 'RespuestaRandomDog'.
}

// Clase que representa la respuesta que obtenemos de la API. En este caso, solo contiene la URL del perro.
data class RespuestaRandomDog(val url: String)

// Objeto que me da una instancia de Retrofit para realizar solicitudes a la API RandomDog.
object RandomDogApi {
    private const val BASE_URL = "https://random.dog/"

    // Configuración de Retrofit que permite hacer solicitudes HTTP a la API.
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) // Especificamos la URL base de la API.
        .addConverterFactory(GsonConverterFactory.create()) // Usamos Gson para convertir las respuestas JSON.
        .build() // Creamos el objeto Retrofit.

    // Instancia de la API, que se crea solo cuando se necesita.
    val retrofitService: InterfazRandomDogApi by lazy {
        retrofit.create(InterfazRandomDogApi::class.java) // Creamos la instancia de la interfaz API.
    }
}
