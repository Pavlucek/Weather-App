package com.example.weather_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ThirdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third, container, false)
        val weatherTextView = view.findViewById<TextView>(R.id.ThirdFragmentTextView)

        // Replace this example forecast text with your actual weather forecast data
        val forecastText = "Today's weather forecast: Sunny with a high of 25°C"
        weatherTextView.text = forecastText

        return view
    }
}
