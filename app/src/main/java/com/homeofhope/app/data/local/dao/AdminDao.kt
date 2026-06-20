package com.homeofhope.app.data.local.dao

import androidx.room.*
import com.homeofhope.app.data.local.entity.Admin

@Dao
interface AdminDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: Admin)

    @Query("SELECT * FROM admins WHERE email = :email LIMIT 1")
    suspend fun getAdminByEmail(email: String): Admin?

    @Query("SELECT * FROM admins WHERE id = :id")
    suspend fun getAdminById(id: Int): Admin?
}
