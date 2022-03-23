package com.example.ilkacilma.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ilkacilma.Login.LoginActivity
import com.example.ilkacilma.Models.Users
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.ActivityProfileBinding
import com.example.ilkacilma.utils.BottonNavigationViewHelper
import com.example.ilkacilma.utils.EvenstBusDataEvents
import com.example.ilkacilma.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import org.greenrobot.eventbus.EventBus


class profileActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 4
    private val TAG = "ProfileAdtivity"
    private lateinit var binding: ActivityProfileBinding
    lateinit var mAuth : FirebaseAuth
    lateinit var mDB : FirebaseFirestore
    lateinit var mAutlistener : FirebaseAuth.AuthStateListener
    lateinit var mUser : FirebaseUser
    //lateinit var gelenler : List<Users>
    lateinit var gelenler : Users

    //   https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTIyvImE1OhLBcBGTzkNsoHUlVKzgFPdq--VQ&usqp=CAU
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupmAuthListener()
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!
        mDB = FirebaseFirestore.getInstance()
        setupNavigation()
        okunanKullaniciBilgileriniGetir()
        setupToolbar()


    }

    private fun okunanKullaniciBilgileriniGetir() {
        val m = mUser.uid

        binding.prifiliDuzenle.isEnabled = false



        mDB.collection("kullanicilar").document(mUser.uid).addSnapshotListener { value, error ->
            if (error != null ){

            }else{
                if(value != null){
                    gelenler = value.toObject(Users::class.java)!!
                    EventBus.getDefault().postSticky(EvenstBusDataEvents.kullaniciBilgileriniGonder(gelenler))

                     binding.prifiliDuzenle.isEnabled = true

                    binding.profileGercekAd.setText(gelenler.AdSoyad.toString())
                    binding.tvProfileAdToolbar.setText(gelenler.user_Name.toString())
                    binding.takipSayisi.setText(gelenler.user_details?.following.toString())
                    binding.takipciSayisi.setText(gelenler.user_details?.follower.toString())
                    binding.PostSayisi.setText(gelenler.user_details?.post.toString())

                    if(!gelenler.user_details?.biography.isNullOrEmpty()){
                        binding.byografi.setText(gelenler.user_details?.biography.toString())
                    }
                    if (!gelenler.user_details?.web_site.isNullOrEmpty()){
                        binding.webSite.setText(gelenler.user_details?.web_site.toString())
                    }

                    val defultImage = R.drawable.ic_person_add_24
                    val imgURL = gelenler.user_details?.profile_pucture ?: "$defultImage"
                    UniversalImageLoader.setImage(imgURL,binding.profileImage,binding.ProfileActivityProgressBar,"")

                }
            }
        }

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



    private fun setupmAuthListener() {

        mAutlistener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user ==  null){
                    val intent = Intent(this@profileActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else{

                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAutlistener)

    }

    override fun onStop() {
        super.onStop()
        if (mAutlistener != null){
            mAuth.removeAuthStateListener(mAutlistener)
        }
    }

}

