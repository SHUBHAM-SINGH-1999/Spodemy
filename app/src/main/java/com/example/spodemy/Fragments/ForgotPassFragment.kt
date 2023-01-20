package com.example.spodemy.Fragments

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.spodemy.R
import com.example.spodemy.Repository.UserRepository
import com.example.spodemy.Room.UserRoomDatabase
import com.example.spodemy.ViewModel.UserViewModel
import com.example.spodemy.ViewModel.UserViewModelFactory
import com.example.spodemy.databinding.FragmentForgotPassBinding
import com.example.spodemy.databinding.FragmentLogin2Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ForgotPassFragment : Fragment() {

    private var _binding: FragmentForgotPassBinding? =null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentForgotPassBinding.inflate(inflater,container,false)

        val dao = UserRoomDatabase.getdata(context?.applicationContext!!).getDao()
        val repo = UserRepository(dao)
        mainViewModel = ViewModelProvider(this, UserViewModelFactory(repo)).get(UserViewModel::class.java)

      binding.Forgotbutton.setOnClickListener {
          validData()
      }
        return binding.root
    }

    private fun validData() {
        val email = binding.ForgotEmail.text.toString()
        val pass = binding.Forgotpass.text.toString()
        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(activity,"Enter the Credentials", Toast.LENGTH_SHORT).show()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(activity,"Enter vaild Email", Toast.LENGTH_SHORT).show()
        }else{
            GlobalScope.launch(Dispatchers.Main) {
                mainViewModel.getUserData().forEach {
                    if(email==it.Email){
                        Toast.makeText(context?.applicationContext!!,"Password changed", Toast.LENGTH_SHORT).show()
                        mainViewModel.insertUserdata(email,pass,it.firstName,it.surname,it.Date,it.ContactNumber,it.gender,it.age,0)
                      findNavController().navigate(R.id.action_forgotPassFragment_to_loginFragment)
                        return@launch
                    }
                }
                Toast.makeText(activity,"User Not Exist", Toast.LENGTH_SHORT).show()
            }
        }
    }

}