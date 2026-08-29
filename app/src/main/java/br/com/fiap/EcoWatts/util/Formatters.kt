package br.com.fiap.EcoWatts.util

import br.com.fiap.EcoWatts.model.Appliance
import java.text.NumberFormat
import java.util.Locale

fun formatPotencia(potenciaWatts: Double): String {
    val valor = if (potenciaWatts == potenciaWatts.toLong().toDouble()) {
        potenciaWatts.toLong().toString()
    } else {
        String.format(Locale("pt", "BR"), "%.1f", potenciaWatts)
    }
    return "$valor W"
}

fun formatHorasDia(horasDia: Double): String {
    val sufixo = if (horasDia == 1.0) "hora/dia" else "horas/dia"
    val valor = if (horasDia == horasDia.toLong().toDouble()) {
        horasDia.toLong().toString()
    } else {
        String.format(Locale("pt", "BR"), "%.1f", horasDia)
    }
    return "$valor $sufixo"
}

fun formatConsumoKwh(consumoMensalKwh: Double): String {
    val valor = if (consumoMensalKwh == consumoMensalKwh.toLong().toDouble()) {
        consumoMensalKwh.toLong().toString()
    } else {
        String.format(Locale("pt", "BR"), "%.1f", consumoMensalKwh)
    }
    return "$valor kWh/mês"
}

fun formatCurrencyBRL(valor: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return formatter.format(valor)
}

fun Appliance.monthlyConsumptionKwh(): Double {
    return (powerWatts * hoursOfUsePerDay * 30) / 1000.0
}