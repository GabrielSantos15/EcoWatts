package br.com.fiap.EcoWatts.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
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
import br.com.fiap.EcoWatts.components.*
import br.com.fiap.EcoWatts.model.Appliance
import br.com.fiap.EcoWatts.model.User
import br.com.fiap.EcoWatts.navigation.Destination
import br.com.fiap.EcoWatts.repository.RoomApplianceRepository
import br.com.fiap.EcoWatts.repository.RoomUserRepository
import br.com.fiap.EcoWatts.repository.SessionRepository
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionRepository = remember { SessionRepository(context) }
    val applianceRepository = remember { RoomApplianceRepository(context) }
    val userRepository = remember { RoomUserRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    var user by remember { mutableStateOf(User() )}
    var aparelhos by remember { mutableStateOf(listOf<Appliance>()) }
    var refreshTrigger by remember { mutableStateOf(0) }
    var kwhPrice by remember { mutableStateOf(0.0) }
    var totalCost by remember { mutableStateOf(0.0) }

    LaunchedEffect(refreshTrigger) {
        val userId = sessionRepository.getUserId()
        user = userRepository.getUser(userId)
        aparelhos = applianceRepository.getAppliancesByUser(userId)

        totalCost = applianceRepository.getTotalMonthlyCostByUser(userId)
        kwhPrice = user.precoKwh
    }

    GradientTopBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                EcoWattsTopAppBar(
                    title = stringResource(R.string.dashboard),
                    subtitle = stringResource(R.string.consumption_summary),
                    navController = navController,
                    isWhiteText = true
                )
            },
            bottomBar = { ButtomAppBar(navController, "dashboard") },
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                if (aparelhos.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Nenhum aparelho cadastrado",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    TotalCostHeroCard(
                        totalMonthlyCost = totalCost,
                        applianceCount = aparelhos.size
                    )

                    KwhPriceEditor(
                        currentPrice = kwhPrice,
                        onPriceSave = { newPrice ->
                            kwhPrice = newPrice
                            coroutineScope.launch {
                                val usuarioAtualizado = user.copy(precoKwh = newPrice)
                                userRepository.update(usuarioAtualizado)
                            }
                        }
                    )

                    ConsumptionDonutChart(
                        appliances = aparelhos,
                        kwhPrice = kwhPrice
                    )

                    ApplianceRankingCard(
                        appliances = aparelhos,
                        kwhPrice = kwhPrice
                    )

                    Spacer(modifier = Modifier.height(34.dp))
                }
            }
        }
    }
}

@Composable
private fun DashboardScreenPreview() {
    EcoWatssTheme {
        DashboardScreen(rememberNavController())
    }
}