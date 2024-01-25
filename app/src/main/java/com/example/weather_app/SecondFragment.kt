package com.example.weather_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SecondFragment : Fragment() {

    private lateinit var secondFragmentTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        secondFragmentTextView = view.findViewById(R.id.SecondFragmentTextView)
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the weather information from arguments
        val powerAndSpeed = arguments?.getString("powerAndSpeed") ?: "N/A"
        val humidity = arguments?.getString("humidity") ?: "N/A"
        val visibility = arguments?.getString("visibility") ?: "N/A"

        // Display the information in the TextView
        secondFragmentTextView.text = "Power and Speed of Wind: $powerAndSpeed\n" +
                "Humidity: $humidity\n" +
                "Visibility: $visibility"
    }
}

