package com.nastolkin.games.zakhvatkoloniy

import kotlin.random.Random

class RandomCityProvider : IRandomCityProvider {
    private val cityRepository: ICityRepository
    private val randomSource: Random

    constructor(cityRepository: ICityRepository, randomSource: Random = Random.Default) {
        this.cityRepository = cityRepository
        this.randomSource = randomSource
    }

    override fun getRandomCity(): City {
        val cities = cityRepository.getCities()
        if (cities.isEmpty()) {
            throw NoSuchElementException("The city repository contains no cities.")
        }
        return cities[randomSource.nextInt(cities.size)]
    }
}
