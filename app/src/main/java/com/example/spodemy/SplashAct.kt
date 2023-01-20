package com.example.spodemy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)
        supportActionBar?.hide()

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        },2000)
    }
}