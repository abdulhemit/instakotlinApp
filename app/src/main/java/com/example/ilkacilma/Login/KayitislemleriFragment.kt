package com.example.ilkacilma.Login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.ilkacilma.Models.users
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.FragmentKayitislemleriBinding
import com.example.ilkacilma.utils.EvenstBusDataEvents
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.nio.file.FileSystemAlreadyExistsException


class KayitislemleriFragment : Fragment() {
    private var _binding : FragmentKayitislemleriBinding? = null
    private val binding get() = _binding
    var gelenEmail = ""
     lateinit var mDB :FirebaseFirestore
     lateinit var mAuth : FirebaseAuth
     var EmailIleKaytIslemi = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mDB = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKayitislemleriBinding.inflate(layoutInflater,container,false)
        binding?.adinEndsoyadin?.addTextChangedListener(watcher)
        binding?.sifre?.addTextChangedListener(watcher)
        binding?.nickName?.addTextChangedListener(watcher)



        binding?.btnGiris?.setOnClickListener {
            // kullanici Email ile kayt olmak istiyor
            if (EmailIleKaytIslemi){

                mAuth.createUserWithEmailAndPassword(gelenEmail,binding?.sifre?.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            //Toast.makeText(requireContext(),"oturum acildi",Toast.LENGTH_LONG).show()
                            val sifre = binding?.sifre?.text.toString()
                            val user_name = binding?.nickName?.text.toString()
                            val ad_soyad = binding?.adinEndsoyadin?.text.toString()
                            val userId = mAuth.currentUser?.uid.toString()
                            val kaydedilecekKullanici = users(gelenEmail,sifre,user_name,ad_soyad,userId)

                            mDB.collection("kullanicilar")
                                .add(kaydedilecekKullanici)
                                .addOnSuccessListener { decumentRefersns->
                                if (decumentRefersns != null){
                                    Toast.makeText(requireContext(),"kullanici kaydedildi",Toast.LENGTH_LONG).show()
                                }
                                else {
                                    mAuth.currentUser!!.delete()
                                }
                            }
                        }
                    }
            }
            // Kullanici telfon No ile kayt olmak istiyor
            else{

            }
        }



        return binding?.root
    }






    val watcher : TextWatcher = object :TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            if (p0!!.length > 5){

                if (binding?.adinEndsoyadin?.text.toString().length >5 &&binding?.sifre?.text.toString().length >5 &&binding?.nickName?.text.toString().length >5 ){
                    binding?.btnGiris?.isEnabled = true
                    binding?.btnGiris?.setTextColor(ContextCompat.getColor(activity!!, R.color.beyaz))
                    binding?.btnGiris?.setBackgroundResource(R.drawable.register_button_aktif)

                }else{


                    binding?.btnGiris?.isEnabled = false
                    binding?.btnGiris?.setTextColor(ContextCompat.getColor(activity!!, R.color.sonukmavi))
                    binding?.btnGiris?.setBackgroundResource(R.drawable.register_button)

                }

            }
            else{
                binding?.btnGiris?.isEnabled = false
                binding?.btnGiris?.setTextColor(ContextCompat.getColor(activity!!, R.color.sonukmavi))
                binding?.btnGiris?.setBackgroundResource(R.drawable.register_button)


            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }

    }

    ///////////////////// EventBus//////////////////////////////////////
    @Subscribe(sticky = true)
    internal fun onKayitEvent(kayitbilgileri: EvenstBusDataEvents.KayitBilgileriniGonder){

        if (kayitbilgileri.emailkayit==true){
            EmailIleKaytIslemi == true
           gelenEmail =kayitbilgileri.email!!
            Log.i("ibo","Gelen tel NO"+gelenEmail)
            Toast.makeText(requireContext(),"Gelen Email "+gelenEmail,Toast.LENGTH_LONG).show()

        }
        else
        {
            EmailIleKaytIslemi == false
            // telefon kayit islemleri yapilacak
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

}