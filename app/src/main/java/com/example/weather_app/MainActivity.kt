package com.example.weather_app

import ViewPagerAdapter
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isTablet(this) && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_tablet)
            setupTabletLayout()
        } else {
            setContentView(R.layout.activity_main)
            setupViewPagerLayout()
        }
    }

    private fun setupTabletLayout() {
        // Load and display fragments side by side
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_first_fragment, FirstFragment())
            replace(R.id.container_second_fragment, SecondFragment())
            replace(R.id.container_third_fragment, ThirdFragment())
            commit()
        }
    }

    private fun setupViewPagerLayout() {
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val adapter = ViewPagerAdapter(this)

        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 1

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Current Weather"
                1 -> "Weather Details"
                2 -> "Forecast"
                else -> ""
            }
        }.attach()
    }

    private fun isTablet(context: Context): Boolean {
        return (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }
}

