
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.guatertas.Destino
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun PantallaMapaItinerario(modifier: Modifier = Modifier) {
    // Implementación de la pantalla del mapa sin `itinerario`
    GoogleMap(
        modifier = modifier.fillMaxSize(),
        // Configuración del mapa
    ) {
        // Marcadores u otros elementos del mapa
    }
}


