package com.example.spodemy.Repository

import com.example.spodemy.Room.RegisterData
import com.example.spodemy.Room.UserDao

class UserRepository(val userDao: UserDao) {

    suspend fun insertUser(Email:String,Password:String,firstName:String,surname:String,Date:String,ContactNumber:String,gender:String,age:String,numberofGlasses:Int){
        userDao.insertUserData(RegisterData(Email, Password, firstName, surname, Date, ContactNumber, gender,age,numberofGlasses))
    }

    suspend fun getUserData():List<RegisterData>{
        return userDao.getUserData()
    }


}