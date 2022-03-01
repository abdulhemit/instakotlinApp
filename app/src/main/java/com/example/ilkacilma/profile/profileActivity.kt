package com.example.ilkacilma.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.ActivityProfileBinding
import com.example.ilkacilma.databinding.ActivitySrarchBinding
import com.example.ilkacilma.utils.BottonNavigationViewHelper
import com.example.ilkacilma.utils.UniversalImageLoader

class profileActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 4
    private val TAG = "ProfileAdtivity"
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        setupToolbar()
        setupProfilePicture()
    }

    private fun setupToolbar() {
        binding.idSttings.setOnClickListener {
            val intent = Intent(this,ProfileSettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        binding.prifiliDuzenle.setOnClickListener {
            binding.ProfileActivitRoot.visibility = View.GONE
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.ProfileActivityContainer,ProfileEditFragment())
            transaction.addToBackStack("EditProfileFragmentEklendi")
            transaction.commit()
        }

    }

    fun setupNavigation(){
        BottonNavigationViewHelper.setupBottonNavigation(this,binding.bottonNavigationviewProfileActivity)
        val menu = binding.bottonNavigationviewProfileActivity.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    override fun onBackPressed() {
        binding.ProfileActivitRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }

    private fun setupProfilePicture() {

        // internetten aldigimiz resmin adresi
        //https://stickker.net/wp-content/uploads/2021/10/Squid-Game-Soldier-Circle.png

        val imgURL ="stickker.net/wp-content/uploads/2021/10/Squid-Game-Soldier-Circle.png"
        UniversalImageLoader.setImage(imgURL,binding.profileImage,binding.ProfileActivityProgressBar,"https://")





    }
}