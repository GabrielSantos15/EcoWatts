package br.com.fiap.EcoWatts.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.EcoWatts.R
import br.com.fiap.EcoWatts.components.ButtomAppBar
import br.com.fiap.EcoWatts.components.EcoWattsTopAppBar
import br.com.fiap.EcoWatts.model.Appliance
import br.com.fiap.EcoWatts.navigation.Destination
import br.com.fiap.EcoWatts.repository.RoomApplianceRepository
import br.com.fiap.EcoWatts.repository.RoomUserRepository
import br.com.fiap.EcoWatts.repository.SessionRepository
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme
import br.com.fiap.EcoWatts.util.formatConsumoKwh
import br.com.fiap.EcoWatts.util.formatCurrencyBRL
import br.com.fiap.EcoWatts.util.formatHorasDia
import br.com.fiap.EcoWatts.util.formatPotencia
import br.com.fiap.EcoWatts.util.getMonthlyCost
import br.com.fiap.EcoWatts.util.monthlyConsumptionKwh
import kotlinx.coroutines.launch

@Composable
fun AppliancesScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionRepository = remember { SessionRepository(context) }
    val applianceRepository = remember { RoomApplianceRepository(context) }
    val userRepository = remember { RoomUserRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    var aparelhos by remember { mutableStateOf(listOf<Appliance>()) }
    var aparelhoSelecionado by remember { mutableStateOf<Appliance?>(null) }
    var aparelhoParaExcluir by remember { mutableStateOf<Appliance?>(null) }
    var refreshTrigger by remember { mutableStateOf(0) }
    var precoKwh by remember { mutableStateOf(0.8) }

    LaunchedEffect(refreshTrigger) {
        val userId = sessionRepository.getUserId()
        aparelhos = applianceRepository.getAppliancesByUser(userId)
        precoKwh = userRepository.getUser(userId).precoKwh
    }

    GradientTopBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                EcoWattsTopAppBar(
                    title = stringResource(R.string.my_appliances),
                    subtitle = stringResource(R.string.track_your_energy_consumption),
                    navController = navController,
                    isWhiteText = true
                )
            },
            bottomBar = { ButtomAppBar(navController, "appliances") },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Destination.AddApplianceScreen.createRoute())
                    },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar aparelho")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (aparelhos.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Nenhum aparelho cadastrado.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                navController.navigate(Destination.AddApplianceScreen.createRoute())
                            }
                        ) {
                            Text(text = "Adicionar primeiro aparelho")
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(aparelhos, key = { it.id }) { aparelho ->
                            AparelhoCard(
                                aparelho = aparelho,
                                precoKwh = precoKwh,
                                onClick = { aparelhoSelecionado = aparelho }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(72.dp)) }
                    }
                }
            }
        }
    }

    aparelhoSelecionado?.let { aparelho ->
        AparelhoDetailDialog(
            aparelho = aparelho,
            precoKwh = precoKwh,
            onDismiss = { aparelhoSelecionado = null },
            onEdit = {
                aparelhoSelecionado = null
                navController.navigate(Destination.AddApplianceScreen.createRoute(aparelho.id))
            },
            onDelete = {
                aparelhoSelecionado = null
                aparelhoParaExcluir = aparelho
            }
        )
    }

    aparelhoParaExcluir?.let { aparelho ->
        AlertDialog(
            onDismissRequest = { aparelhoParaExcluir = null },
            title = { Text(text = "Excluir aparelho") },
            text = { Text(text = "Deseja realmente excluir \"${aparelho.name}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            applianceRepository.delete(aparelho)
                            aparelhoParaExcluir = null
                            refreshTrigger++
                        }
                    }
                ) {
                    Text(text = "Excluir", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { aparelhoParaExcluir = null }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}

@Composable
fun AparelhoCard(aparelho: Appliance, precoKwh : Double, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = aparelho.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatPotencia(aparelho.powerWatts),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = formatHorasDia(aparelho.hoursOfUsePerDay),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = formatConsumoKwh(aparelho.monthlyConsumptionKwh()),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${formatCurrencyBRL(aparelho.getMonthlyCost(precoKwh))}/mês",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun AparelhoDetailDialog(
    aparelho: Appliance,
    precoKwh : Double,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = aparelho.name) },
        text = {
            Column {
                Text(text = formatPotencia(aparelho.powerWatts))
                Text(text = formatHorasDia(aparelho.hoursOfUsePerDay))
                Text(
                    text = formatConsumoKwh(aparelho.monthlyConsumptionKwh()),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(text = "${formatCurrencyBRL(aparelho.getMonthlyCost(precoKwh))}/mês")
            }
        },
        confirmButton = {
            TextButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Editar")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Excluir", color = MaterialTheme.colorScheme.error)
                }
                TextButton(onClick = onDismiss) {
                    Text(text = "Fechar")
                }
            }
        }
    )
}

@Preview
@Composable
private fun AppliancesScreenPreview() {
    EcoWatssTheme {
        AppliancesScreen(rememberNavController())
    }
}