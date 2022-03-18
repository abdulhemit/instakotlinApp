package com.example.ilkacilma.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ilkacilma.Models.Users
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.FragmentProfileEditBinding
import com.example.ilkacilma.utils.EvenstBusDataEvents
import com.example.ilkacilma.utils.UniversalImageLoader
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ProfileEditFragment : Fragment() {
    private var _binding : FragmentProfileEditBinding? = null
    private val binding get() = _binding
    lateinit var mDatabase : FirebaseFirestore
    lateinit var GelenkullaniciBilgileri : Users
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabase = FirebaseFirestore.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileEditBinding.inflate(layoutInflater,container,false)

        setupKullaniciBilgileri()
        binding?.fotografiDegister?.setOnClickListener {
            gorselYap()
        }

        binding?.icCheckEditProfileFragment?.setOnClickListener {

                if (!GelenkullaniciBilgileri.AdSoyad!!.equals(binding?.adEditText?.text.toString())){
                   val adsoyad = binding?.adEditText?.text.toString()
                    mDatabase.collection("kullanicilar").document(GelenkullaniciBilgileri.user_id!!).update("adSoyad",adsoyad)
                }
            if (!GelenkullaniciBilgileri.user_details?.biography.equals(binding!!.BiyografiEditText.text.toString())){
                val biografi = binding!!.BiyografiEditText.text.toString()
                mDatabase.collection("kullanicilar").document(GelenkullaniciBilgileri.user_id!!).update("user_details.biography",biografi)
            }
            if(!GelenkullaniciBilgileri.user_details?.web_site.equals(binding!!.internetStesiEditText.text.toString())){
                val web_site = binding!!.internetStesiEditText.text.toString()
                mDatabase.collection("kullanicilar").document(GelenkullaniciBilgileri.user_id!!).update("user_details.web_site",web_site)
                }
            if (!GelenkullaniciBilgileri.user_Name.equals(binding!!.KullaniciAdEditText.text.toString())){

                mDatabase.collection("kullanicilar").orderBy("user_Name").addSnapshotListener { value, error ->

                    if (error != null ){
                        Toast.makeText(requireContext(),error.localizedMessage.toString(),Toast.LENGTH_LONG).show()
                    }else{
                        if (value != null ){

                            var  userNameKUllanimdaMI = false
                            for ( dc in value){

                                val okunanKullaniciAdi = dc.toObject(Users::class.java).user_Name
                                if(okunanKullaniciAdi.equals(binding!!.KullaniciAdEditText.text.toString())){
                                    Toast.makeText(requireContext(),"Bu kullanıcı adı Kullanımda",Toast.LENGTH_LONG).show()
                                    userNameKUllanimdaMI  = true
                                    break
                                }


                            }
                            if (userNameKUllanimdaMI == false ){
                                val user_name = binding!!.KullaniciAdEditText.text.toString()
                                mDatabase.collection("kullanicilar").document(GelenkullaniciBilgileri.user_id!!).update("user_Name",user_name)

                            }

                        }

                    }
                }

                Toast.makeText(requireContext(),"kullanıdı adı güncellendi ",Toast.LENGTH_LONG).show()
            }
        }

        binding?.icCloseProfileEditFragment?.setOnClickListener {
            activity?.onBackPressed()
        }
        return binding?.root
    }

    fun gorselYap(){
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        }else {
            val GaleriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(GaleriIntent,2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){

            binding?.profileFragmentImage?.setImageURI(data.data)

        }

        super.onActivityResult(requestCode, resultCode, data)
    }



    private fun setupKullaniciBilgileri() {
            binding?.adEditText?.setText(GelenkullaniciBilgileri.AdSoyad.toString())
            binding?.KullaniciAdEditText?.setText(GelenkullaniciBilgileri.user_Name.toString())
            if (!GelenkullaniciBilgileri.user_details?.biography.isNullOrEmpty()){
                binding?.BiyografiEditText?.setText(GelenkullaniciBilgileri.user_details?.biography.toString())
            }
            if (!GelenkullaniciBilgileri.user_details?.web_site.isNullOrEmpty()){
                binding?.internetStesiEditText?.setText(GelenkullaniciBilgileri.user_details?.web_site.toString())
            }
            val defultImage = R.drawable.ic_person_add_24
            val imgURL = GelenkullaniciBilgileri.user_details?.profile_pucture ?: "$defultImage"
            binding?.profileFragmentImage?.let {
                UniversalImageLoader.setImage(imgURL,it,binding?.progressBar,"")
            }


    }


    ///////////////////// EventBus//////////////////////////////////////
    @Subscribe(sticky = true)
    internal fun onKullaniciBilgileriEvent(kullanicibilgileri:EvenstBusDataEvents.kullaniciBilgileriniGonder){

        GelenkullaniciBilgileri = kullanicibilgileri.kullanici
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