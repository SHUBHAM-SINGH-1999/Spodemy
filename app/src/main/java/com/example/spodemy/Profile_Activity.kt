package com.example.spodemy

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.spodemy.Repository.UserRepository
import com.example.spodemy.Room.RegisterData
import com.example.spodemy.Room.UserRoomDatabase
import com.example.spodemy.ViewModel.UserViewModel
import com.example.spodemy.ViewModel.UserViewModelFactory
import com.example.spodemy.databinding.ActivityProfileBinding
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.annotation.Target

class Profile_Activity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val PERMISSION_ID = 1010

    private lateinit var name:String
    private lateinit var age:String
    private lateinit var email:String
    private lateinit var data: RegisterData
    private lateinit var binding:ActivityProfileBinding
    private lateinit var number:String
    private lateinit var mainViewModel: UserViewModel


    override fun onRestart() {
        super.onRestart()
        GlobalScope.launch(Dispatchers.Main) {
            mainViewModel.getUserData().forEach {
                if(it.Email==email){
                    data = RegisterData(it.Email,it.Password,it.firstName,it.surname,it.Date,it.ContactNumber,it.gender,it.age,it.NumberOfGlasses)
                    WaterTracker()
                    return@launch
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile)
        supportActionBar?.hide()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val dao = UserRoomDatabase.getdata(this).getDao()
        val repo = UserRepository(dao)
        mainViewModel = ViewModelProvider(this, UserViewModelFactory(repo)).get(UserViewModel::class.java)

        data = RegisterData("","","","","","","","",0)


         name = intent.getStringExtra("name").toString()
         age = intent.getStringExtra("dob").toString()
        email = intent.getStringExtra("email").toString()
        number = intent.getStringExtra("number").toString()


        GlobalScope.launch(Dispatchers.Main) {
            mainViewModel.getUserData().forEach {
                if(it.Email==email){
                    data = RegisterData(it.Email,it.Password,it.firstName,it.surname,it.Date,it.ContactNumber,it.gender,it.age,it.NumberOfGlasses)
                    WaterTracker()
                    return@launch
                }
            }
        }

        binding.name.text = name
        binding.age.text = age

        Log.d("shu1",data.toString())

        addTextChangeListener()
        CheckPermission()


        binding.target.setOnClickListener {
            var s = Intent(this, SetTarget::class.java)
            Log.d("shu2",data.toString())
            s.putExtra("data",data)
            startActivity(s)
        }
    }

    private fun WaterTracker() {
        Log.d("shu3",data.toString())
        var target = data.NumberOfGlasses
        binding.SetWaterTarget.text = target.toString()
        var counter=0
        binding.progressBar.max = target
        binding.plus.setOnClickListener {
            if(counter>=target) counter=target
            else counter++;
            binding.progressBar.setProgress(counter)
        }
        binding.minus.setOnClickListener {
            if(counter<=0) counter=0
            else{
                counter--
            }
            binding.progressBar.setProgress(counter)
        }

    }

    private fun calculateBMI(height: Int, weight: Int): Float {
        val Height_in_metre = height.toFloat() / 100
        val BMI = weight.toFloat() / (Height_in_metre * Height_in_metre)
        return BMI
    }

    private fun addTextChangeListener() {
            binding.height.addTextChangedListener(EditTextWatcher(binding.height))
            binding.weight.addTextChangedListener(EditTextWatcher(binding.weight))

    }

    inner class EditTextWatcher(val view: View): TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            var text = p0.toString()
            if(p0!!.toString().trim().isEmpty() || binding.weight.text.trim().isEmpty() || binding.height.text.trim().toString().isEmpty()){
                binding.bmi.text = "0"
                return
            }else {
               var text1 = p0.toString().toInt()
                when (view.id) {
                    R.id.height -> {
                        var weight = binding.weight.text.toString().toInt()
                        val bmi = calculateBMI(text1, weight)
                        binding.bmi.text = bmi.toString()
                        bmiStatus(bmi)
                    }

                    R.id.weight -> {
                        var height = binding.height.text.toString().toInt()
                        val bmi = calculateBMI(height, text1)
                        binding.bmi.text = bmi.toString()
                        bmiStatus(bmi)
                    }
                }
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    private fun bmiStatus(BMI:Float) {
        if (BMI < 18.5) {
            binding.status.text = "Under Weight"
        } else if (BMI >= 18.5 && BMI < 24.9) {
            binding.status.text = "Healthy"
        } else if (BMI >= 24.9 && BMI < 30) {
            binding.status.text = "Overweight"
        } else if (BMI >= 30) {
            binding.status.text = "Suffering from Obesity"
        }
    }

    // for location

    fun CheckPermission(){
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    var location: Location? = task.result
                    if(location == null){
                        var locationRequest =  LocationRequest()
                        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        locationRequest.interval = 0
                        locationRequest.fastestInterval = 0
                        locationRequest.numUpdates = 1
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationProviderClient!!.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
                    }else{
                        binding.address.text = getCityName(location.latitude,location.longitude)
                    }
                }
            }else{
                buildAlertMessageNoGps()
            }
        }
        else{
            RequestPermission()
            Handler().postDelayed({
                CheckPermission()
            },1000)

        }
    }


    fun RequestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
    }

    fun isLocationEnabled():Boolean{
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
            binding.address.text = getCityName(lastLocation.latitude,lastLocation.longitude)
        }
    }

    private fun callback(){

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                var location: Location? = task.result
                if(location == null){
                    var locationRequest =  LocationRequest()
                    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    locationRequest.interval = 0
                    locationRequest.fastestInterval = 0
                    locationRequest.numUpdates = 1
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                    fusedLocationProviderClient!!.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
                }else{
                    binding.address.text = getCityName(location.latitude,location.longitude)
                }
            }
        }

    }

    private fun getCityName(lat: Double,long: Double):String{
        var cityName:String = ""
        var countryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,3)

        cityName = Adress.get(0).locality
        countryName = Adress.get(0).countryName
        var dis = Adress.get(0).adminArea
        val address:String = cityName + ", " + dis +", "+ countryName
        Log.d("Debug:", "Your City: $cityName ; your Country $countryName")
        return address
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                   callback()
                })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

}