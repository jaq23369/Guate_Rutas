package com.example.guatertas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppWithDualDrawer(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val leftDrawerState = rememberDrawerState(DrawerValue.Closed)
    val rightDrawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            LeftDrawerContent(navController = navController, closeDrawer = {
                coroutineScope.launch { leftDrawerState.close() }
            })
        },
        drawerState = leftDrawerState,
        gesturesEnabled = false
    ) {
        ModalNavigationDrawer(
            drawerContent = {
                RightDrawerContent(closeDrawer = {
                    coroutineScope.launch { rightDrawerState.close() }
                })
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
                content = content
            )
        }
    }
}

@Composable
fun LeftDrawerContent(navController: NavHostController, closeDrawer: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC)) // Fondo azul claro
            .padding(16.dp)
    ) {
        Text("Home", fontSize = 18.sp, modifier = Modifier.clickable {
            navController.navigate("main")
            closeDrawer()
        })
        Spacer(modifier = Modifier.height(16.dp))

        Text("Pantalla 2", fontSize = 18.sp, modifier = Modifier.clickable {
            navController.navigate("pantalla2")
            closeDrawer()
        })
        Spacer(modifier = Modifier.height(16.dp))

        Text("Pantalla 3", fontSize = 18.sp, modifier = Modifier.clickable {
            navController.navigate("pantalla3")
            closeDrawer()
        })
        Spacer(modifier = Modifier.height(16.dp))

        Text("Pantalla 4", fontSize = 18.sp, modifier = Modifier.clickable {
            navController.navigate("pantalla4")
            closeDrawer()
        })
        Spacer(modifier = Modifier.height(16.dp))

        Text("Pantalla 5", fontSize = 18.sp, modifier = Modifier.clickable {
            navController.navigate("pantalla5")
            closeDrawer()
        })
        Spacer(modifier = Modifier.height(16.dp))

        Text("Pantalla 6", fontSize = 18.sp, modifier = Modifier.clickable {
            navController.navigate("pantalla6")
            closeDrawer()
        })
    }
}

@Composable
fun RightDrawerContent(closeDrawer: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Fondo gris claro
            .padding(16.dp)
    ) {
        Text("Ajustes de cuenta", fontSize = 18.sp, modifier = Modifier.clickable {
            // Lógica para ajustes de cuenta
            closeDrawer()
        })
        Spacer(modifier = Modifier.height(16.dp))

        Text("Cerrar sesión", fontSize = 18.sp, modifier = Modifier.clickable {
            // Lógica para cerrar sesión
            closeDrawer()
        })
    }
}
