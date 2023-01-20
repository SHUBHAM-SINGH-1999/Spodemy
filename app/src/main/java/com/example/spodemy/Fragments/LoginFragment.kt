package com.example.spodemy.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.spodemy.Profile_Activity
import com.example.spodemy.R
import com.example.spodemy.Repository.UserRepository
import com.example.spodemy.Room.UserRoomDatabase
import com.example.spodemy.ViewModel.UserViewModel
import com.example.spodemy.ViewModel.UserViewModelFactory
import com.example.spodemy.databinding.FragmentLogin2Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLogin2Binding? =null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =FragmentLogin2Binding.inflate(inflater,container,false)


        val dao = UserRoomDatabase.getdata(context?.applicationContext!!).getDao()
        val repo = UserRepository(dao)
        mainViewModel = ViewModelProvider(this, UserViewModelFactory(repo)).get(UserViewModel::class.java)

        btnx()



        return binding.root
    }

    private fun validData() {
        val email = binding.email.text.toString()
        val pass = binding.pass.text.toString()
        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(activity,"Enter the credentials", Toast.LENGTH_SHORT).show()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(activity,"Enter vaild Email", Toast.LENGTH_SHORT).show()
        }else{
                checkForUser(email,pass)

        }
    }

     private fun checkForUser(email: String,pass:String) {
       GlobalScope.launch(Dispatchers.Main) {
            mainViewModel.getUserData().forEach {
                if(it.Email==email&& it.Password==pass){
                    val intent = Intent(activity, Profile_Activity::class.java)
                    val n = it.firstName + " " + it.surname
                    intent.putExtra("name",n)
                    intent.putExtra("dob",it.age)
                    intent.putExtra("email",it.Email)
                    intent.putExtra("number",it.NumberOfGlasses.toString())
                    startActivity(intent)
                    activity?.finish()
                    return@launch
                }
            }
           Toast.makeText(activity,"User not found", Toast.LENGTH_SHORT).show()
        }

    }


    private fun btnx() {
        binding.SubmitButton.setOnClickListener {
            validData()
        }
        binding.Register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.ForPass.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPassFragment)
        }
    }

}