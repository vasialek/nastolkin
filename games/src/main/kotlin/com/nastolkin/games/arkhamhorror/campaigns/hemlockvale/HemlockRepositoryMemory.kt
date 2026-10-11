package com.nastolkin.games.arkhamhorror.campaigns.hemlockvale

class HemlockRepositoryMemory : IHemlockRepository {
    override fun getLocations(): List<HemlockLocation> = listOf(
        HemlockLocation("north-point", 10), HemlockLocation("hemlock-harbor", 14),
        HemlockLocation("pearl-ridge", 19), HemlockLocation("akuan-shore", 23),
        HemlockLocation("eastwick", 27), HemlockLocation("western-woods", 34), HemlockLocation("southern-fields", 46)
    )
    override fun getResidents(): List<HemlockResident> = listOf("rachel", "leah", "simeon", "william", "river", "gideon", "judith", "theo").map(::HemlockResident)
    override fun getDays(): List<Int> = listOf(1, 2, 3)
}
