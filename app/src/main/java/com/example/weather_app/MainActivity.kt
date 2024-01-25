package com.example.weather_app

import ViewPagerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val adapter = ViewPagerAdapter(this)

        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Ustaw etykiety karty TabLayout
            tab.text = when (position) {
                0 -> "Current Weather"
                1 -> "Weather Details"
                2 -> "Forecast"
                else -> ""
            }
        }.attach()
    }
}
