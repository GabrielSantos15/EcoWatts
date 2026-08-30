package br.com.fiap.EcoWatts.service

import br.com.fiap.EcoWatts.model.GeocodingResponse
import br.com.fiap.EcoWatts.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {

    // 1. Busca as coordenadas (Latitude/Longitude) pelo nome da cidade
    @GET("https://geocoding-api.open-meteo.com/v1/search")
    suspend fun searchCity(
        @Query("name") cityName: String,
        @Query("language") language: String = "pt",
        @Query("count") count: Int = 1
    ): GeocodingResponse

    // Busca a previsão do tempo usando as coordenadas
    @GET("https://api.open-meteo.com/v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m"
    ): WeatherResponse
}