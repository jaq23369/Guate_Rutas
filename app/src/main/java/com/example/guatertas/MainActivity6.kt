package com.example.guatertas

import PreferencesManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.guatertas.ui.theme.GuateRütasTheme
import com.google.gson.Gson
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview

class MainActivity6 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuateRütasTheme {
                val navController = rememberNavController()  // Añadimos el NavController
                PantallaNotificacionesYAlertas(navController)
            }
        }
    }
}

@Composable
fun PantallaNotificacionesYAlertas(navController: NavHostController) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager
    val coroutineScope = rememberCoroutineScope()

    // Estado de la lista de destinos cargados
    var destinosNotificados by remember { mutableStateOf<List<DestinoConNotificacion>>(emptyList()) }

    // Obtener datos en caché si los hay
    val cachedData by preferencesManager.getCachedScreenData(context, "pantalla6").collectAsState(initial = null)

    LaunchedEffect(Unit) {
        cachedData?.let { data ->
            val cachedDestinos = Gson().fromJson(data, Array<DestinoConNotificacion>::class.java).toList()
            destinosNotificados = cachedDestinos
        }

        // Simulación: Verificar si hay conexión para cargar datos nuevos
        if (isOnline(context)) {
            val nuevosDestinos = listOf(
                DestinoConNotificacion(
                    nombre = "El Cimarron",
                    clima = "Lluvia ligera",
                    accesibilidad = "Accesible",
                    notificacionesHabilitadas = false
                ),
                DestinoConNotificacion(
                    nombre = "Laguna Ordoñez",
                    clima = "Soleado",
                    accesibilidad = "Accesible",
                    notificacionesHabilitadas = true
                ),
                DestinoConNotificacion(
                    nombre = "Playa Blanca",
                    clima = "Nublado",
                    accesibilidad = "Cerrado por mantenimiento",
                    notificacionesHabilitadas = false
                )
            )
            destinosNotificados = nuevosDestinos

            // Guardar nuevos datos en caché
            val dataToCache = Gson().toJson(nuevosDestinos)
            coroutineScope.launch {
                preferencesManager.saveScreenData(context, "pantalla6", dataToCache)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Notificaciones y Alertas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(destinosNotificados) { destino ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = destino.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Clima: ${destino.clima}", fontSize = 14.sp)
                        Text(text = "Accesibilidad: ${destino.accesibilidad}", fontSize = 14.sp)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Notificaciones", modifier = Modifier.weight(1f))

                            Switch(
                                checked = destino.notificacionesHabilitadas,
                                onCheckedChange = {
                                    val index = destinosNotificados.indexOf(destino)
                                    destinosNotificados = destinosNotificados.toMutableList().apply {
                                        this[index] = this[index].copy(notificacionesHabilitadas = it)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Volver")
        }
    }
}

data class DestinoConNotificacion(
    val nombre: String,
    val clima: String,
    val accesibilidad: String,
    val notificacionesHabilitadas: Boolean
)

@Preview(showBackground = true)
@Composable
fun PreviewPantallaNotificacionesYAlertas() {
    GuateRütasTheme {
        PantallaNotificacionesYAlertas(rememberNavController())  // En el Preview, usamos navController
    }
}
