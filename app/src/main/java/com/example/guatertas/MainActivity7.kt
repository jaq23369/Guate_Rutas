package com.example.guatertas

import PreferencesManager
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
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.guatertas.ui.theme.GuateRütasTheme
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainActivity7 : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuateRütasTheme {
                val navController = rememberNavController()
                PantallaCachableInformacionDetallad(navController)
            }
        }
    }
}

@Composable
fun PantallaCachableInformacionDetallad(navController: NavHostController) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager
    val coroutineScope = rememberCoroutineScope()

    // Estado de destino recuperado desde el caché
    var destino by remember { mutableStateOf<Destino?>(null) }
    val cachedData by preferencesManager.getCachedScreenData(context, "pantalla7").collectAsState(initial = null)

    LaunchedEffect(Unit) {
        cachedData?.let { data ->
            destino = Gson().fromJson(data, Destino::class.java)
        }

        // Simulación: Descargar datos si hay conexión (sustituir lógica por verificación de internet)
        if (destino == null) {
            val nuevoDestino = Destino(
                nombre = "laguna Ordoñez",
                descripcion = "Un hermoso monumento natural en medio del desierto.",
                imagenResId = R.drawable.lagordo,
                comoLlegar = "Puedes llegar en autobús desde Ciudad de Guatemala hasta Huehuetenango y luego tomar transporte hacia el Cimarron.",
                queLlevar = "Lleva ropa cómoda, protector solar y suficiente agua.",
                queEsperar = "Naturaleza increíble, senderos y caminata de 2 horas.",
                latitud = 15.0311,
                longitud = -91.6365
            )
            destino = nuevoDestino

            // Guardar en caché
            val dataToCache = Gson().toJson(nuevoDestino)
            coroutineScope.launch {
                preferencesManager.saveScreenData(context, "pantalla7", dataToCache)
            }
        }
    }

    // Mostrar pantalla
    if (destino != null) {
        PantallaInformacionDetallad(navController, destino!!)
    } else {
        Text(
            text = "No hay datos disponibles. Por favor, conéctate a Internet.",
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}

@Composable
fun PantallaInformacionDetallad(
    navController: NavHostController,
    destino: Destino
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = destino.imagenResId),
            contentDescription = destino.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = destino.nombre,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        InformacionSeccio(titulo = "Cómo llegar", descripcion = destino.comoLlegar)
        Spacer(modifier = Modifier.height(16.dp))

        InformacionSeccio(titulo = "Qué llevar", descripcion = destino.queLlevar)
        Spacer(modifier = Modifier.height(16.dp))

        InformacionSeccio(titulo = "Qué esperar", descripcion = destino.queEsperar)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Volver a la lista")
        }
    }
}

@Composable
fun InformacionSeccio(titulo: String, descripcion: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = descripcion, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaInformacionDetallad() {
    val destino = Destino(
        nombre = "laguna Ordoñez",
        descripcion = "Un hermoso monumento natural en medio del desierto.",
        imagenResId = R.drawable.lagordo,
        comoLlegar = "Puedes llegar en autobús desde Ciudad de Guatemala hasta Huehuetenango y luego tomar transporte hacia el Cimarron.",
        queLlevar = "Lleva ropa cómoda, protector solar y suficiente agua.",
        queEsperar = "Naturaleza increíble, senderos y caminata de 2 horas.",
        latitud = 15.0311,
        longitud = -91.6365
    )
    GuateRütasTheme {
        PantallaInformacionDetallad(rememberNavController(), destino)
    }
}