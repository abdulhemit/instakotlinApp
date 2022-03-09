package com.example.ilkacilma.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.ActivityNewsBinding
import com.example.ilkacilma.databinding.ActivityRegisterBinding
import com.example.ilkacilma.utils.EvenstBusDataEvents
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

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
                    binding.loginRoot.visibility = View.GONE
                    binding.loginContainer.visibility = View.VISIBLE
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.loginContainer,KayitislemleriFragment())
                    transaction.addToBackStack("KayitislemleriFragmentiEklendi")
                    transaction.commit()
                    EventBus.getDefault().postSticky(EvenstBusDataEvents.KayitBilgileriniGonder(null,binding.editTelefon.text.toString(),null,null,true))
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

}