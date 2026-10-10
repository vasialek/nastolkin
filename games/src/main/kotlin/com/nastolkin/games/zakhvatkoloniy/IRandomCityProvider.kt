package com.nastolkin.games.zakhvatkoloniy

interface IRandomCityProvider {
    /** @throws NoSuchElementException when the repository contains no cities. */
    fun getRandomCity(): City
}
