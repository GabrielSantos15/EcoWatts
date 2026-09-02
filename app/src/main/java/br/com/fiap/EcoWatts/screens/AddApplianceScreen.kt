package br.com.fiap.EcoWatts.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.EcoWatts.R
import br.com.fiap.EcoWatts.components.BottomStartCard
import br.com.fiap.EcoWatts.components.TopEndCard
import br.com.fiap.EcoWatts.model.Appliance
import br.com.fiap.EcoWatts.navigation.Destination
import br.com.fiap.EcoWatts.repository.RoomApplianceRepository
import br.com.fiap.EcoWatts.repository.RoomUserRepository
import br.com.fiap.EcoWatts.repository.SessionRepository
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme
import kotlinx.coroutines.launch

@Composable
fun AddApplianceScreen(navController: NavController, applianceId: Int? = null) {
    val context = LocalContext.current
    val sessionRepository = SessionRepository(context)
    val userRepository = RoomUserRepository(context)
    val applianceRepository = RoomApplianceRepository(context)
    val coroutineScope = rememberCoroutineScope()

    val emEdicao = applianceId != null
    var idOriginal by remember { mutableStateOf(0) }
    var userIdOriginal by remember { mutableStateOf(0) }

    var nome by remember { mutableStateOf("") }
    var potencia by remember { mutableStateOf("") }
    var horasPorDia by remember { mutableStateOf("") }

    var isNomeError by remember { mutableStateOf(false) }
    var isPotenciaError by remember { mutableStateOf(false) }
    var isHorasError by remember { mutableStateOf(false) }

    var showDialogError by remember { mutableStateOf(false) }
    var showDialogSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(applianceId) {
        if (applianceId != null) {
            val appliance = applianceRepository.getApplianceById(applianceId)
            if (appliance != null) {
                idOriginal = appliance.id
                userIdOriginal = appliance.userId
                nome = appliance.name
                potencia = if (appliance.powerWatts % 1.0 == 0.0)
                    appliance.powerWatts.toLong().toString() else appliance.powerWatts.toString()
                horasPorDia = if (appliance.hoursOfUsePerDay % 1.0 == 0.0)
                    appliance.hoursOfUsePerDay.toLong()
                        .toString() else appliance.hoursOfUsePerDay.toString()
            }
        }
    }
    fun validate(): Boolean {
        isNomeError = nome.isBlank()
        isPotenciaError =
            potencia.toDoubleOrNull() == null || (potencia.toDoubleOrNull() ?: 0.0) <= 0.0
        isHorasError = horasPorDia.toDoubleOrNull() == null || (horasPorDia.toDoubleOrNull()
            ?: 0.0) <= 0.0 || (horasPorDia.toDoubleOrNull() ?: 0.0) > 24.0
        return !isNomeError && !isPotenciaError && !isHorasError
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        TopEndCard(modifier = Modifier.align(Alignment.TopEnd))
        BottomStartCard(modifier = Modifier.align(Alignment.BottomStart))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 16.dp, start = 8.dp)

            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(90.dp))

            Text(
                text = stringResource(R.string.add_appliance_title),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Left
            )
            Text(
                text = stringResource(R.string.add_appliance_subtitle),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Left
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
                label = {
                    Text(
                        text = stringResource(R.string.appliance_name_label),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Devices,
                        contentDescription = null,
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
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = stringResource(R.string.error_icon_description)
                        )
                    }
                },
                supportingText = {
                    if (isNomeError) {
                        Text(
                            text = stringResource(R.string.error_invalid_name),
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
                label = {
                    Text(
                        text = stringResource(R.string.power_watts_label),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
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
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = stringResource(R.string.error_icon_description)
                        )
                    }
                },
                supportingText = {
                    if (isPotenciaError) {
                        Text(
                            text = stringResource(R.string.error_invalid_watts),
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
                label = {
                    Text(
                        text = stringResource(R.string.hours_per_day_label),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
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
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = stringResource(R.string.error_icon_description)
                        )
                    }
                },
                supportingText = {
                    if (isHorasError) {
                        Text(
                            text = stringResource(R.string.error_invalid_hours),
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
                        val userId = if (emEdicao) userIdOriginal else sessionRepository.getUserId()
                        val user = userRepository.getUser(userId)

                        val w = potencia.toDouble()
                        val h = horasPorDia.toDouble()

                        coroutineScope.launch {
                            if (emEdicao) {
                                applianceRepository.update(
                                    Appliance(
                                        id = idOriginal,
                                        name = nome,
                                        powerWatts = w,
                                        hoursOfUsePerDay = h,
                                        userId = userId
                                    )
                                )
                            } else {
                                applianceRepository.insert(
                                    Appliance(
                                        name = nome,
                                        powerWatts = w,
                                        hoursOfUsePerDay = h,
                                        userId = userId
                                    )
                                )
                            }
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
                    text = stringResource(if (emEdicao) R.string.save_changes else R.string.save_appliance),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }


    // Dialogo de Sucesso
    if (showDialogSuccess) {
        AlertDialog(
            onDismissRequest = { showDialogSuccess = false },
            title = { Text(text = stringResource(R.string.success_title)) },
            text = {
                Text(
                    text = stringResource(
                        if (emEdicao) R.string.appliance_updated_success
                        else R.string.appliance_created_success
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialogSuccess = false
                        navController.navigate(Destination.HomeScreen.route) {
                            popUpTo(Destination.HomeScreen.route) { inclusive = true }
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        )
    }

    // Dialogo de Erro
    if (showDialogError) {
        AlertDialog(
            onDismissRequest = { showDialogError = false },
            title = { Text(text = stringResource(R.string.validation_error_title)) },
            text = { Text(text = stringResource(R.string.validation_error_message)) },
            confirmButton = {
                TextButton(
                    onClick = { showDialogError = false }
                ) {
                    Text(stringResource(R.string.ok))
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