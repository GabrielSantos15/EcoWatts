package br.com.fiap.EcoWatts.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class EnergyTip(
    val title: String,
    val message: String,
    val icon: ImageVector
)

data class WeatherTipData(
    val temperature: String,
    val locationInfo: String,
    val icon: ImageVector,
    val backgroundColor: Color,
    val tips: List<EnergyTip>
)