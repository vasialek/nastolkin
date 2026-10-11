package com.nastolkin.home

class GameRepositoryMemory : IGameRepository {
    override fun getGames(): List<Game> = listOf(Game.ZAKHVAT_KOLONIY, Game.ARKHAM_HORROR)
}
