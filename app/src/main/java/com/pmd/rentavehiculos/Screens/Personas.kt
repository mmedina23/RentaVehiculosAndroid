import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.Api.RetrofitInstance
import com.pmd.rentavehiculos.Entity.Persona

@Composable
fun PersonasFunction() {
    // API Service instance
    val service = RetrofitInstance.makeRetrofitService()

    // State to hold the list of personas, loading state, and error message
    var personas by remember { mutableStateOf<List<Persona>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // LaunchedEffect to fetch data when composable is first launched
    LaunchedEffect(Unit) {
        try {
            // Fetch personas data from API
            val result = service.obtenerpersonas("31012025230930787")
            personas = result
            isLoading = false
        } catch (e: Exception) {
            // Handle error if API call fails
            errorMessage = "Error: ${e.message}"
            isLoading = false
        }
    }

    // Display UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display loading message while data is being fetched
        if (isLoading) {
            Text("Loading...", style = MaterialTheme.typography.bodyLarge)
        } else {
            // Display error message if any
            errorMessage?.let {
                Text(it, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
            }

            // Display the list of personas
            if (personas.isNotEmpty()) {
                personas.forEach { persona ->
                    PersonaItem(persona)
                }
            } else {
                Text("No personas found.", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

// A Composable to display individual Persona details
@Composable
fun PersonaItem(persona: Persona) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text("Id: ${persona.id}", style = MaterialTheme.typography.bodyMedium)
        Text("Nombre: ${persona.nombre}", style = MaterialTheme.typography.bodyMedium)
        Text("Apellido: ${persona.apellido}", style = MaterialTheme.typography.bodyMedium)
        Text("Direccion: ${persona.direccion}", style = MaterialTheme.typography.bodyMedium)
        Text("Telefono: ${persona.telefono}", style = MaterialTheme.typography.bodyMedium)
        Text("TipoIdentificacion: ${persona.tipoIdentificacion}", style = MaterialTheme.typography.bodyMedium)
        Text("Identificacion: ${persona.identificacion}", style = MaterialTheme.typography.bodyMedium)

        /*persona.apellido?.let { Text("Apellidos: $it", style = MaterialTheme.typography.bodyMedium)} ?: Text("No dispnible", style = MaterialTheme.typography.bodyMedium)
        persona.direccion?.let { Text("Direccion: $it", style = MaterialTheme.typography.bodyMedium) }
        persona.telefono?.let { Text("Telefono: $it", style = MaterialTheme.typography.bodyMedium) }
        persona.tipoIdentificacion?.let { Text("Tipo Identificación: $it", style = MaterialTheme.typography.bodyMedium) }
        persona.identificacion?.let { Text("Identificación: $it", style = MaterialTheme.typography.bodyMedium) }*/
    }
}