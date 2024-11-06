package com.example.guatertas

import PantallaMapaItinerario
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            PantallaDescubrimientoDestinosApp(navController)
        }

        composable(route = "pantallaMapaItinerario") {
            PantallaMapaItinerario()
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
            PantallaInformacionDetallada(navController = navController, destino = destino)
        }

        composable(route = "pantalla3") { backStackEntry ->
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
    }
}



