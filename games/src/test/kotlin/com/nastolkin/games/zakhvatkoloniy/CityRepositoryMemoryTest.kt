package com.nastolkin.games.zakhvatkoloniy

import kotlin.test.Test
import kotlin.test.assertEquals

class CityRepositoryMemoryTest {
    @Test
    fun `returns the exact historical names in the supplied order`() {
        val cityRepository: ICityRepository = CityRepositoryMemory()

        assertEquals(
            listOf("Момбаса", "Могадишо", "Коломбо", "Кокосовые острова", "Бомбей", "Рангун", "Броом", "Паданг", "Сейшелы", "Гоа", "Аден"),
            cityRepository.getCities().map { it.name }
        )
    }

    @Test
    fun `caller cannot corrupt later repository reads`() {
        val cityRepository = CityRepositoryMemory()
        val expectedCities = cityRepository.getCities().toList()
        val returnedCities = cityRepository.getCities()

        runCatching { (returnedCities as MutableList<City>).clear() }

        assertEquals(expectedCities, cityRepository.getCities())
    }
}
