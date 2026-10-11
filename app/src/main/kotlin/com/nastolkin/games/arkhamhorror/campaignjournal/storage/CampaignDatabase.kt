package com.nastolkin.games.arkhamhorror.campaignjournal.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CampaignRecord::class], version = 1, exportSchema = true)
abstract class CampaignDatabase : RoomDatabase() {
    abstract fun campaignDao(): ICampaignDao

    companion object {
        @Volatile private var instance: CampaignDatabase? = null

        fun getInstance(context: Context): CampaignDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context.applicationContext, CampaignDatabase::class.java, "arkham-journals.db")
                .build().also { instance = it }
        }
    }
}
