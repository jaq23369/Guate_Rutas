package com.example.guatertas

import android.annotation.SuppressLint
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperar la última pantalla visitada al iniciar
        val lastVisitedScreen = runBlocking {
            PreferencesManager.obtenerUltimaPantalla(this@MainActivity)
        } ?: "main"

        setContent {
            GuateRütasTheme {
                val navController = rememberNavController()

                // Escuchar cambios de destino en NavController
                LaunchedEffect(navController) {
                    navController.addOnDestinationChangedListener { _, destination, _ ->
                        val currentRoute = destination.route
                        if (currentRoute != null) {
                            launch {
                                PreferencesManager.guardarUltimaPantalla(this@MainActivity, currentRoute)
                            }
                        }
                    }
                }

                AppWithDualDrawer(navController, lastVisitedScreen)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppWithDualDrawer(navController: NavHostController, startDestination: String) {
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
                    RightDrawerContent(
                        closeDrawer = {
                            coroutineScope.launch { rightDrawerState.close() }
                        },
                        onLogout = {
                            // Manejo del cierre de sesión
                            val context = navController.context
                            val signInIntent = Intent(context, SignInActivity::class.java)
                            signInIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(signInIntent)
                        }
                    )
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
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("main") {
                            PantallaDescubrimientoDestinosApp(navController)
                        }
                        composable("pantalla2") {
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
                            PantallaInformacionDetallada(navController, destino)
                        }
                        composable(route = "pantalla3") {
                            PantallaPersonalizaUbicacion()
                        }
                        composable("pantalla4") {
                            PantallaPlanificacionViajes()
                        }
                        composable("pantalla5") {
                            PantallaApoyoComunidadLocal(navController)
                        }
                        composable("pantalla6") {
                            PantallaNotificacionesYAlertas(navController)
                        }

                        // Composable para pantalla de SignIn
                        composable("signIn") {
                            SignInScreen(auth = FirebaseAuth.getInstance()) { isSignedIn ->
                                if (isSignedIn) {
                                    navController.navigate("main") {
                                        popUpTo("main") { inclusive = true }
                                    }
                                }
                            }
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
            Destino(
                nombre = "El cimarron",
                descripcion = "Un hermoso monumento natural en medio del desierto.",
                imagenResId = R.drawable.cimarron,
                latitud = 15.0311,
                longitud = -91.6365
            ),
            Destino(
                nombre = "Playa blanca",
                descripcion = "Una playa preciosa en el atlántico.",
                imagenResId = R.drawable.plbl,
                latitud = 15.7835,
                longitud = -88.9947
            ),
            Destino(
                nombre = "Laguna Ordoñez",
                descripcion = "Un destino de aventuras con impresionantes paisajes montañosos.",
                imagenResId = R.drawable.lagordo,
                latitud = 14.5289,
                longitud = -89.3781
            )
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
                            android.R.drawable.ic_menu_gallery,
                            latitud = 15.7153, // Ejemplo de latitud
                            longitud = -91.8726 // Ejemplo de longitud
                        )
                    )
                },
                onDestinoClick = { destino ->
                    navController.navigate("pantalla2")
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


