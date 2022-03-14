package com.example.ilkacilma.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.example.ilkacilma.Models.Users
import com.example.ilkacilma.Models.userDetails
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.ActivityRegisterBinding
import com.example.ilkacilma.home.MainActivity
import com.example.ilkacilma.utils.EvenstBusDataEvents
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    lateinit var mDB : FirebaseFirestore
    lateinit var mAuth : FirebaseAuth
    lateinit var mAutlistener : FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mDB = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        init()
        setupmAuthListener()
        binding.registerGirisYap.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

    }
    private fun init(){
        binding.tvEmail.setOnClickListener {
            binding.telefonView.visibility = View.GONE
            binding.EpostaView.visibility = View.VISIBLE
            binding.editTelefon.setText("")
            binding.editTelefon.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            binding.editTelefon.setHint("E-Posta")

            binding.registerButtonIleri.isEnabled = false
            binding.registerButtonIleri.setTextColor(getColor(R.color.sonukmavi))
            binding.registerButtonIleri.setBackgroundResource(R.drawable.register_button)

        }

        binding.tvTelefon.setOnClickListener {
            binding.EpostaView.visibility = View.GONE
            binding.telefonView.visibility = View.VISIBLE
            binding.editTelefon.setText("")
            binding.editTelefon.inputType = InputType.TYPE_CLASS_NUMBER
            binding.editTelefon.setHint("Telefon")


            binding.registerButtonIleri.isEnabled = false
            binding.registerButtonIleri.setTextColor(getColor(R.color.sonukmavi))
            binding.registerButtonIleri.setBackgroundResource(R.drawable.register_button)

        }

        binding.editTelefon.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if ( p1+p2+p3 >= 10){

                    binding.registerButtonIleri.isEnabled = true
                    binding.registerButtonIleri.setTextColor(getColor(R.color.beyaz))
                    binding.registerButtonIleri.setBackgroundResource(R.drawable.register_button_aktif)

                }else {

                    binding.registerButtonIleri.isEnabled = false
                    binding.registerButtonIleri.setTextColor(getColor(R.color.sonukmavi))
                    binding.registerButtonIleri.setBackgroundResource(R.drawable.register_button)

                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.registerButtonIleri.setOnClickListener {

            if(binding.editTelefon.hint.toString().equals("Telefon")){

                Toast.makeText(this,"Telegon kaytdi islemleri guncelleniyor !!",Toast.LENGTH_LONG).show()
                // telefon ile kayt olma islemleri yapilacaktir !
//
//                binding.loginRoot.visibility = View.GONE
//                binding.loginContainer.visibility = View.VISIBLE
//                val transaction = supportFragmentManager.beginTransaction()
//                transaction.replace(R.id.loginContainer,telegonKoduGirFragment())
//                transaction.addToBackStack("telefonKoduGirFragmentiEklendi")
//                transaction.commit()
//                EventBus.getDefault().postSticky(EvenstBusDataEvents.TelefonNOGonder(binding.editTelefon.text.toString()))
            }
            else{



                if (isValidEmail(binding.editTelefon.text.toString())){

                    var EmailKullanimdaMi = false

                    // firebase den verileri yani email verisini aldim
                     mDB.collection("kullanicilar").get().addOnCompleteListener(object :OnCompleteListener<QuerySnapshot>{
                        override fun onComplete(p0: Task<QuerySnapshot>) {
                            if (p0.isSuccessful){
                                for (data in p0.result){

                                    // Email kullanimdami yoksa yeni bir Email mi kontrol ettim
                                 val kaytedilecekKullaniciDetaylari = userDetails("0","0","0","","","")
                                var list=(Users(data.get("email") as String,null,null,null,null,kaytedilecekKullaniciDetaylari))
                                    if(list.email.equals(binding.editTelefon.text.toString())){
                                        Toast.makeText(this@RegisterActivity,"Bu Email Kullanımda",Toast.LENGTH_LONG).show()
                                        EmailKullanimdaMi = true
                                        break
                                    }

                                }
                                if (EmailKullanimdaMi == false){

                                    // yeni Email ise kayt olmak icin Fragmnet degistirdim
                                    binding.loginRoot.visibility = View.GONE
                                    binding.loginContainer.visibility = View.VISIBLE
                                    val transaction = supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.loginContainer,KayitislemleriFragment())
                                    transaction.addToBackStack("KayitislemleriFragmentiEklendi")
                                    transaction.commit()
                                    EventBus.getDefault().postSticky(EvenstBusDataEvents
                                        .KayitBilgileriniGonder(null,binding.editTelefon.text.toString(),null,null,true))

                                }
                            }
                        }

                    })





                }
                else {
                    //Snackbar.make(it,"Lütfen geçerli bir Email giriniz !!",Snackbar.LENGTH_LONG).show()
                    Toast.makeText(this,"Lütfen geçerli bir Email giriniz !!",Toast.LENGTH_LONG).show()
                }

            }

        }


    }


    fun isValidEmail(kontrolEdilecekEmail:String):Boolean{

        if (kontrolEdilecekEmail == null){
            return false
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(kontrolEdilecekEmail).matches()
    }


    override fun onBackPressed() {
        binding.loginRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }

    private fun setupmAuthListener() {
        mAutlistener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user !=  null){
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
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