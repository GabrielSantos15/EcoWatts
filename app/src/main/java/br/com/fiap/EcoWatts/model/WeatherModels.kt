package br.com.fiap.EcoWatts.model
import com.google.gson.annotations.SerializedName

// Resposta principal da API de Geocodificação
data class GeocodingResponse(
    val results: List<LocationResult>?
)

// Os dados da cidade encontrada
data class LocationResult(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    @SerializedName("admin1")
    val state: String?
)

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val current: CurrentWeather
)

// Os dados climáticos do momento
data class CurrentWeather(
    @SerializedName("temperature_2m")
    val temperature: Double,

    @SerializedName("relative_humidity_2m")
    val humidity: Int,

    @SerializedName("weather_code")
    val weatherCode: Int,

    @SerializedName("wind_speed_10m")
    val windSpeed: Double
)