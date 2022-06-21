package com.ev.composition_number.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ev.composition_number.R
import com.ev.composition_number.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}