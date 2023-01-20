package com.example.spodemy.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RegisterData::class], version = 1)
abstract class UserRoomDatabase: RoomDatabase() {
    abstract fun getDao():UserDao

    companion object{
        var INSTANCE:UserRoomDatabase?=null

        fun getdata(context: Context):UserRoomDatabase{
            if(INSTANCE==null){
                INSTANCE = Room.databaseBuilder(context,UserRoomDatabase::class.java,"userdb").build()
            }
            return INSTANCE!!
        }
    }

}