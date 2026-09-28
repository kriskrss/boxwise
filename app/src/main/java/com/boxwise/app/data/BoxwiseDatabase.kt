package com.boxwise.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Box::class, Item::class], version = 1, exportSchema = false)
abstract class BoxwiseDatabase : RoomDatabase() {
    abstract fun boxDao(): BoxDao
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var instance: BoxwiseDatabase? = null

        fun get(context: Context): BoxwiseDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    BoxwiseDatabase::class.java,
                    "boxwise.db"
                ).build().also { instance = it }
            }
    }
}
