package com.homeofhope.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.homeofhope.app.data.local.dao.MemberDao
import com.homeofhope.app.data.local.entity.Member

@Database(entities = [Member::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "home_of_hope_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
