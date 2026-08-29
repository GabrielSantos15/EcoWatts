package br.com.fiap.EcoWatts.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.EcoWatts.model.Appliance
import br.com.fiap.EcoWatts.navigation.Destination
import br.com.fiap.EcoWatts.repository.RoomApplianceRepository
import br.com.fiap.EcoWatts.repository.RoomUserRepository
import br.com.fiap.EcoWatts.repository.SessionRepository
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme
import kotlinx.coroutines.launch

@Composable
fun AddApplianceScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionRepository = SessionRepository(context)
    val userRepository = RoomUserRepository(context)
    val applianceRepository = RoomApplianceRepository(context)
    val coroutineScope = rememberCoroutineScope()

    var nome by remember { mutableStateOf("") }
    var potencia by remember { mutableStateOf("") }
    var horasPorDia by remember { mutableStateOf("") }

    // Estados de erro
    var isNomeError by remember { mutableStateOf(false) }
    var isPotenciaError by remember { mutableStateOf(false) }
    var isHorasError by remember { mutableStateOf(false) }

    var showDialogError by remember { mutableStateOf(false) }
    var showDialogSuccess by remember { mutableStateOf(false) }

    fun validate(): Boolean {
        isNomeError = nome.isBlank()
        isPotenciaError = potencia.toDoubleOrNull() == null || (potencia.toDoubleOrNull() ?: 0.0) <= 0.0
        isHorasError = horasPorDia.toDoubleOrNull() == null || (horasPorDia.toDoubleOrNull() ?: 0.0) <= 0.0 || (horasPorDia.toDoubleOrNull() ?: 0.0) > 24.0
        return !isNomeError && !isPotenciaError && !isHorasError
    }

    Scaffold(
        bottomBar = {
//            BottomAppBar(navController = navController, rotaAtual = "tela_adicionar")
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Cadastrar Aparelho",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Informe os dados para calcular o custo mensal",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Campo Nome do Aparelho
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    label = { Text(text = "Nome do Aparelho", style = MaterialTheme.typography.labelSmall) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Devices,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    isError = isNomeError,
                    trailingIcon = {
                        if (isNomeError) {
                            Icon(imageVector = Icons.Default.Error, contentDescription = "")
                        }
                    },
                    supportingText = {
                        if (isNomeError) {
                            Text(
                                text = "Informe um nome válido",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo Potência (Watts)
                OutlinedTextField(
                    value = potencia,
                    onValueChange = { potencia = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    label = { Text(text = "Potência (Watts)", style = MaterialTheme.typography.labelSmall) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    isError = isPotenciaError,
                    trailingIcon = {
                        if (isPotenciaError) {
                            Icon(imageVector = Icons.Default.Error, contentDescription = "")
                        }
                    },
                    supportingText = {
                        if (isPotenciaError) {
                            Text(
                                text = "Informe um valor em Watts válido",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo Horas de uso por dia
                OutlinedTextField(
                    value = horasPorDia,
                    onValueChange = { horasPorDia = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    label = { Text(text = "Horas de uso por dia", style = MaterialTheme.typography.labelSmall) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    isError = isHorasError,
                    trailingIcon = {
                        if (isHorasError) {
                            Icon(imageVector = Icons.Default.Error, contentDescription = "")
                        }
                    },
                    supportingText = {
                        if (isHorasError) {
                            Text(
                                text = "Informe entre 1 e 24 horas",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (validate()) {
                            val userId = sessionRepository.getUserId()
                            val user = userRepository.getUser(userId)
                            val precoKwh = user?.precoKwh ?: 0.8

                            val w = potencia.toDouble()
                            val h = horasPorDia.toDouble()

                            // Fórmula: (Watts * Horas * 30 dias / 1000) * Preço_do_kWh
                            val custoMensal = ((w * h * 30) / 1000.0) * precoKwh

                            coroutineScope.launch {
                                applianceRepository.insert(
                                    Appliance(
                                        name = nome,
                                        powerWatts = w,
                                        hoursOfUsePerDay = h,
                                        monthlyCost = custoMensal,
                                        userId = userId
                                    )
                                )
                                showDialogSuccess = true
                            }
                        } else {
                            showDialogError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Salvar Aparelho",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }

    // Dialogo de Sucesso
    if (showDialogSuccess) {
        AlertDialog(
            onDismissRequest = { showDialogSuccess = false },
            title = { Text(text = "Sucesso") },
            text = { Text(text = "Aparelho cadastrado e calculado com sucesso!") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialogSuccess = false
                        navController.navigate(Destination.HomeScreen.route) {
                            popUpTo(Destination.HomeScreen.route) { inclusive = true }
                        }
                    }
                ) {
                    Text(text = "Ok")
                }
            }
        )
    }

    // Dialogo de Erro
    if (showDialogError) {
        AlertDialog(
            onDismissRequest = { showDialogError = false },
            title = { Text(text = "Erro de Validação") },
            text = { Text(text = "Por favor, preencha todos os campos corretamente.") },
            confirmButton = {
                TextButton(
                    onClick = { showDialogError = false }
                ) {
                    Text("Ok")
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun AddApplianceScreenPreview() {
    EcoWatssTheme {
        AddApplianceScreen(rememberNavController())
    }
}