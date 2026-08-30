package br.com.fiap.EcoWatts.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
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
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val sessionRepository = remember { SessionRepository(context) }
    val userRepository = remember { RoomUserRepository(context) }

    var userName by remember { mutableStateOf("Carregando...") }
    var userEmail by remember { mutableStateOf("") }
    var userImage by remember { mutableStateOf<ByteArray?>(null) }

    LaunchedEffect(Unit) {
        val loggedInId = sessionRepository.getUserId()

        if (loggedInId != 0) {
            val user = userRepository.getUser(loggedInId)

            if (user != null) {
                userName = user.name
                userEmail = user.email
                userImage = user.userImage
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
            bottomBar = { ButtomAppBar(navController, "tela_home") },
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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TELA HOME",
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

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
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    EcoWatssTheme() {
        HomeScreen(rememberNavController())
    }
}