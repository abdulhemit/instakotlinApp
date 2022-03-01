package com.example.ilkacilma.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.ActivityMainBinding
import com.example.ilkacilma.utils.BottonNavigationViewHelper
import com.example.ilkacilma.utils.HomePagerAdapter
import com.example.ilkacilma.utils.UniversalImageLoader
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nostra13.universalimageloader.core.ImageLoader

class MainActivity : AppCompatActivity() {
    // github token
    // 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
    private val ACTIVITY_NO = 0
    private val TAG = "HomeActivity"
    private lateinit var  binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        setupHomeViewPager()
        initimageLoader()
    }

    private fun setupHomeViewPager() {
        var homePagerAdapter = HomePagerAdapter(supportFragmentManager,lifecycle)
        homePagerAdapter.addFragments(CameraFragment())
        homePagerAdapter.addFragments(HomeFragment())
        homePagerAdapter.addFragments(MessagesFragment())

        // Main Activityde bulunan viewpagera olusturdugumuz adaptoru atadik
        binding.homeViewPager.adapter = homePagerAdapter

        // viewpagerin homeFragmentten baslamasina sagladik
        binding.homeViewPager.setCurrentItem(1)

    }

    fun setupNavigation(){
        BottonNavigationViewHelper.setupBottonNavigation(this,binding.bottomNavigationHome)
        val menu = binding.bottomNavigationHome.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    private fun initimageLoader(){
        val universalImageLoader = UniversalImageLoader(this)
        ImageLoader.getInstance().init(universalImageLoader.config)
    }

}