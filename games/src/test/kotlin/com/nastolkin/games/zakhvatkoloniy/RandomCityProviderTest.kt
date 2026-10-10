package com.nastolkin.games.zakhvatkoloniy

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RandomCityProviderTest {
    @Test
    fun `every repository entry can be selected using its index`() {
        val cities = CityRepositoryMemory().getCities()

        cities.forEachIndexed { index, city ->
            val randomSource = RandomMock(index)
            val randomCityProvider = RandomCityProvider(CityRepositoryMock(cities), randomSource)

            assertEquals(city, randomCityProvider.getRandomCity())
            assertEquals(cities.size, randomSource.requestedBound)
        }
    }

    @Test
    fun `single city can repeat across selections`() {
        val city = City("Аден")
        val randomCityProvider = RandomCityProvider(CityRepositoryMock(listOf(city)), Random(42))

        repeat(3) {
            assertEquals(city, randomCityProvider.getRandomCity())
        }
    }

    @Test
    fun `reads repository on each selection`() {
        val cityRepository = CityRepositoryMock(listOf(City("Аден")))
        val randomCityProvider = RandomCityProvider(cityRepository, Random(42))
        assertEquals(City("Аден"), randomCityProvider.getRandomCity())

        cityRepository.availableCities = listOf(City("Гоа"))

        assertEquals(City("Гоа"), randomCityProvider.getRandomCity())
    }

    @Test
    fun `empty repository fails explicitly before requesting randomness`() {
        val randomSource = RandomMock(0)
        val randomCityProvider = RandomCityProvider(CityRepositoryMock(emptyList()), randomSource)

        assertFailsWith<NoSuchElementException> { randomCityProvider.getRandomCity() }
        assertEquals(null, randomSource.requestedBound)
    }

    @Test
    fun `default random source returns a repository city`() {
        val cityRepository = CityRepositoryMemory()
        val randomCityProvider = RandomCityProvider(cityRepository)

        assertTrue(randomCityProvider.getRandomCity() in cityRepository.getCities())
    }

    private class CityRepositoryMock(var availableCities: List<City>) : ICityRepository {
        override fun getCities(): List<City> = availableCities
    }

    private class RandomMock(private val selectedIndex: Int) : Random() {
        var requestedBound: Int? = null
            private set

        override fun nextInt(until: Int): Int {
            requestedBound = until
            return selectedIndex
        }

        override fun nextBits(bitCount: Int): Int = error("Unexpected random call")
    }
}
