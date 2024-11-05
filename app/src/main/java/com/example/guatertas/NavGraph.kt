package com.example.guatertas

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        // Pantalla principal
        composable("main") {
            PantallaDescubrimientoDestinosApp(navController)
        }

        // Navegación a cada pantalla adicional
        composable("pantalla2") {
            val destino = Destino(
                nombre = "Cimarron",
                descripcion = "Un hermoso monumento natural en medio del desierto.",
                imagenResId = R.drawable.cimarron,
                comoLlegar = "Puedes llegar en autobús desde Ciudad de Guatemala hasta huehuetengango y luego tomar transporte hacia el cimarron.",
                queLlevar = "Lleva ropa cómoda, protector solar y suficiente agua.",
                queEsperar = "Naturaleza increíble, senderos y caminata de 2 horas."
            )
            PantallaInformacionDetallada(navController = navController, destino = destino)
        }

        // Agregar navegación para otras pantallas
        composable("pantalla3") {
            PantallaInteraccionComunitaria(navController)
        }

        composable("pantalla4") {
            PantallaPlanificacionViajes(navController)
        }

        composable("pantalla5") {
            PantallaApoyoComunidadLocal(navController)
        }

        composable("pantalla6") {
            PantallaNotificacionesYAlertas(navController)
        }
    }
}


