package br.com.fiap.EcoWatts.screens

import android.util.Log // <-- Import necessário para o teste
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.EcoWatts.R
import br.com.fiap.EcoWatts.components.ButtomAppBar
import br.com.fiap.EcoWatts.components.EcoWattsTopAppBar
import br.com.fiap.EcoWatts.navigation.Destination
import br.com.fiap.EcoWatts.repository.RoomUserRepository
import br.com.fiap.EcoWatts.repository.SessionRepository
import br.com.fiap.EcoWatts.repository.WeatherRepository // <-- Import do seu novo repositório
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val sessionRepository = remember { SessionRepository(context) }
    val userRepository = remember { RoomUserRepository(context) }

    // 1. Instanciamos o repositório da API
    val weatherRepository = remember { WeatherRepository() }

    var userName by remember { mutableStateOf("Carregando...") }
    var userEmail by remember { mutableStateOf("") }
    var userImage by remember { mutableStateOf<ByteArray?>(null) }
    var weatherTemp by remember { mutableStateOf("Carregando clima...") }
    var weatherDesc by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val loggedInId = sessionRepository.getUserId()

        if (loggedInId != 0) {
            val user = userRepository.getUser(loggedInId)

            if (user != null) {
                userName = user.name
                userEmail = user.email
                userImage = user.userImage

                try {
                    val cidade = user.city
                    val resultado = weatherRepository.getWeatherForCity(cidade)

                    if (resultado != null) {
                        val clima = resultado.first
                        val local = resultado.second

                        val regiao = local.state ?: local.country

                        weatherTemp = "${clima.temperature}°C"
                        weatherDesc = "Vento: ${clima.windSpeed} km/h em ${local.name} - $regiao"
                    } else {
                        weatherTemp = "Clima indisponível"
                        weatherDesc = "Cidade não encontrada"
                    }
                } catch (e: Exception) {
                    weatherTemp = "Erro ao carregar"
                    weatherDesc = "Verifique sua conexão"
                }
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                EcoWattsTopAppBar(
                    title = stringResource(R.string.hello, userName),
                    subtitle = userEmail,
                    navController = navController
                )
            },
            bottomBar = { ButtomAppBar(navController, "home") },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Destination.AddApplianceScreen.route) },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add button"
                    )
                }
            },
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card (
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Clima Atual",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = weatherTemp, // Aqui entra a temperatura da API!
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        if (weatherDesc.isNotEmpty()) {
                            Text(
                                text = weatherDesc,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    EcoWatssTheme() {
        HomeScreen(rememberNavController())
    }
}