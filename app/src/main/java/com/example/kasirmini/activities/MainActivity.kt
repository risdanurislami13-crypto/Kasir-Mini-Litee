package com.example.kasirmini.activities

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kasirmini.R
import com.example.kasirmini.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNav)

        // ✅ Fragment awal
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
        }

        setupBottomNavigation()

        // ✅ Back button (tidak deprecated)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (bottomNav.selectedItemId == R.id.nav_home) {
                    finish()
                } else {
                    bottomNav.selectedItemId = R.id.nav_home
                }
            }
        })
    }

    private fun setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener { item ->

            val fragment: Fragment? = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_penjualan -> PenjualanFragment()
                R.id.nav_pengeluaran -> PengeluaranFragment()


                else -> null
            }

            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, it)
                    .commit()
                true
            } ?: false
        }
    }
}
