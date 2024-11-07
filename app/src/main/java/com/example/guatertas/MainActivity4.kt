package com.example.guatertas

import PreferencesManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guatertas.ui.theme.GuateRütasTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

class MainActivity4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuateRütasTheme {
                PantallaPlanificacionViajes()
            }
        }
    }
}

@Composable
fun PantallaPlanificacionViajes() {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager
    val coroutineScope = rememberCoroutineScope()

    // Lista de destinos predefinidos
    val destinos = listOf(
        Destino(
            nombre = "Cimarron",
            descripcion = "Un hermoso monumento natural en medio del desierto.",
            imagenResId = R.drawable.cimarron,
            latitud = 15.0311,
            longitud = -91.6365
        ),
        Destino(
            nombre = "Laguna Ordoñez",
            descripcion = "Precioso lago en una de las montañas más altas del país.",
            imagenResId = R.drawable.lagordo,
            latitud = 14.7528,
            longitud = -91.2678
        )
    )

    // Recuperar caché
    val cachedData by preferencesManager.getCachedScreenData(context, "pantalla4").collectAsState(initial = null)
    var destinosCargados by remember { mutableStateOf<List<Destino>?>(null) }
    var mostrarMapa by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Si hay datos en caché, cargarlos
        cachedData?.let { data ->
            destinosCargados = Gson().fromJson(data, Array<Destino>::class.java).toList()
        }

        // Simulación de conexión para cargar datos nuevos
        if (isOnline(context)) { // Reemplaza el comentario con la función de verificación de conexión
            val nuevosDestinos = destinos // Simulando una respuesta de red
            destinosCargados = nuevosDestinos

            // Guardar en caché
            val dataToCache = Gson().toJson(nuevosDestinos)
            coroutineScope.launch {
                preferencesManager.saveScreenData(context = context, screen = "pantalla4", data = dataToCache)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Posibles Destinos", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            mostrarMapa = true
        }) {
            Text("Ver Mapa")
        }

        if (mostrarMapa) {
            destinosCargados?.let { destinos ->
                val initialPosition = LatLng(destinos[0].latitud, destinos[0].longitud)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(initialPosition, 10f)
                }

                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    cameraPositionState = cameraPositionState
                ) {
                    destinos.forEach { destino ->
                        Marker(
                            state = MarkerState(position = LatLng(destino.latitud, destino.longitud)),
                            title = destino.nombre,
                            snippet = destino.descripcion
                        )
                    }
                }
            } ?: Text("No hay datos disponibles. Conéctate a Internet para cargar destinos.")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaPlanificacionViajes() {
    GuateRütasTheme {
        PantallaPlanificacionViajes()
    }
}
