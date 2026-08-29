package br.com.fiap.EcoWatts.model

package br.com.fiap.EcoWatts.model

data class Aparelho(
    val id: Int = 0,
    val nome: String = "",
    val potenciaWatts: Double = 0.0,
    val horasDia: Double = 0.0,
    val consumoMensalKwh: Double = 0.0,
    val custoMensal: Double = 0.0
)
