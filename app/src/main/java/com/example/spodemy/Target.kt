package com.example.spodemy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.spodemy.Repository.UserRepository
import com.example.spodemy.Room.RegisterData
import com.example.spodemy.Room.UserRoomDatabase
import com.example.spodemy.ViewModel.UserViewModel
import com.example.spodemy.ViewModel.UserViewModelFactory
import com.example.spodemy.databinding.ActivityTargetBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SetTarget : AppCompatActivity() {

    private lateinit var mainViewModel: UserViewModel
    private lateinit var binding: ActivityTargetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_target)
        supportActionBar?.hide()

        val dao = UserRoomDatabase.getdata(this).getDao()
        val repo = UserRepository(dao)
        mainViewModel = ViewModelProvider(this, UserViewModelFactory(repo)).get(UserViewModel::class.java)

        var s = intent.getParcelableExtra<RegisterData>("data")
        Log.d("shuu",s.toString())

        binding.Setbutton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                mainViewModel.insertUserdata(s!!.Email,s.Password,s.firstName,s.surname,s.Date,s.ContactNumber,s.gender,s.age,binding.glassesTarget.text.toString().toInt())
            }
            Toast.makeText(this,"Target Updated",Toast.LENGTH_SHORT).show()
        }
    }
}