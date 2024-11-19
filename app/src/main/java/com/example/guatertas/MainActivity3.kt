package com.example.guatertas

import PreferencesManager
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.guatertas.ui.theme.GuateRütasTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GuateRütasTheme {
                PantallaPrincipal()
            }
        }
    }
}

// Nueva función para generar coordenadas aleatorias dentro de Guatemala
fun generarUbicacionAleatoriaEnGuatemala(): LatLng {
    val latitud = Random.nextDouble(13.7, 17.8) // Límites de latitud de Guatemala
    val longitud = Random.nextDouble(-92.2, -88.2) // Límites de longitud de Guatemala
    return LatLng(latitud, longitud)
}

@Composable
fun PantallaPrincipal() {
    var locationPermissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    if (locationPermissionGranted) {
        PantallaPersonalizaUbicacion()
    } else {
        Text("Se requiere permiso de ubicación para acceder al mapa.", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun PantallaPersonalizaUbicacion() {
    var nombreUbicacion by remember { mutableStateOf(TextFieldValue("")) }
    var reseñaUbicacion by remember { mutableStateOf(TextFieldValue("")) }
    var linkImagen by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    val preferencesManager = PreferencesManager
    val coroutineScope = rememberCoroutineScope()

    // Generar ubicación aleatoria inicialmente
    var currentLocation by remember { mutableStateOf(generarUbicacionAleatoriaEnGuatemala()) }

    // Recuperar datos de caché
    val cachedData by preferencesManager.getCachedScreenData(context, "pantalla3").collectAsState(initial = null)

    LaunchedEffect(Unit) {
        cachedData?.let { data ->
            val cachedLocation = Gson().fromJson(data, Ubicacion::class.java)
            nombreUbicacion = TextFieldValue(cachedLocation.nombreUbicacion)
            reseñaUbicacion = TextFieldValue(cachedLocation.reseñaUbicacion)
            linkImagen = TextFieldValue(cachedLocation.linkImagen)
            currentLocation = LatLng(cachedLocation.latitud, cachedLocation.longitud)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Personaliza tu Ubicación", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(currentLocation, 15f)
            }
        ) {
            Marker(
                state = MarkerState(position = currentLocation),
                title = "Ubicación aleatoria"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombreUbicacion,
            onValueChange = { nombreUbicacion = it },
            label = { Text("Nombre de la ubicación") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = reseñaUbicacion,
            onValueChange = { reseñaUbicacion = it },
            label = { Text("Descripción de la ubicación") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = linkImagen,
            onValueChange = { linkImagen = it },
            label = { Text("Enlace de la imagen") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val locationData = Ubicacion(
                nombreUbicacion.text,
                reseñaUbicacion.text,
                linkImagen.text,
                currentLocation.latitude,
                currentLocation.longitude
            )

            // Guardar en Firestore
            firestore.collection("ubicaciones")
                .add(locationData)
                .addOnSuccessListener {
                    println("Ubicación guardada exitosamente en Firestore")
                }
                .addOnFailureListener { e ->
                    println("Error al guardar la ubicación: $e")
                }

            // Guardar en caché
            val dataToCache = Gson().toJson(locationData)
            coroutineScope.launch {
                preferencesManager.saveScreenData(context, "pantalla3", dataToCache)
            }

            // Generar nueva ubicación aleatoria
            currentLocation = generarUbicacionAleatoriaEnGuatemala()
        }) {
            Text("Guardar Ubicación")
        }
    }
}

// Modelo de datos para caché
data class Ubicacion(
    val nombreUbicacion: String,
    val reseñaUbicacion: String,
    val linkImagen: String,
    val latitud: Double,
    val longitud: Double
)
