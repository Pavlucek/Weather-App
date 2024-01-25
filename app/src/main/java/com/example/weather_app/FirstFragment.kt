package com.example.weather_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.widget.ImageView
import com.squareup.picasso.Picasso



class FirstFragment : Fragment() {
    // Existing variables
    private lateinit var cityEditText: EditText
    private lateinit var getWeatherButton: Button
    private lateinit var weatherTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        cityEditText = view.findViewById(R.id.cityEditText)
        getWeatherButton = view.findViewById(R.id.getWeatherButton)
        weatherTextView = view.findViewById(R.id.FirstFragmentTextView)

        getWeatherButton.setOnClickListener {
            val city = cityEditText.text.toString()
            if (city.isNotEmpty()) {
                loadWeatherData(city)
            } else {
                // Notify user to enter a city name
            }
        }

        return view
    }
    private fun loadWeatherData(city: String) {
        val weatherApiService = WeatherApiService()
        val apiKey = "9bc09018cfb9f27b8a6b2bec61d02aba" // Replace with your API key

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val weatherData = weatherApiService.getWeatherData(city, apiKey)
                withContext(Dispatchers.Main) {
                    updateUI(weatherData)
                }
            } catch (e: Exception) {
                // Handle exception (e.g., city not found, network issues)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateUI(weatherData: WeatherData) {
        val weather = weatherData.weather.firstOrNull() // Get the first Weather object (if available)

        val timestamp = weatherData.timestamp // Time of data retrieval
        val description = weather?.description ?: "N/A" // Weather description
        val iconUrl = "https://openweathermap.org/img/wn/${weather?.icon}.png" // Weather icon URL

        val weatherIconImageView = view?.findViewById<ImageView>(R.id.weatherIconImageView)

        weatherTextView.text =
            "City: ${weatherData.name}\n" +
                    "Temperature: ${weatherData.main.temp}°C\n" +
                    "Pressure: ${weatherData.main.pressure} hPa\n" +
                    "Coordinates: [${weatherData.coord.lat}, ${weatherData.coord.lon}]\n" +
                    "Timestamp: ${getFormattedTime(timestamp)}\n" +
                    "Description: $description"

        // Load and display the weather icon using Picasso or Glide
        Picasso.get().load(iconUrl).into(weatherIconImageView)
        // or
        // Glide.with(this).load(iconUrl).into(weatherIconImageView)
    }


    private fun getFormattedTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000)) // Convert Unix timestamp to milliseconds
    }


}
