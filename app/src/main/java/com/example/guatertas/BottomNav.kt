package com.example.guatertas

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Descubrimiento", "main", Icons.Default.Home),
        BottomNavItem("Detalles", "detalles", Icons.Default.Info),
        BottomNavItem("Interacción", "interaccion_comunitaria", Icons.Default.People),
        BottomNavItem("Planificación", "planificacion", Icons.Default.Map),
        BottomNavItem("Comunidad", "apoyo_comunidad", Icons.Default.Favorite),
        BottomNavItem("Notificaciones", "notificaciones", Icons.Default.Notifications)
    )

    BottomNavigation {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(val label: String, val route: String, val icon: ImageVector)
