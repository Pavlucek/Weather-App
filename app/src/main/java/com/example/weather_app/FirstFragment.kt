package com.example.weather_app

import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import com.squareup.picasso.Picasso
import retrofit2.HttpException
import java.io.IOException


class FirstFragment : Fragment() {
    private lateinit var cityEditText: EditText
    private lateinit var getWeatherButton: Button
    private lateinit var weatherTextView: TextView
    private lateinit var refreshButton: Button
    private lateinit var favoriteButton: Button
    private lateinit var favoriteloadButton: Button
    private lateinit var temperatureSwitch: SwitchCompat

    private var lastSearchedCity: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        cityEditText = view.findViewById(R.id.cityEditText)
        getWeatherButton = view.findViewById(R.id.getWeatherButton)
        weatherTextView = view.findViewById(R.id.FirstFragmentTextView)
        refreshButton = view.findViewById(R.id.refreshButton)
        favoriteloadButton = view.findViewById(R.id.favoriteloadButton)
        temperatureSwitch = view.findViewById(R.id.TemperatureSwitchLayout)
        setupButtonListeners()

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteButton = view.findViewById(R.id.favoriteButton)
        favoriteButton.setOnClickListener {
            if (lastSearchedCity != null) {
                saveCityToFavorites(lastSearchedCity!!)
                showToast("$lastSearchedCity added to favorites")
            } else {
                showToast("No city searched to add to favorites")
            }
        }
    }

    private fun saveCityToFavorites(cityName: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("FAVORITE_CITY", cityName)
            apply()
        }
    }
    private fun loadFavoriteCity() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val favoriteCity = sharedPref?.getString("FAVORITE_CITY", null)
        if (favoriteCity != null) {
            loadWeatherData(favoriteCity)
        } else {
            showToast("No favorite city saved")
        }
    }

    private fun setupButtonListeners() {
        getWeatherButton.setOnClickListener {
            val city = cityEditText.text.toString()
            if (city.isNotEmpty()) {
                lastSearchedCity = city // Store the last searched city
                loadWeatherData(city)
            } else {
                showToast("Please enter the name of the city")
            }
        }

        refreshButton.setOnClickListener {
            if (lastSearchedCity != null) {
                loadWeatherData(lastSearchedCity!!)
            } else {
                showToast("No previous search data available")
            }
        }
        favoriteloadButton.setOnClickListener{
            loadFavoriteCity()
        }
    }

    private fun loadWeatherData(city: String) {
        val apiKey = "9bc09018cfb9f27b8a6b2bec61d02aba" // Replace with your API key
        val weatherApiService = WeatherApiService()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val weatherData = weatherApiService.getWeatherData(city, apiKey)
                withContext(Dispatchers.Main) {
                    updateUI(weatherData)
                }
            } catch (e: Exception) {
                handleWeatherError(e)
            }
        }
    }


    private fun handleWeatherError(e: Exception) {
        when (e) {
            is IOException -> showToast("No internet connection")
            is HttpException -> {
                if (e.code() == 404) {
                    showToast("City not found")
                } else {
                    showToast("An error occurred")
                }
            }
            else -> showToast("An error occurred")
        }
    }

    private fun showToast(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
    @SuppressLint("SetTextI18n")

    private fun updateUI(weatherData: WeatherData) {
        val weather = weatherData.weather.firstOrNull() // Get the first Weather object (if available)

        val currentTimeMillis = System.currentTimeMillis() // Get the current time in milliseconds
        val formmatedTime = getFormattedTimeFromMillis(currentTimeMillis)
        val description = weather?.description ?: "N/A" // Weather description
        val iconUrl = "https://openweathermap.org/img/wn/${weather?.icon}.png" // Weather icon URL
        val temperatureInKelvin = weatherData.main.temp
        val temperature = if (temperatureSwitch.isChecked) {
            // Konwersja na Celsiusze
            kelvinToCelsius(temperatureInKelvin)
        } else {
            // Konwersja na Fahrenheita
            kelvinToFahrenheit(temperatureInKelvin)
        }

        val weatherIconImageView = view?.findViewById<ImageView>(R.id.weatherIconImageView)

        weatherTextView.text =
            "City: ${weatherData.name}\n" +
                    "Temperature: ${String.format("%.2f", temperature)}${if (temperatureSwitch.isChecked) "°C" else "°F"}\n" +
                    "Pressure: ${weatherData.main.pressure} hPa\n" +
                    "Coordinates: [${weatherData.coord.lat}, ${weatherData.coord.lon}]\n" +
                    "Timestamp: ${formmatedTime}\n" + // Use the current time
                    "Description: $description"

        Picasso.get().load(iconUrl).into(weatherIconImageView)
    }
    private fun getFormattedTimeFromMillis(timestampMillis: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestampMillis))
    }
    private fun kelvinToCelsius(kelvin: Double): Double {
        return kelvin - 273.15
    }

    private fun kelvinToFahrenheit(kelvin: Double): Double {
        return kelvin * 9/5 - 459.67
    }

}
