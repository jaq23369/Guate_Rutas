package com.example.guatertas


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.guatertas.ui.theme.GuateRütasTheme


class MainActivity2 : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuateRütasTheme{
                // No necesitamos inicializar el NavController aquí, ya lo pasaremos desde el NavGraph
                val destino = Destino(
                    nombre = "Cimarron",
                    descripcion = "Un hermoso monumento natural en medio del desierto.",
                    imagenResId = R.drawable.cimarron,
                    comoLlegar = "Puedes llegar en autobús desde Ciudad de Guatemala hasta huehuetengango y luego tomar transporte hacia el cimarron.",
                    queLlevar = "Lleva ropa cómoda, protector solar y suficiente agua.",
                    queEsperar = "Naturaleza increíble, senderos y caminata de 2 horas.",
                    latitud = 15.0311,
                    longitud = -91.6365
                )
                // Solo para previsualizar la pantalla, usamos un NavController simulado
                PantallaInformacionDetallada(rememberNavController(), destino = destino)
            }
        }
    }
}

// Composable para mostrar la pantalla de información detallada
@Composable
fun PantallaInformacionDetallada(
    navController: NavHostController, // Recibimos el navController desde el NavGraph
    destino: Destino
) {
    // Scrollable column para permitir desplazamiento vertical
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Imagen del destino
        Image(
            painter = painterResource(id = destino.imagenResId),
            contentDescription = destino.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título del destino
        Text(
            text = destino.nombre,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sección: Cómo llegar
        InformacionSeccion(titulo = "Cómo llegar", descripcion = destino.comoLlegar)

        Spacer(modifier = Modifier.height(16.dp))

        // Sección: Qué llevar
        InformacionSeccion(titulo = "Qué llevar", descripcion = destino.queLlevar)

        Spacer(modifier = Modifier.height(16.dp))

        // Sección: Qué esperar
        InformacionSeccion(titulo = "Qué esperar", descripcion = destino.queEsperar)

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para regresar a la pantalla anterior
        Button(
            onClick = { navController.popBackStack() }, // Navegación para volver
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Volver a la lista")
        }
    }
}

// Componente para mostrar una sección de información
@Composable
fun InformacionSeccion(titulo: String, descripcion: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título de la sección
            Text(
                text = titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción de la sección
            Text(
                text = descripcion,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaInformacionDetallada() {
    val destino = Destino(
        nombre = "Cimarron",
        descripcion = "Un hermoso monumento natural en medio del desierto.",
        imagenResId = R.drawable.cimarron,
        comoLlegar = "Puedes llegar en autobús desde Ciudad de Guatemala hasta huehuetengango y luego tomar transporte hacia el cimarron.",
        queLlevar = "Lleva ropa cómoda, protector solar y suficiente agua.",
        queEsperar = "Naturaleza increíble, senderos y caminata de 2 horas.",
        latitud = 15.0311,
        longitud = -91.6365
    )
    GuateRütasTheme{
        // En la vista previa, simulamos el NavController
        PantallaInformacionDetallada(rememberNavController(), destino)
    }
}
