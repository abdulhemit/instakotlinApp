package com.example.ilkacilma.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.ilkacilma.Models.Users
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.ActivityLoginBinding
import com.example.ilkacilma.home.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    lateinit var mDB : FirebaseFirestore
    lateinit var mAuth : FirebaseAuth
    lateinit var mAutlistener : FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        mDB = FirebaseFirestore.getInstance()

        binding.LoginKaydol.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
        init()
        setupmAuthListener()

    }



    fun init(){
        binding.editTelEmailKullaniciAdi.addTextChangedListener(wtcher)
        binding.sifre.addTextChangedListener(wtcher)

        binding.btnGirisYap.setOnClickListener {
            oturumAcicakKUllaniciyiDenetle(binding.editTelEmailKullaniciAdi.text.toString(),binding.sifre.text.toString())
        }



    }

    private fun oturumAcicakKUllaniciyiDenetle(editTelEmailKullaniciAdi: String, sifre: String) {

        mDB.collection("kullanicilar").orderBy("email").get().addOnCompleteListener(object :OnCompleteListener<QuerySnapshot>{
            override fun onComplete(p0: Task<QuerySnapshot>) {
                for (ds in p0.result){
                    var okunanKullanici = ds.toObject(Users::class.java)
                    if (okunanKullanici.email.toString().equals(editTelEmailKullaniciAdi)){
                        acOturum(okunanKullanici,sifre)
                        break
                    }else if (okunanKullanici.user_Name.toString().equals(editTelEmailKullaniciAdi)){
                        acOturum(okunanKullanici,sifre)
                        break
                    }
                }
            }


        })

    }

    private fun acOturum(okunanKullanici: Users, sifre: String) {

        val email = okunanKullanici.email

        mAuth.signInWithEmailAndPassword(email!!,sifre).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(this,"oturum a????ld??" +  mAuth.currentUser!!.uid,Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(this,"kullan??c?? ad?? veya ??ifre hatal?? ",Toast.LENGTH_LONG).show()
            }
        }
    }





    private val wtcher : TextWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (binding.editTelEmailKullaniciAdi.text.toString().length>= 6 && binding.sifre.text.toString().length>= 6){

                binding.btnGirisYap.isEnabled = true
                binding.btnGirisYap.setTextColor(ContextCompat.getColor(this@LoginActivity,R.color.beyaz))
                binding.btnGirisYap.setBackgroundResource(R.drawable.register_button_aktif)

            }
            else{
                binding.btnGirisYap.isEnabled = false
                binding.btnGirisYap.setTextColor(ContextCompat.getColor(this@LoginActivity,R.color.sonukmavi))
                binding.btnGirisYap.setBackgroundResource(R.drawable.register_button)

            }
        }

        override fun afterTextChanged(p0: Editable?) {

        }


    }

    private fun setupmAuthListener() {
        mAutlistener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user !=  null){
                    val intent = Intent(this@LoginActivity,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
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