package com.homeofhope.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.homeofhope.app.data.local.dao.AnnouncementDao
import com.homeofhope.app.data.local.dao.CommunityDao
import com.homeofhope.app.data.local.dao.DonationDao
import com.homeofhope.app.data.local.dao.EventDao
import com.homeofhope.app.data.local.dao.ImpactStoryDao
import com.homeofhope.app.data.local.dao.MemberDao
import com.homeofhope.app.data.local.entity.Announcement
import com.homeofhope.app.data.local.entity.DonationRecord
import com.homeofhope.app.data.local.entity.Event
import com.homeofhope.app.data.local.entity.EventJoin
import com.homeofhope.app.data.local.entity.ImpactStory
import com.homeofhope.app.data.local.entity.Member

@Database(
    entities = [
        Member::class,
        DonationRecord::class,
        Event::class,
        EventJoin::class,
        Announcement::class,
        ImpactStory::class,
        com.homeofhope.app.data.local.entity.CommunityMessage::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun donationDao(): DonationDao
    abstract fun eventDao(): EventDao
    abstract fun communityDao(): CommunityDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun impactStoryDao(): ImpactStoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "home_of_hope_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
