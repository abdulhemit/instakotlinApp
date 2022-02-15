package com.example.ilkacilma.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.ActivityNewsBinding
import com.example.ilkacilma.utils.BottonNavigationViewHelper

class newsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
    }
    fun setupNavigation(){
        //BottonNavigationViewHelper.setupBottonNavigation(this,binding.bottomNavigationHome)
    }
}