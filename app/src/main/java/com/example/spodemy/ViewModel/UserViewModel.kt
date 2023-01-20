package com.example.spodemy.ViewModel

import androidx.lifecycle.ViewModel
import com.example.spodemy.Repository.UserRepository
import com.example.spodemy.Room.RegisterData

class UserViewModel(val repository: UserRepository):ViewModel() {

    suspend fun insertUserdata(Email:String,Password:String,firstName:String,surname:String,Date:String,ContactNumber:String,gender:String,age:String,numberofGlasses:Int){
        repository.insertUser(Email, Password, firstName, surname, Date, ContactNumber, gender,age,numberofGlasses)
    }

    suspend fun getUserData():List<RegisterData>{
       return repository.getUserData()
    }

}