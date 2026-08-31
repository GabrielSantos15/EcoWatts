package br.com.fiap.EcoWatts.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.EcoWatts.R
import br.com.fiap.EcoWatts.components.ButtomAppBar
import br.com.fiap.EcoWatts.components.DashboardSummary
import br.com.fiap.EcoWatts.components.EcoWattsTopAppBar
import br.com.fiap.EcoWatts.components.TipCard
import br.com.fiap.EcoWatts.model.Appliance
import br.com.fiap.EcoWatts.navigation.Destination
import br.com.fiap.EcoWatts.repository.RoomApplianceRepository
import br.com.fiap.EcoWatts.repository.RoomUserRepository
import br.com.fiap.EcoWatts.repository.SessionRepository
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val sessionRepository = remember { SessionRepository(context) }
    val userRepository = remember { RoomUserRepository(context) }
    val applianceRepository = remember { RoomApplianceRepository(context) }

    var userName by remember { mutableStateOf("Carregando...") }
    var userEmail by remember { mutableStateOf("") }
    var userImage by remember { mutableStateOf<ByteArray?>(null) }
    var aparelhos by remember { mutableStateOf(listOf<Appliance>()) }



    LaunchedEffect(Unit) {
        val loggedInId = sessionRepository.getUserId()

        if (loggedInId != 0) {
            val user = userRepository.getUser(loggedInId)

            if (user != null) {
                userName = user.name
                userEmail = user.email
                userImage = user.userImage
            }

            aparelhos = applianceRepository.getAppliancesByUser(loggedInId)
        }
    }
    GradientTopBackground {

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                EcoWattsTopAppBar(
                    title = stringResource(R.string.hello, userName),
                    subtitle = userEmail,
                    navController = navController,
                    isWhiteText = true
                )
            },
            bottomBar = { ButtomAppBar(navController, "tela_home") },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Destination.AddApplianceScreen.createRoute()) },
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
                DashboardSummary(aparelhos = aparelhos)

                Spacer(modifier = Modifier.height(16.dp))

                TipCard()
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