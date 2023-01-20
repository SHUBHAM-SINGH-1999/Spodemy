package com.example.spodemy.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spodemy.Repository.UserRepository

class UserViewModelFactory(val repository: UserRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(repository) as T
    }
}