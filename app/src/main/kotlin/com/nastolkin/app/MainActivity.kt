package com.nastolkin.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nastolkin.games.zakhvatkoloniy.CityRepositoryMemory
import com.nastolkin.games.zakhvatkoloniy.CityViewModel
import com.nastolkin.games.zakhvatkoloniy.RandomCityProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityViewModelFactory = viewModelFactory {
            initializer { CityViewModel(RandomCityProvider(CityRepositoryMemory())) }
        }
        setContent {
            val cityViewModel: CityViewModel = viewModel(factory = cityViewModelFactory)
            NastolkinApp(cityViewModel)
        }
    }
}
