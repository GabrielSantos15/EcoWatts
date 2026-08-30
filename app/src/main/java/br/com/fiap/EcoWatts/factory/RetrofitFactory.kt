package br.com.fiap.EcoWatts.factory

import br.com.fiap.EcoWatts.service.OpenMeteoApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create()) // Avisa para usar o Gson
        .build()

    val weatherService: OpenMeteoApi = retrofit.create(OpenMeteoApi::class.java)
}