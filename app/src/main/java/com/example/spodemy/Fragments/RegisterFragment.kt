package com.example.spodemy.Fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.example.spodemy.R
import com.example.spodemy.Repository.UserRepository
import com.example.spodemy.Room.RegisterData
import com.example.spodemy.Room.UserRoomDatabase
import com.example.spodemy.ViewModel.UserViewModel
import com.example.spodemy.ViewModel.UserViewModelFactory
import com.example.spodemy.databinding.FragmentRegisterBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? =null
    private val binding get() = _binding!!
    private  var gender:String? = null
    private lateinit var mainViewModel: UserViewModel
   lateinit var age:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         _binding = FragmentRegisterBinding.inflate(inflater,container,false)

        val mycalender = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener{view,year,month,dayofMonth ->
            mycalender.set(Calendar.YEAR,year)
            mycalender.set(Calendar.MONTH,month)
            mycalender.set(Calendar.DAY_OF_MONTH,dayofMonth)
            age = getAge(year,month,dayofMonth)
            Log.d("shu",age.toString())
            updateC(mycalender)
        }

        val dao = UserRoomDatabase.getdata(context?.applicationContext!!).getDao()
        val repo = UserRepository(dao)
        mainViewModel = ViewModelProvider(this@RegisterFragment,UserViewModelFactory(repo)).get(UserViewModel::class.java)


        binding.radioGroup.setOnCheckedChangeListener{group,checkedid->
            when(checkedid){
                R.id.Male -> gender = "Male"
                R.id.Female -> gender = "Female"
                R.id.Others -> gender = "Others"
            }
        }
        binding.datebtn.setOnClickListener {
            DatePickerDialog(requireActivity(),datePicker,mycalender.get(Calendar.YEAR),mycalender.get(Calendar.MONTH),mycalender.get(Calendar.DAY_OF_MONTH)).show()
        }
       binding.RegisterButton.setOnClickListener {
           checkValidData()
       }
        return binding.root
    }


    fun getAge(year: Int, month: Int, day: Int): String {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()

        dob.set(year, month, day)

        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        val ageInt = age

        return ageInt.toString()
    }



    private fun updateC(mycalender: Calendar) {
        val format = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(format, Locale.US)
        binding.Date.setText(sdf.format(mycalender.time))
    }

    private fun checkValidData() {
        val firstname = binding.Firstname.text.toString()
        val surname = binding.Surname.text.toString()
        val Date = binding.Date.text.toString()
        val ContactNumber = binding.ContactNumber.text.toString()
        val Email = binding.RegisterEmail.text.toString()
        val password = binding.RegisterPass.text.toString()




        if(firstname.isEmpty())  Toast.makeText(activity,"Enter the FirstName", Toast.LENGTH_SHORT).show()
        else if(surname.isEmpty())  Toast.makeText(activity,"Enter the Surname", Toast.LENGTH_SHORT).show()
        else if(gender==null)  Toast.makeText(activity,"Select the gender", Toast.LENGTH_SHORT).show()
        else if(Date.isEmpty())  Toast.makeText(activity,"Enter the Date", Toast.LENGTH_SHORT).show()
        else if(ContactNumber.isEmpty())  Toast.makeText(activity,"Enter the ContactNumber", Toast.LENGTH_SHORT).show()
        else if(Email.isEmpty())  Toast.makeText(activity,"Enter the Email", Toast.LENGTH_SHORT).show()
        else if(password.isEmpty())  Toast.makeText(activity,"Enter the Password", Toast.LENGTH_SHORT).show()
        else if(Email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(Email).matches()) Toast.makeText(activity,"Enter the valid Email", Toast.LENGTH_SHORT).show()
        else{

           GlobalScope.launch {
                mainViewModel.insertUserdata(Email, password, firstname, surname, Date, ContactNumber, gender!!,age,0)
            }

            Toast.makeText(activity,"Register Successfully",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}