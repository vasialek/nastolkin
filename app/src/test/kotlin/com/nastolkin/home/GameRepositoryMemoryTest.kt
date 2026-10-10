package com.nastolkin.home

import org.junit.Assert.assertEquals
import org.junit.Test

class GameRepositoryMemoryTest {
    @Test
    fun returnsAvailableGames() {
        val gameRepository: IGameRepository = GameRepositoryMemory()

        assertEquals(listOf(Game.ZAKHVAT_KOLONIY), gameRepository.getGames())
    }
}
