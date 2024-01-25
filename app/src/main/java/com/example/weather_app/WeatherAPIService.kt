package com.example.weather_app

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class ForecastData(
    val city: CityInfo,
    val list: List<WeatherForecast>
)

data class CityInfo(
    val name: String,
    val coord: Coordinates,
    val country: String
)

data class WeatherForecast(
    val dt: Long, // Unix time
    val main: MainData,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val datetimeweather: String // Date and time in text format
)

data class WeatherData(
    val name: String, // Name of the city
    val coord: Coordinates,
    val weather: List<Weather>,
    val main: MainData,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long, // Unix time in seconds
    val visibility: Int
    // Add other fields as required by your use case
)
data class Clouds(val all: Int)
data class Coordinates(val lon: Double, val lat: Double)
data class Weather(val icon: String, val description: String) // reprezentacja graficzna warunków
data class MainData(val temp: Double, val pressure: Double, val humidity: Int)
data class Wind(val speed: Double, val deg: Double)


interface OpenWeatherMapApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): WeatherData

    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): ForecastData
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

    suspend fun getWeatherForecast(city: String, apiKey: String): ForecastData {
        return api.getWeatherForecast(city, apiKey)
    }
}

