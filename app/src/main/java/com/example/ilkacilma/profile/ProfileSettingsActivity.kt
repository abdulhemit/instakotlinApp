package com.example.ilkacilma.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.ActivityProfileSettingsBinding
import com.example.ilkacilma.utils.BottonNavigationViewHelper

class ProfileSettingsActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 4
    private val TAG = "ProfileAdtivity"
    private lateinit var binding: ActivityProfileSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        setupToolbar()
        fragmentNavigations()
    }

    private fun fragmentNavigations() {
        binding.idSttingProfiliDuzenle.setOnClickListener {
            binding.profileSttingsRoot.visibility = View.GONE
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSttingsContainer,ProfileEditFragment())
            transaction.addToBackStack("EditProfileFragmentEklendi")
            transaction.commit()
        }

        binding.idCikisYap.setOnClickListener {
            binding.profileSttingsRoot.visibility = View.GONE
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSttingsContainer,SingOutFragment())
            transaction.addToBackStack("SindOutFragmentEklendi")
            transaction.commit()
        }
    }

    private fun setupToolbar() {
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        binding.profileSttingsRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }

    fun setupNavigation(){
        BottonNavigationViewHelper.setupBottonNavigation(this,binding.bottonNavigarionViewProfileSttinge)
        val menu = binding.bottonNavigarionViewProfileSttinge.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}