package com.pmd.rentavehiculos

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.pmd.rentavehiculos.data.model.Persona
import com.pmd.rentavehiculos.data.service.RetrofitClient.RetrofitService
import com.pmd.rentavehiculos.ui.theme.RentaVehiculosTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val personas = arrayOf(
            Persona("1", "Jose", "Apellido", 20, "Calle Falsa 123", 123456789, "DNI", "12345678A"),
            Persona("2", "Carlos", "Mendoza", 18, "Avenida Siempre Viva 742", 987654321, "DNI", "87654321B"),
            Persona("3", "Pepe", "Pepito", 25, "Plaza Mayor 1", 555555555, "DNI", "11223344C"),
            Persona("4", "Maximus", "Max", 19, "Calle del Imperio 9", 444444444, "DNI", "99887766D")
        )


        val service = RetrofitService.retrofitService()
        lifecycleScope.launch {
            val vehiculo = service.listaVehiculosDisponibles("30012025005447394", "disponibles")
            println(vehiculo)
        }

        setContent {
            RentaVehiculosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Llamada al composable Greeting
                    Greeting(
                        name = "Clase Android",
                        modifier = Modifier.padding(innerPadding)
                    )

                    // Otras llamadas a composables (comentadas)
                    /*
                    LazyColumn {
                        items(nombres) {
                            ListarItem(it, modifier = Modifier.padding(innerPadding))
                        }
                    }
                    LazyColumn {
                        items(personas) {
                            ListarPersona(it, modifier = Modifier.padding(innerPadding))
                        }
                    }
                    RegistroUsuarioForm("Prueba", modifier = Modifier.padding(innerPadding))
                    */
                }
            }
        }
    }
}

private fun RetrofitService.retrofitService() {
    TODO("Not yet implemented")
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(3.dp)
            .fillMaxWidth()
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.persona),
                contentDescription = "img persona",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Saludo",
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Hello $name!",
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { println("Hola Mariana") }
            ) {
                Text("Hola")
            }
            ButtonToast("Hola Mariana2")
        }
    }
}

@Composable
fun ButtonToast(mensaje: String) {
    val context = LocalContext.current
    FloatingActionButton(
        onClick = {
            Toast.makeText(
                context,
                mensaje,
                Toast.LENGTH_LONG
            ).show()
        },
        shape = CircleShape,
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Floating action button.")
    }
}

@Composable
fun ListarItem(nombre: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.persona),
                contentDescription = "img de persona",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
            Text(
                text = nombre,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}

@Composable
fun ListarPersona(persona: Persona, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.persona),
                contentDescription = "img de persona",
                modifier = Modifier
                    .width(85.dp)
                    .height(85.dp)
            )
            Text(
                text = persona.nombre,
                modifier = Modifier.padding(24.dp)
            )
            Text(
                text = persona.apellidos,
                modifier = Modifier.padding(24.dp)
            )
            Text(
                text = persona.edad.toString(),
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}

@Composable
fun RegistroUsuarioForm(nombre: String, modifier: Modifier = Modifier) {
    val usuario = remember { mutableStateOf(TextFieldValue()) }
    val primerNombre = remember { mutableStateOf(TextFieldValue()) }
    val primerApellido = remember { mutableStateOf(TextFieldValue()) }
    val segundoApellido = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = nombre,
            style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Registro de nuevo usuario")
        Spacer(modifier = Modifier.height(45.dp))
        TextField(
            label = { Text(text = "Usuario") },
            value = usuario.value,
            onValueChange = { usuario.value = it }
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            label = { Text(text = "Primer Nombre") },
            value = primerNombre.value,
            onValueChange = { primerNombre.value = it }
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            label = { Text(text = "Primer apellido") },
            value = primerApellido.value,
            onValueChange = { primerApellido.value = it }
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            label = { Text(text = "Segundo apellido") },
            value = segundoApellido.value,
            onValueChange = { segundoApellido.value = it }
        )
        Spacer(modifier = Modifier.height(65.dp))
        Box(modifier = Modifier.padding(horizontal = 40.dp)) {
            Button(
                onClick = {
                    crearUsuario(
                        usuario.value.text,
                        primerNombre.value.text,
                        primerApellido.value.text,
                        segundoApellido.value.text
                    )
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Crear")
            }
        }
    }
}

private fun crearUsuario(
    username: String,
    primerNombre: String,
    primerApellido: String = "",
    segundoApellido: String = ""
) {
    println("usuario registrado: $username - $primerNombre - $primerApellido - $segundoApellido")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RentaVehiculosTheme {
        Greeting("Android")
    }
}

