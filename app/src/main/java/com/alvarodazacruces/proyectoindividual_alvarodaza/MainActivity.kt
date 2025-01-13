package com.alvarodazacruces.proyectoindividual_alvarodaza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape


// Clase principal de la aplicación.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiAplicacion()
        }
    }
}

// Composable principal de la aplicación, donde se maneja el estado de la pantalla.
@Composable
fun MiAplicacion() {
    // Estado que maneja si mostrar la pantalla de perros o la pantalla principal.
    var mostrarPantallaPerro by remember { mutableStateOf(false) }

    // Si el estado es true, muestra la pantalla con la imagen o video de perro, si es false, muestra la pantalla principal con el botón.
    if (mostrarPantallaPerro) {
        PantallaPerro() // Pantalla con imagen de perro.
    } else {
        MainScreen(onButtonClick = { mostrarPantallaPerro = true }) // Pantalla principal con botón.
    }
}

// Composable que representa la pantalla principal con un botón para mostrar las imágenes de perros.
@Composable
fun MainScreen(onButtonClick: () -> Unit) {
    // Caja que ocupa el tamaño de la pantalla y está alineada al centro.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA)) // Fondo azul claro.
            .padding(16.dp), // Espaciado de 16 dp.
        contentAlignment = Alignment.Center // Alineación del contenido al centro.
    ) {
        // Botón que al hacer clic cambia el estado para mostrar las imágenes de perros.
        Button(onClick = onButtonClick) {
            Text(text = "Ver Imágenes De Perros")
        }
    }
}

// Composable que muestra la pantalla con la imagen o video de perro aleatorio.
@Composable
fun PantallaPerro() {
    //Uso de corrutinas para cargar la imagen/video de forma asíncrona.
    val coroutineScope = rememberCoroutineScope()
    var dogMediaUrl by remember { mutableStateOf("") } // URL de la imagen o video del perro.
    var isVideo by remember { mutableStateOf(false) } // Determina si es un video o una imagen.
    var isLoading by remember { mutableStateOf(false) } // Estado de carga (si se está obteniendo el perro).

    // Función que hace la solicitud a la API para obtener una nueva imagen o video de perro.
    fun cargarNuevoPerro() {
        isLoading = true // Mientras carga, se muestra el indicador de carga.
        coroutineScope.launch(Dispatchers.IO) {
            // Hacemos la solicitud a la API de perros aleatorios.
            val response = RandomDogApi.retrofitService.getRandomDogMedia()
            dogMediaUrl = response.url // Asignamos la URL recibida a la variable dogMediaUrl.
            // Verificamos si la URL termina en .mp4 o .webm, lo cual indicaría que es un video.
            isVideo = dogMediaUrl.endsWith(".mp4") || dogMediaUrl.endsWith(".webm")
            isLoading = false // Terminó de cargar la imagen o video.
        }
    }

    // Llamamos a la función para cargar un perro cuando se inicia la pantalla.
    LaunchedEffect(Unit) {
        cargarNuevoPerro()
    }

    // Caja que ocupa el tamaño de la pantalla y está alineada al centro.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA)) // Fondo azul claro.
            .padding(16.dp),
        contentAlignment = Alignment.Center // Alineación del contenido al centro.
    ) {
        // Si se está cargando la imagen o video, mostramos un indicador de carga.
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp), // Indicador de carga de 100 dp de tamaño.
                strokeWidth = 8.dp
            )
        } else {
            // Si ya se ha cargado la imagen o video, mostramos el contenido.
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Caja con el texto "Random.Dog", mostrando el nombre del servicio.
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary, // Color de fondo primario.
                            shape = RoundedCornerShape(12.dp) // Esquinas redondeadas de 12 dp.
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Random.Dog", // Nombre del servicio.
                        style = TextStyle(
                            fontWeight = FontWeight.Bold, // Negrita.
                            fontSize = 30.sp, // Tamaño de fuente de 30 sp.
                            color = Color.White, // Color blanco.
                        )
                    )
                }

                // Si la URL del perro es un video, mostramos un texto con la URL.
                if (isVideo) {
                    BasicText(text = "Es un video: $dogMediaUrl")
                } else {
                    // Si no es un video, mostramos la imagen del perro.
                    AsyncImage(
                        model = dogMediaUrl, // URL de la imagen.
                        contentDescription = "Imagen De Un Perro", // Descripción de la imagen.
                        modifier = Modifier
                            .fillMaxWidth() // La imagen ocupa el ancho.
                            .height(300.dp) // Altura de la imagen de 300 dp.
                    )
                }

                Spacer(modifier = Modifier.height(16.dp)) // Espaciado entre los elementos.

                // Botón para cargar un nuevo perro aleatorio.
                Button(onClick = { cargarNuevoPerro() }) {
                    Text(text = "Ver Otro Perro") // Texto en el botón.
                }
            }
        }
    }
}
