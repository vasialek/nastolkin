package com.nastolkin.games.zakhvatkoloniy

class CityRepositoryMemory : ICityRepository {
    override fun getCities(): List<City> = listOf(
        City("Момбаса"),
        City("Могадишо"),
        City("Коломбо"),
        City("Кокосовые острова"),
        City("Бомбей"),
        City("Рангун"),
        City("Броом"),
        City("Паданг"),
        City("Сейшелы"),
        City("Гоа"),
        City("Аден")
    )
}
