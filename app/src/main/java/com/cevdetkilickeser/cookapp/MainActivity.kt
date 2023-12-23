package com.cevdetkilickeser.cookapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.cevdetkilickeser.cookapp.db.MealDatabase
import com.cevdetkilickeser.cookapp.viewmodel.HomeViewModel
import com.cevdetkilickeser.cookapp.viewmodel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    val viewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var navController = Navigation.findNavController(this,R.id.navHostFragment)
        var bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        NavigationUI.setupWithNavController(bottomNav,navController)
    }
}