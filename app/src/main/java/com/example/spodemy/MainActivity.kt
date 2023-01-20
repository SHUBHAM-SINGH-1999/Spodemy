package com.example.spodemy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.spodemy.Fragments.LoginFragment
import com.example.spodemy.Fragments.RegisterFragment
import com.example.spodemy.Repository.UserRepository
import com.example.spodemy.Room.UserRoomDatabase
import com.example.spodemy.ViewModel.UserViewModel
import com.example.spodemy.ViewModel.UserViewModelFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()


    }
}