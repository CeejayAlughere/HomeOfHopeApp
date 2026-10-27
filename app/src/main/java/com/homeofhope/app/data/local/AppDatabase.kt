
package com.homeofhope.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 7,
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

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE members ADD COLUMN firebaseUid TEXT"
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "home_of_hope_database"
                )
                    .addMigrations(MIGRATION_6_7)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
