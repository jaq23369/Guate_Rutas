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
import com.example.guatertas.Destino

class MainActivity4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuateRütasTheme{
                // Iniciamos el NavController para navegación
                val navController = rememberNavController()
                PantallaPlanificacionViajes(navController)
            }
        }
    }
}

// Pantalla de Planificación de Viajes
@Composable
fun PantallaPlanificacionViajes(navController: NavHostController) {
    // Lista de destinos disponibles
    val destinosDisponibles = listOf(
        Destino("Cimarron", "Un monumento natural en Guatemala.", R.drawable.cimarron),
        Destino("Laguna Ordoñez", "Precioso lago en una de las montañas más altas del país.", R.drawable.lagordo),
        Destino("Atitlán", "Una hermosa playa en el atlántico.", R.drawable.plbl)
    )

    // Lista mutable para almacenar los destinos seleccionados por el usuario
    val itinerario = remember { mutableStateListOf<Destino>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "Planificación de Viajes",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista de destinos seleccionables
        Text(
            text = "Destinos disponibles:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(destinosDisponibles) { destino ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = destino.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = destino.descripcion, fontSize = 14.sp)

                        // Botón para agregar destino al itinerario
                        Button(
                            onClick = {
                                if (destino !in itinerario) {
                                    itinerario.add(destino)
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Agregar al itinerario")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el itinerario
        Text(
            text = "Tu itinerario:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(itinerario) { destino ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = destino.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                        // Botón para eliminar destino del itinerario
                        Button(
                            onClick = { itinerario.remove(destino) },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Eliminar del itinerario")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para guardar el itinerario
        Button(
            onClick = {
                // Aquí puedes implementar la lógica de guardar itinerario en almacenamiento o base de datos
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Guardar itinerario")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para regresar a la pantalla principal u otra pantalla
        Button(
            onClick = { navController.popBackStack() }, // Usamos el NavController para regresar
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Volver")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaPlanificacionViajes() {
    GuateRütasTheme{
        PantallaPlanificacionViajes(rememberNavController()) // Previsualización con el navController
    }
}
