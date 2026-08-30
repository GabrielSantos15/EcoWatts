package br.com.fiap.EcoWatts.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.fiap.EcoWatts.R
import br.com.fiap.EcoWatts.model.EnergyTip
import br.com.fiap.EcoWatts.model.WeatherTipData
import br.com.fiap.EcoWatts.repository.RoomUserRepository
import br.com.fiap.EcoWatts.repository.SessionRepository
import br.com.fiap.EcoWatts.repository.WeatherRepository
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme

@Composable
fun TipCard(modifier: Modifier = Modifier) {
    val weatherRepository = remember { WeatherRepository() }
    val context = LocalContext.current
    val sessionRepository = remember { SessionRepository(context) }
    val userRepository = remember { RoomUserRepository(context) }

    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var weatherData by remember { mutableStateOf<WeatherTipData?>(null) }

    // Strings resolvidas aqui em cima, fora do LaunchedEffect,
    // porque stringResource só pode ser chamado em contexto @Composable
    val extremeColdTitle = stringResource(R.string.tip_extreme_cold_title)
    val extremeColdMessage = stringResource(R.string.tip_extreme_cold_message)
    val heatersTitle = stringResource(R.string.tip_heaters_title)
    val heatersMessage = stringResource(R.string.tip_heaters_message)
    val coldShowerTitle = stringResource(R.string.tip_cold_shower_title)
    val coldShowerMessage = stringResource(R.string.tip_cold_shower_message)
    val mildWeatherTitle = stringResource(R.string.tip_mild_weather_title)
    val mildWeatherMessage = stringResource(R.string.tip_mild_weather_message)
    val daylightTitle = stringResource(R.string.tip_daylight_title)
    val daylightMessage = stringResource(R.string.tip_daylight_message)
    val moderateHeatTitle = stringResource(R.string.tip_moderate_heat_title)
    val moderateHeatMessage = stringResource(R.string.tip_moderate_heat_message)
    val fanBeforeAcTitle = stringResource(R.string.tip_fan_before_ac_title)
    val fanBeforeAcMessage = stringResource(R.string.tip_fan_before_ac_message)
    val extremeHeatTitle = stringResource(R.string.tip_extreme_heat_title)
    val extremeHeatMessage = stringResource(R.string.tip_extreme_heat_message)
    val fridgeTitle = stringResource(R.string.tip_fridge_title)
    val fridgeMessage = stringResource(R.string.tip_fridge_message)
    val rainyDayTitle = stringResource(R.string.tip_rainy_day_title)
    val rainyDayMessage = stringResource(R.string.tip_rainy_day_message)
    val stormTitle = stringResource(R.string.tip_storm_title)
    val stormMessage = stringResource(R.string.tip_storm_message)
    val fogTitle = stringResource(R.string.tip_fog_title)
    val fogMessage = stringResource(R.string.tip_fog_message)
    val sunnyDayTitle = stringResource(R.string.tip_sunny_day_title)
    val sunnyDayMessage = stringResource(R.string.tip_sunny_day_message)
    val loadingText = stringResource(R.string.weather_loading)
    val unavailableText = stringResource(R.string.weather_unavailable)

    LaunchedEffect(Unit) {
        val loggedInId = sessionRepository.getUserId()

        if (loggedInId != 0) {
            val user = userRepository.getUser(loggedInId)

            if (user != null) {
                try {
                    val cidade = user.city
                    val resultado = weatherRepository.getWeatherForCity(cidade)

                    if (resultado != null) {
                        val clima = resultado.first
                        val local = resultado.second
                        val regiao = local.state ?: local.country
                        val temp = clima.temperature
                        val codigo = clima.weatherCode

                        val (icon, corFundo) = when {
                            temp < 12 -> Icons.Default.AcUnit to Color(0xFF37474F)
                            temp < 18 -> Icons.Default.Cloud to Color(0xFF4A6FA5)
                            temp < 24 -> Icons.Default.WbSunny to Color(0xFF5C9EAD)
                            temp < 30 -> Icons.Default.WbSunny to Color(0xFFEF8C3C)
                            else -> Icons.Default.WbSunny to Color(0xFFD64545)
                        }

                        val tips = mutableListOf<EnergyTip>()

                        when {
                            temp < 12 -> {
                                tips.add(EnergyTip(extremeColdTitle, extremeColdMessage, Icons.Default.Shower))
                                tips.add(EnergyTip(heatersTitle, heatersMessage, Icons.Default.Bolt))
                            }
                            temp < 18 -> {
                                tips.add(EnergyTip(coldShowerTitle, coldShowerMessage, Icons.Default.Shower))
                            }
                            temp < 24 -> {
                                tips.add(EnergyTip(mildWeatherTitle, mildWeatherMessage, Icons.Default.WaterDrop))
                                tips.add(EnergyTip(daylightTitle, daylightMessage, Icons.Default.Lightbulb))
                            }
                            temp < 30 -> {
                                tips.add(EnergyTip(moderateHeatTitle, moderateHeatMessage, Icons.Default.Air))
                                tips.add(EnergyTip(fanBeforeAcTitle, fanBeforeAcMessage, Icons.Default.Thermostat))
                            }
                            else -> {
                                tips.add(EnergyTip(extremeHeatTitle, extremeHeatMessage, Icons.Default.Air))
                                tips.add(EnergyTip(fridgeTitle, fridgeMessage, Icons.Default.Thermostat))
                            }
                        }

                        when (codigo) {
                            in 51..67, in 80..82 -> {
                                tips.add(EnergyTip(rainyDayTitle, rainyDayMessage, Icons.Default.LocalLaundryService))
                            }
                            in 95..99 -> {
                                tips.add(EnergyTip(stormTitle, stormMessage, Icons.Default.Thunderstorm))
                            }
                            45, 48 -> {
                                tips.add(EnergyTip(fogTitle, fogMessage, Icons.Default.Air))
                            }
                            0 -> {
                                tips.add(EnergyTip(sunnyDayTitle, sunnyDayMessage, Icons.Default.WbSunny))
                            }
                        }

                        weatherData = WeatherTipData(
                            temperature = "${temp}°C",
                            locationInfo = "${local.name} - $regiao • Vento ${clima.windSpeed} km/h",
                            icon = icon,
                            backgroundColor = corFundo,
                            tips = tips
                        )
                        isLoading = false
                    } else {
                        hasError = true
                        isLoading = false
                    }
                } catch (e: Exception) {
                    hasError = true
                    isLoading = false
                }
            } else {
                hasError = true
                isLoading = false
            }
        }
    }

    AnimatedContent(
        targetState = Triple(isLoading, hasError, weatherData),
        label = "weather_tip_transition"
    ) { (loading, error, data) ->
        when {
            loading -> {
                    WeatherTipSkeleton(modifier = modifier)
            }

            error || data == null -> {
                Card(
                    modifier = modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = unavailableText,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            else -> {
                Column(modifier = modifier.fillMaxWidth()) {
                    WeatherInfoCard(data = data)
                    Spacer(modifier = Modifier.height(12.dp))
                    WeatherTipsCarousel(
                        tips = data.tips,
                        cardColor = data.backgroundColor
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TipCardPreview() {
    val sampleData = WeatherTipData(
        temperature = "15°C",
        locationInfo = "Boituva - SP • Vento 8 km/h",
        icon = Icons.Default.Cloud,
        backgroundColor = Color(0xFF4A6FA5),
        tips = listOf(
            EnergyTip(
                stringResource(R.string.tip_cold_shower_title),
                stringResource(R.string.tip_cold_shower_message),
                Icons.Default.Shower
            ),
            EnergyTip(
                stringResource(R.string.tip_rainy_day_title),
                stringResource(R.string.tip_rainy_day_message),
                Icons.Default.LocalLaundryService
            )
        )
    )

    EcoWatssTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            WeatherInfoCard(data = sampleData)
            Spacer(modifier = Modifier.height(12.dp))
            WeatherTipsCarousel(
                tips = sampleData.tips,
                cardColor = sampleData.backgroundColor
            )
        }
    }
}

@Composable
fun WeatherTipSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    SkeletonTextLine(width = 90.dp, height = 32.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    SkeletonTextLine(width = 160.dp)
                }
                SkeletonCircle(size = 48.dp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                SkeletonTextLine(width = 100.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SkeletonBox(modifier = Modifier.weight(1f), height = 90.dp, shape = RoundedCornerShape(16.dp))
                    SkeletonBox(modifier = Modifier.weight(1f), height = 90.dp, shape = RoundedCornerShape(16.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun WeatherTipSkeletonPreview() {
    EcoWatssTheme() {
        WeatherTipSkeleton()
    }
}