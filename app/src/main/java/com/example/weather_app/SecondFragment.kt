package com.example.weather_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SecondFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var secondFragmentTextView: TextView

    @Deprecated("Deprecated in Java")Www
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)

        viewModel.weatherData.observe(viewLifecycleOwner, Observer { data ->
            updateUI(data) // Metoda do aktualizacji UI
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        secondFragmentTextView = view.findViewById(R.id.SecondFragmentTextView)
        return view
    }
    @SuppressLint("SetTextI18n")
    private fun updateUI(data: WeatherData) {
        // Użyj danych pogodowych do aktualizacji UI
        secondFragmentTextView.text = "Humidity: ${data.main.humidity}%\n" +
                "Wind Speed: ${data.wind.speed} m/s\n" +
                "Visibility: ${data.visibility} meters"
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pobieranie danych z bundla
        val powerAndSpeed = arguments?.getString("powerAndSpeed") ?: "N/A"
        val humidity = arguments?.getString("humidity") ?: "N/A"
        val visibility = arguments?.getString("visibility") ?: "N/A"

        // Wyświetlanie danych
        secondFragmentTextView.text = "Power and Speed of Wind: $powerAndSpeed\n" +
                "Humidity: $humidity\n" +
                "Visibility: $visibility"
    }
}

