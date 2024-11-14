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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.guatertas.ui.theme.GuateRütasTheme
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainActivity5 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuateRütasTheme {
                val navController = rememberNavController()  // Añadimos el navController
                PantallaApoyoComunidadLocal(navController)
            }
        }
    }
}

@Composable
fun PantallaApoyoComunidadLocal(navController: NavHostController) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager
    val coroutineScope = rememberCoroutineScope()

    // Estado de la lista de proyectos cargados
    var proyectosCargados by remember { mutableStateOf<List<ProyectoSostenible>>(emptyList()) }

    // Obtener datos en caché si los hay
    val cachedData by preferencesManager.getCachedScreenData(context, "pantalla5").collectAsState(initial = null)

    LaunchedEffect(Unit) {
        cachedData?.let { data ->
            val cachedProyectos = Gson().fromJson(data, Array<ProyectoSostenible>::class.java).toList()
            proyectosCargados = cachedProyectos
        }

        // Simulación: Verificar si hay conexión para cargar datos nuevos
        if (isOnline(context)) {
            val nuevosProyectos = listOf(
                ProyectoSostenible(
                    nombre = "Reforma de senderos el Cimarron",
                    descripcion = "Proyecto que mejorar los senderos para llegar al lugar.",
                    detalles = "\n" +
                            "Este proyecto busca mejorar los caminos hacia El Cimarrón, un lugar de gran valor natural, haciéndolos más seguros y accesibles para los visitantes. Se integrarán señalizaciones sobre la flora y fauna para ofrecer una experiencia educativa y respetuosa con el entorno.\n"
                ),
                ProyectoSostenible(
                    nombre = "Turismo en laguna Ordoñez",
                    descripcion = "Un proyecto que involucra a la comunidad para promover el turismo.",
                    detalles = "Este proyecto fomenta la participación de la comunidad local en el desarrollo turístico sostenible, invitando a los visitantes a sumergirse en la cultura y tradiciones de la región. A través de actividades guiadas y talleres, los turistas pueden aprender sobre la historia, el arte y la vida cotidiana de los habitantes, mientras contribuyen directamente a proyectos de desarrollo comunitario que benefician a la economía local."
                ),
                ProyectoSostenible(
                    nombre = "Conservación de Playa blanca",
                    descripcion = "Iniciativa para reducir la contaminación y proteger el ecosistema de la playa.",
                    detalles = "sta iniciativa tiene como objetivo proteger el ecosistema único de Playa Blanca mediante la reducción de la contaminación y la preservación de su biodiversidad. El proyecto realiza campañas de concientización que educan a los visitantes sobre la importancia de mantener la playa limpia, además de organizar actividades de limpieza donde turistas y residentes colaboran en la recolección de desechos. "
                )
            )
            proyectosCargados = nuevosProyectos

            // Guardar nuevos datos en caché
            val dataToCache = Gson().toJson(nuevosProyectos)
            coroutineScope.launch {
                preferencesManager.saveScreenData(context, "pantalla5", dataToCache)
            }
        }
    }

    var proyectoSeleccionado by remember { mutableStateOf<ProyectoSostenible?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Apoyo a la Comunidad Local",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (proyectoSeleccionado == null) {
            Text(
                text = "Proyectos de turismo sostenible:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(proyectosCargados) { proyecto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = proyecto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = proyecto.descripcion, fontSize = 14.sp)

                            Button(
                                onClick = { proyectoSeleccionado = proyecto },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Ver más")
                            }
                        }
                    }
                }
            }
        } else {
            ProyectoSostenibleDetalles(proyectoSeleccionado!!) {
                proyectoSeleccionado = null
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


// Componente que muestra los detalles del proyecto seleccionado
@Composable
fun ProyectoSostenibleDetalles(proyecto: ProyectoSostenible, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título del proyecto
        Text(
            text = proyecto.nombre,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Descripción detallada del proyecto
        Text(text = proyecto.detalles, fontSize = 16.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Botón para regresar a la lista de proyectos
        Button(
            onClick = { onBack() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Regresar a la lista")
        }
    }
}

// Modelo de datos para un proyecto sostenible
data class ProyectoSostenible(
    val nombre: String,
    val descripcion: String,
    val detalles: String
)

@Preview(showBackground = true)
@Composable
fun PreviewPantallaApoyoComunidadLocal() {
    GuateRütasTheme {
        PantallaApoyoComunidadLocal(rememberNavController())  // En el Preview, usamos navController
    }
}
