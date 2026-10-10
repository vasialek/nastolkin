package com.nastolkin.games.zakhvatkoloniy

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CityViewModelTest {
    @Test
    fun `starts without a selected or visible city`() {
        val cityViewModel = CityViewModel(RandomCityProviderMock())

        assertEquals(CityScreenState(), cityViewModel.state.value)
    }

    @Test
    fun `selection calls provider once and immediately reveals the city`() {
        val randomCityProvider = RandomCityProviderMock()
        val cityViewModel = CityViewModel(randomCityProvider)

        cityViewModel.selectRandomCity()

        assertEquals(CityScreenState(City("Момбаса"), true), cityViewModel.state.value)
        assertEquals(1, randomCityProvider.callCount)
    }

    @Test
    fun `visibility toggles without changing or selecting a city`() {
        val randomCityProvider = RandomCityProviderMock()
        val cityViewModel = CityViewModel(randomCityProvider)
        cityViewModel.selectRandomCity()

        cityViewModel.toggleCityVisibility()
        assertEquals(CityScreenState(City("Момбаса"), false), cityViewModel.state.value)

        cityViewModel.toggleCityVisibility()
        assertEquals(CityScreenState(City("Момбаса"), true), cityViewModel.state.value)
        assertEquals(1, randomCityProvider.callCount)
    }

    @Test
    fun `toggling before selection has no effect`() {
        val randomCityProvider = RandomCityProviderMock()
        val cityViewModel = CityViewModel(randomCityProvider)

        cityViewModel.toggleCityVisibility()

        assertEquals(CityScreenState(), cityViewModel.state.value)
        assertEquals(0, randomCityProvider.callCount)
    }

    @Test
    fun `new selection replaces the hidden city and reveals it`() {
        val randomCityProvider = RandomCityProviderMock()
        val cityViewModel = CityViewModel(randomCityProvider)
        cityViewModel.selectRandomCity()
        cityViewModel.toggleCityVisibility()
        randomCityProvider.city = City("Гоа")

        cityViewModel.selectRandomCity()

        assertEquals(CityScreenState(City("Гоа"), true), cityViewModel.state.value)
        assertEquals(2, randomCityProvider.callCount)
    }

    @Test
    fun `repeated city is revealed again after hiding`() {
        val cityViewModel = CityViewModel(RandomCityProviderMock())
        cityViewModel.selectRandomCity()
        cityViewModel.toggleCityVisibility()

        cityViewModel.selectRandomCity()

        assertEquals(CityScreenState(City("Момбаса"), true), cityViewModel.state.value)
    }

    @Test
    fun `provider failure preserves previous selection and visibility`() {
        val randomCityProvider = RandomCityProviderMock()
        val cityViewModel = CityViewModel(randomCityProvider)
        cityViewModel.selectRandomCity()
        cityViewModel.toggleCityVisibility()
        randomCityProvider.shouldFail = true

        assertFailsWith<NoSuchElementException> { cityViewModel.selectRandomCity() }

        assertEquals(CityScreenState(City("Момбаса"), false), cityViewModel.state.value)
    }

    private class RandomCityProviderMock : IRandomCityProvider {
        var city = City("Момбаса")
        var callCount = 0
        var shouldFail = false

        override fun getRandomCity(): City {
            callCount++
            if (shouldFail) {
                throw NoSuchElementException("No cities available")
            }
            return city
        }
    }
}
