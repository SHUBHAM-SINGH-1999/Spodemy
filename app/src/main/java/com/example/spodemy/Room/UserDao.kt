package com.example.spodemy.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(registerData: RegisterData)

    @Query("SELECT * FROM userData")
    suspend fun getUserData():List<RegisterData>


}


