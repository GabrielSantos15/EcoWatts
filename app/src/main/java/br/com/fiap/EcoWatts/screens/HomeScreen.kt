package br.com.fiap.EcoWatts.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.EcoWatts.navigation.Destination
import br.com.fiap.EcoWatts.repository.RoomUserRepository
import br.com.fiap.EcoWatts.repository.SessionRepository

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val sessionRepository = remember { SessionRepository(context) }
    val userRepository = remember { RoomUserRepository(context) }

    var userName by remember { mutableStateOf("Carregando...") }
    var userEmail by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val loggedInId = sessionRepository.getUserId()

        if (loggedInId != 0) {
            val user = userRepository.getUser(loggedInId)

            // Atualiza os textos da tela
            userName = user.name
            userEmail = user.email
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TELA HOME",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Bem-vindo(a), $userName!",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = userEmail,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                sessionRepository.logout()

                navController.navigate(Destination.InitialScreen.route) {
                    popUpTo(0)
                }
            }
        ) {
            Text(text = "Sair")
        }
    }
}