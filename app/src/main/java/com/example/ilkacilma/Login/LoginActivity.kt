package com.example.ilkacilma.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.ilkacilma.Models.userDetails
import com.example.ilkacilma.Models.users
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
            var kullaniciBulundu = false
            mDB.collection("kullanicilar").get().addOnCompleteListener(object :
                OnCompleteListener<QuerySnapshot> {
                override fun onComplete(p0: Task<QuerySnapshot>) {
                    for (documens in p0.result){
                        val kaytedilecekKullaniciDetaylari = userDetails("0","0","0","","","")
                        val list = users(documens.get("email") as String,documens.get("pasword") as String,documens.get("user_Name") as String,null,null,kaytedilecekKullaniciDetaylari)
                        val email = list.email
                        val sifre = binding.sifre.text.toString()
                        if (list.email!!.equals(binding.editTelEmailKullaniciAdi.text.toString())){
                            oturumAc(email,sifre)
                            kullaniciBulundu = true
                            break
                        }else if (list.user_Name!!.equals(binding.editTelEmailKullaniciAdi.text.toString())){
                            oturumAc(email,sifre)
                            kullaniciBulundu = true
                            break
                        }
                    }
                    if (kullaniciBulundu == false){

                        Toast.makeText(this@LoginActivity,"Kullanıcı bulunamadı",Toast.LENGTH_LONG).show()
                    }

                }

            })

        }
    }

    private fun oturumAc(email: String?, sifre: String) {

        mAuth.signInWithEmailAndPassword(email!!,sifre).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(this,"oturum açıldı" +  mAuth.currentUser!!.uid,Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(this,"kullanıcı adı veya şifre hatalı ",Toast.LENGTH_LONG).show()
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