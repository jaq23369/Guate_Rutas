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
        composable("pantalla7") {
            val destino = Destino(
                nombre = "laguna Ordoñez",
                descripcion = "Un espectacular cuerpo de agua rodeado de dunas y vegetación en el corazón del desierto.",
                imagenResId = R.drawable.lagordo,
                comoLlegar = "Se conduce por la Carretera Interamericana CA-1 y al llegar a la bifurcación de cuatro caminos se debe dirigir hacia Huehuetenango. Al cruzar Huehuetenango se debe de pasar por el municipio de Chiantla iniciando la cuesta por un trayecto de 12 kilómetros hasta el mirador Juan Diéguez Olaverri ubicado en los Cuchumatanes.",
                queLlevar = "Zapatos comodos, protector solar y suficiente agua.",
                queEsperar = "Naturaleza increíble, senderos y caminata de 3 horas.",
                latitud = 15.0311,
                longitud = -91.6365
            )
            PantallaInformacionDetallad(navController, destino)
        }
        composable("pantalla8") {
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
            PantallaInformacionDetalla(navController, destino)
        }
    }
}