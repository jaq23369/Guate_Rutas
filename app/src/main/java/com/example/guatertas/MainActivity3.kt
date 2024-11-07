package com.example.guatertas

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
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.example.guatertas.ui.theme.GuateRütasTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.annotation.SuppressLint
import android.location.Location
import com.google.firebase.firestore.FirebaseFirestore

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

@Composable
fun PantallaPrincipal() {
    var locationPermissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current // Obtener el contexto actual

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
    val context = LocalContext.current // Necesario para Firestore

    // Ubicación actual fija (puedes integrarla con FusedLocationProvider para obtener dinámicamente)
    val currentLocation = LatLng(14.634915, -90.506882)
    val firestore = FirebaseFirestore.getInstance() // Instancia de Firestore

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
                title = "Tu ubicación actual"
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
            // Llamada a Firestore para almacenar la información
            val locationData = hashMapOf(
                "nombreUbicacion" to nombreUbicacion.text,
                "reseñaUbicacion" to reseñaUbicacion.text,
                "linkImagen" to linkImagen.text,
                "latitud" to currentLocation.latitude,
                "longitud" to currentLocation.longitude
            )

            firestore.collection("ubicaciones")
                .add(locationData)
                .addOnSuccessListener {
                    println("Ubicación guardada exitosamente en Firestore")
                }
                .addOnFailureListener { e ->
                    println("Error al guardar la ubicación: $e")
                }
        }) {
            Text("Guardar Ubicación")
        }
    }
}



