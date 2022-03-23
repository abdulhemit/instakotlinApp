package com.example.ilkacilma.share

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.ActivityMainBinding
import com.example.ilkacilma.databinding.ActivityShareBinding
import com.example.ilkacilma.utils.SharePagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class shareActivity : AppCompatActivity() {
    private lateinit var  binding : ActivityShareBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupShareViewPager()
    }

    private fun setupShareViewPager() {


        val sharePagerAdapter = SharePagerAdapter(supportFragmentManager,lifecycle)
        sharePagerAdapter.addFragments(ShareGaleriFragment())
        sharePagerAdapter.addFragments(ShareCameraFragment())
        sharePagerAdapter.addFragments(ShareVideoFragment())

        binding.sharveiwpager.adapter = sharePagerAdapter

        TabLayoutMediator(binding.sharetabLayout,binding.sharveiwpager){ tab,position ->
            when (position){
                0->{
                    tab.text = "GALERI"
                }
                1->{
                    tab.text = "FOTOĞRAF"
                }
                2->{
                    tab.text = "VIDEO"
                }

            }

        }.attach()
    }
}