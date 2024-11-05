package com.example.guatertas

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.guatertas.ui.theme.GuateRütasTheme

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

// Pantalla de Apoyo a la Comunidad Local
@Composable
fun PantallaApoyoComunidadLocal(navController: NavHostController) {  // Añadimos navController
    // Lista de proyectos de turismo sostenible
    val proyectosSostenibles = listOf(
        ProyectoSostenible(
            nombre = "Reforma de senderos el Cimarron",
            descripcion = "Proyecto que mejorar los senderos para llegar al lugar.",
            detalles = "Este proyecto se centra en arreglar y mejorar los senderos del Cimarron."
        ),
        ProyectoSostenible(
            nombre = "Turismo en laguna Ordoñez",
            descripcion = "Un proyecto que involucra a la comunidad para promover el turismo.",
            detalles = "Los visitantes pueden aprender sobre la cultura local y contribuir a proyectos de desarrollo comunitario."
        ),
        ProyectoSostenible(
            nombre = "Conservación de Playa blanca",
            descripcion = "Iniciativa para reducir la contaminación y proteger el ecosistema de la playa.",
            detalles = "El proyecto incluye campañas de concientización y actividades de limpieza en colaboración con los turistas y la comunidad."
        )
    )

    var proyectoSeleccionado by remember { mutableStateOf<ProyectoSostenible?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "Apoyo a la Comunidad Local",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (proyectoSeleccionado == null) {
            // Lista de proyectos disponibles
            Text(
                text = "Proyectos de turismo sostenible:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(proyectosSostenibles) { proyecto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = proyecto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = proyecto.descripcion, fontSize = 14.sp)

                            // Botón para ver más detalles
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
            // Detalles del proyecto seleccionado
            ProyectoSostenibleDetalles(proyectoSeleccionado!!) {
                proyectoSeleccionado = null  // Regresar a la lista
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para regresar a la pantalla anterior
        Button(
            onClick = { navController.popBackStack() }, // Botón para regresar
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
