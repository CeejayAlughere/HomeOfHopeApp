package com.homeofhope.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.homeofhope.app.data.local.dao.*
import com.homeofhope.app.data.local.entity.*

@Database(
    entities = [
        Member::class,
        DonationRecord::class,
        Event::class,
        EventJoin::class,
        Announcement::class,
        ImpactStory::class,
        CommunityMessage::class,
        Admin::class,
        VolunteerHours::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun donationDao(): DonationDao
    abstract fun eventDao(): EventDao
    abstract fun communityDao(): CommunityDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun impactStoryDao(): ImpactStoryDao
    abstract fun adminDao(): AdminDao
    abstract fun volunteerHoursDao(): VolunteerHoursDao

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
