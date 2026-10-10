package com.nastolkin.games.zakhvatkoloniy

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CityViewModel : ViewModel {
    private val randomCityProvider: IRandomCityProvider
    private val mutableState = MutableStateFlow(CityScreenState())

    val state: StateFlow<CityScreenState> = mutableState.asStateFlow()

    constructor(randomCityProvider: IRandomCityProvider) {
        this.randomCityProvider = randomCityProvider
    }

    fun selectRandomCity() {
        mutableState.value = CityScreenState(randomCityProvider.getRandomCity(), true)
    }

    fun toggleCityVisibility() {
        mutableState.update { currentState ->
            if (currentState.city == null) {
                currentState
            } else {
                currentState.copy(isCityVisible = currentState.isCityVisible.not())
            }
        }
    }
}
