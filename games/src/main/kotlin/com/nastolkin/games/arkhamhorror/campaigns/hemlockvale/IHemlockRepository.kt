package com.nastolkin.games.arkhamhorror.campaigns.hemlockvale

data class HemlockLocation(val id: String, val page: Int)
data class HemlockResident(val id: String)

interface IHemlockRepository {
    fun getLocations(): List<HemlockLocation>
    fun getResidents(): List<HemlockResident>
    fun getDays(): List<Int>
}
