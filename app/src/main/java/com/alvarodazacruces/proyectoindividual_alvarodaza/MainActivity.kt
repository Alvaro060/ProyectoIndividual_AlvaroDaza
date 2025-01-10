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


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiAplicacion()
        }
    }
}

@Composable
fun MiAplicacion() {
    var mostrarPantallaPerro by remember { mutableStateOf(false) }

    if (mostrarPantallaPerro) {
        PantallaPerro()
    } else {
        MainScreen(onButtonClick = { mostrarPantallaPerro = true })
    }
}

@Composable
fun MainScreen(onButtonClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA)) // Color más suave que combina con el botón
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onButtonClick) {
            Text(text = "Ver imágenes de perros")
        }
    }
}

@Composable
fun PantallaPerro() {
    val coroutineScope = rememberCoroutineScope()
    var dogMediaUrl by remember { mutableStateOf("") }
    var isVideo by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } // Estado para manejar la carga

    // Función para cargar un nuevo perro
    fun loadNewDog() {
        isLoading = true // Establecer el estado de carga como verdadero
        coroutineScope.launch(Dispatchers.IO) {
            val response = RandomDogApi.retrofitService.getRandomDogMedia()
            dogMediaUrl = response.url
            isVideo = dogMediaUrl.endsWith(".mp4") || dogMediaUrl.endsWith(".webm")
            isLoading = false // Establecer el estado de carga como falso cuando se haya completado
        }
    }

    // Llamamos a loadNewDog al cargar la pantalla por primera vez
    LaunchedEffect(Unit) {
        loadNewDog()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA)) // Color más suave que combina con el botón
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            // Hacemos el círculo más grande y visible
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp), // Aumenta el tamaño del círculo
                strokeWidth = 8.dp // Aumenta el grosor de la línea del círculo
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Título con recuadro estilo botón
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary, // Color del fondo
                            shape = RoundedCornerShape(12.dp) // Bordes redondeados
                        )
                        .padding(16.dp) // Padding interno del recuadro
                ) {
                    Text(
                        text = "Random.Dog",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color.White, // Color del texto blanco
                        )
                    )
                }

                // Mostrar el contenido (foto o video)
                if (isVideo) {
                    BasicText(text = "Es un video: $dogMediaUrl") // Mostrar URL del video
                } else {
                    AsyncImage(
                        model = dogMediaUrl,
                        contentDescription = "Dog Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para cargar un nuevo perro
                Button(onClick = { loadNewDog() }) {
                    Text(text = "Ver otro perro")
                }
            }
        }
    }
}