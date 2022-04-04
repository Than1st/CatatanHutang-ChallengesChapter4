package com.than.challengeschapter4catatanhutang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.than.challengeschapter4catatanhutang.databinding.ActivityMainBinding
import com.than.challengeschapter4catatanhutang.model.Pengutang
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}