package br.com.fiap.EcoWatts.repository // Ajuste o pacote

import br.com.fiap.EcoWatts.model.CurrentWeather
import android.util.Log
import br.com.fiap.EcoWatts.factory.RetrofitFactory
import br.com.fiap.EcoWatts.model.LocationResult

class WeatherRepository {

    private val api = RetrofitFactory.weatherService

    suspend fun getWeatherForCity(cityName: String): Pair<CurrentWeather, LocationResult>? {
        return try {
            val geoResponse = api.searchCity(cityName = cityName)
            val location = geoResponse.results?.firstOrNull()

            if (location != null) {
                val weatherResponse = api.getWeather(
                    latitude = location.latitude,
                    longitude = location.longitude
                )

                // Retorna o clima e a localização
                Pair(weatherResponse.current, location)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}