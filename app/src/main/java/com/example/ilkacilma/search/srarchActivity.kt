package com.example.ilkacilma.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.ActivitySrarchBinding
import com.example.ilkacilma.utils.BottonNavigationViewHelper

class srarchActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 0
    private val TAG = "HomeActivity"
    private lateinit var binding: ActivitySrarchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySrarchBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}