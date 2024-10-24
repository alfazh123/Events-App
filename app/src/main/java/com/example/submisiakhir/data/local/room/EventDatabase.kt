package com.example.submisiakhir.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.submisiakhir.data.local.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 2, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): EventDatabase {

            val MIGRATION_1_2 = object : Migration(1, 2) {
                override fun migrate(db: SupportSQLiteDatabase) {
                }
            }

            if (INSTANCE == null) {
                synchronized(EventDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        EventDatabase::class.java, "favorite_databse"
                    ).addMigrations(MIGRATION_1_2)
                        .build()
                }
            }

            return INSTANCE as EventDatabase

        }
    }
}