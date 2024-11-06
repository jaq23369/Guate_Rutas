package com.example.guatertas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guatertas.ui.theme.GuateRütasTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

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
    // Lista de destinos con ubicaciones predefinidas
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

    var mostrarMapa by remember { mutableStateOf(false) }

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
            // Configuración de la posición inicial de la cámara en el primer destino
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
