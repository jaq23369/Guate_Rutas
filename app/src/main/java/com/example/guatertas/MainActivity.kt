package com.example.guatertas

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guatertas.ui.theme.GuateRütasTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuateRütasTheme {
                val navController = rememberNavController()
                AppWithDualDrawer(navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppWithDualDrawer(navController: NavHostController) {
    val leftDrawerState = rememberDrawerState(DrawerValue.Closed)
    val rightDrawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            if (leftDrawerState.isOpen) {
                LeftDrawerContent(navController = navController, closeDrawer = {
                    coroutineScope.launch { leftDrawerState.close() }
                })
            }
        },
        drawerState = leftDrawerState,
        gesturesEnabled = false
    ) {
        ModalNavigationDrawer(
            drawerContent = {
                if (rightDrawerState.isOpen) {
                    RightDrawerContent(closeDrawer = {
                        coroutineScope.launch { rightDrawerState.close() }
                    })
                }
            },
            drawerState = rightDrawerState,
            gesturesEnabled = false
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Descubre Guatemala") },
                        navigationIcon = {
                            IconButton(onClick = {
                                coroutineScope.launch { leftDrawerState.open() }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Abrir menú de navegación")
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                coroutineScope.launch { rightDrawerState.open() }
                            }) {
                                Icon(Icons.Default.AccountCircle, contentDescription = "Abrir opciones de cuenta")
                            }
                        }
                    )
                },
                content = { paddingValues ->
                    // Configuración del NavHost
                    NavHost(
                        navController = navController,
                        startDestination = "main",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        // Pantalla principal
                        composable("main") {
                            PantallaDescubrimientoDestinosApp(navController)
                        }
                        // Pantalla 2: Información detallada
                        composable("pantalla2") {
                            val destino = Destino(
                                nombre = "Cimarron",
                                descripcion = "Un hermoso monumento natural en medio del desierto.",
                                imagenResId = R.drawable.cimarron,
                                comoLlegar = "Puedes llegar en autobús desde Ciudad de Guatemala hasta huehuetengango y luego tomar transporte hacia el cimarron.",
                                queLlevar = "Lleva ropa cómoda, protector solar y suficiente agua.",
                                queEsperar = "Naturaleza increíble, senderos y caminata de 2 horas."
                            )
                            PantallaInformacionDetallada(navController, destino)
                        }
                        // Pantalla 3: Interacción Comunitaria
                        composable("pantalla3") {
                            PantallaInteraccionComunitaria(navController)
                        }
                        // Pantalla 4: Planificación de Viajes
                        composable("pantalla4") {
                            PantallaPlanificacionViajes(navController)
                        }
                        // Pantalla 5: Apoyo a la Comunidad Local
                        composable("pantalla5") {
                            PantallaApoyoComunidadLocal(navController)
                        }
                        // Pantalla 6: Notificaciones y Alertas
                        composable("pantalla6") {
                            PantallaNotificacionesYAlertas(navController)
                        }
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDescubrimientoDestinosApp(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val destinos = remember {
        mutableStateListOf(
            Destino("El cimarron", "Un hermoso monumento natural en medio del desierto.", R.drawable.cimarron),
            Destino("Playa blanca", "Una playa preciosa en el atlántico.", R.drawable.plbl),
            Destino("Laguna Ordoñez", "Un destino de aventuras con impresionantes paisajes montañosos.", R.drawable.lagordo)
        )
    }

    Scaffold(
        content = {
            PantallaDescubrimientoDestinos(
                destinos = destinos,
                onRefresh = {
                    destinos.add(
                        Destino(
                            "Laguna Brava",
                            "Un lago escondido en las montañas, perfecto para los amantes de la naturaleza.",
                            android.R.drawable.ic_menu_slideshow
                        )
                    )
                },
                onDestinoClick = { destino ->
                    navController.navigate("detalles/${destino.nombre}")
                },
                modifier = modifier.padding(it)
            )
        }
    )
}

@Composable
fun PantallaDescubrimientoDestinos(
    destinos: List<Destino>,
    onRefresh: () -> Unit,
    onDestinoClick: (Destino) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Descubre destinos únicos",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(destinos) { destino ->
                CardDestino(destino, onClick = { onDestinoClick(destino) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Button(
            onClick = { onRefresh() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Recargar")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ver más recomendaciones")
        }
    }
}

@Composable
fun CardDestino(destino: Destino, onClick: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = destino.imagenResId),
                contentDescription = destino.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = destino.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = destino.descripcion, fontSize = 14.sp)
        }
    }
}

