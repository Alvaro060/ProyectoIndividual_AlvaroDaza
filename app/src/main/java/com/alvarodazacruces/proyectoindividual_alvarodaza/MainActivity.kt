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
            .background(Color(0xFFE0F7FA))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onButtonClick) {
            Text(text = "Ver Imágenes De Perros")
        }
    }
}

@Composable
fun PantallaPerro() {
    val coroutineScope = rememberCoroutineScope()
    var dogMediaUrl by remember { mutableStateOf("") }
    var isVideo by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    fun cargarNuevoPerro() {
        isLoading = true
        coroutineScope.launch(Dispatchers.IO) {
            val response = RandomDogApi.retrofitService.getRandomDogMedia()
            dogMediaUrl = response.url
            isVideo = dogMediaUrl.endsWith(".mp4") || dogMediaUrl.endsWith(".webm")
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        cargarNuevoPerro()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp),
                strokeWidth = 8.dp
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Random.Dog",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color.White,
                        )
                    )
                }

                if (isVideo) {
                    BasicText(text = "Es un video: $dogMediaUrl")
                } else {
                    AsyncImage(
                        model = dogMediaUrl,
                        contentDescription = "Imagen De Un Perro",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { cargarNuevoPerro() }) {
                    Text(text = "Ver Otro Perro")
                }
            }
        }
    }
}