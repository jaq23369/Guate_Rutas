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

class MainActivity8 : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuateRütasTheme {
                val navController = rememberNavController()
                PantallaCachableInformacionDetalla(navController)
            }
        }
    }
}

@Composable
fun PantallaCachableInformacionDetalla(navController: NavHostController) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager
    val coroutineScope = rememberCoroutineScope()

    // Estado de destino recuperado desde el caché
    var destino by remember { mutableStateOf<Destino?>(null) }
    val cachedData by preferencesManager.getCachedScreenData(context, "pantalla8").collectAsState(initial = null)

    LaunchedEffect(Unit) {
        cachedData?.let { data ->
            destino = Gson().fromJson(data, Destino::class.java)
        }

        // Simulación: Descargar datos si hay conexión (sustituir lógica por verificación de internet)
        if (destino == null) {
            val nuevoDestino = Destino(
                nombre = "Playa Blanca",
                descripcion = "Un hermoso monumento natural en medio del desierto.",
                imagenResId = R.drawable.plbl,
                comoLlegar = "Para llegar por tierra hay que tomar la Carretera al Atlántico pasando por El Rancho, Río Hondo, y el cruce de Zacapa e Izabal. Muy cerca de esta playa, se encuentra uno de los lugares turísticos más reconocidos del lugar Siete Altares.",
                queLlevar = "Lleva ropa cómoda, protector solar.",
                queEsperar = "Vistas hermosa, frescura del mar  y un buen tiempo.",
                latitud = 15.0311,
                longitud = -91.6365
            )
            destino = nuevoDestino

            // Guardar en caché
            val dataToCache = Gson().toJson(nuevoDestino)
            coroutineScope.launch {
                preferencesManager.saveScreenData(context, "pantalla8", dataToCache)
            }
        }
    }

    // Mostrar pantalla
    if (destino != null) {
        PantallaInformacionDetalla(navController, destino!!)
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
fun PantallaInformacionDetalla(
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

        InformacionSecci(titulo = "Cómo llegar", descripcion = destino.comoLlegar)
        Spacer(modifier = Modifier.height(16.dp))

        InformacionSecci(titulo = "Qué llevar", descripcion = destino.queLlevar)
        Spacer(modifier = Modifier.height(16.dp))

        InformacionSecci(titulo = "Qué esperar", descripcion = destino.queEsperar)
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
fun InformacionSecci(titulo: String, descripcion: String) {
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
fun PreviewPantallaInformacionDetalla() {
    val destino = Destino(
        nombre = "Play Blanca",
        descripcion = "Un hermoso monumento natural en medio del desierto.",
        imagenResId = R.drawable.plbl,
        comoLlegar = "Para llegar por tierra hay que tomar la Carretera al Atlántico pasando por El Rancho, Río Hondo, y el cruce de Zacapa e Izabal. Muy cerca de esta playa, se encuentra uno de los lugares turísticos más reconocidos del lugar Siete Altares.",
        queLlevar = "Lleva ropa cómoda, protector solar.",
        queEsperar = "Vistas hermosa, frescura del mar  y un buen tiempo.",
        latitud = 15.0311,
        longitud = -91.6365
    )
    GuateRütasTheme {
        PantallaInformacionDetallada(rememberNavController(), destino)
    }
}