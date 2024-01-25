package com.example.weather_app

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Updated model danych pogodowychdata class Weather(val description: String, val icon: String)
data class WeatherData(
    val name: String, // Name of the city
    val coord: Coordinates,
    val weather: List<Weather>,
    val main: Main,
    val dt: Long, // czas w formacie Unix
    val timestamp: Long // time of data retrieval in Unix timestamp format
)


data class Coordinates(val lon: Double, val lat: Double)
data class Weather(val description: String, val icon: String)
data class Main(val temp: Double, val pressure: Double)

interface OpenWeatherMapApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): WeatherData
}

class WeatherApiService {
    private val baseUrl = "https://api.openweathermap.org/data/2.5/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(OpenWeatherMapApi::class.java)

    suspend fun getWeatherData(city: String, apiKey: String): WeatherData {
        return api.getCurrentWeather(city, apiKey)
    }
}
