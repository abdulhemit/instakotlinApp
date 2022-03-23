package com.example.ilkacilma.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception



class ProfileEditFragment : Fragment() {
    private var _binding : FragmentProfileEditBinding? = null
    private val binding get() = _binding
    lateinit var mDatabase : FirebaseFirestore
    lateinit var GelenkullaniciBilgileri : Users
    lateinit var storage : FirebaseStorage
    var profilePhoto : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabase = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

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

        ////////////////////////////////////////////////// konrton /////////////////////////////////////////////////
        binding?.icCheckEditProfileFragment?.setOnClickListener {

            if (profilePhoto != null ){
                var diaogYukleniyor = YukleniyorFragment()
                activity?.let { it1 -> diaogYukleniyor.show(it1.supportFragmentManager,"yukleniyorFragmenti") }
                diaogYukleniyor.isCancelable = false


                storage.reference.child("kullanicilar").child(GelenkullaniciBilgileri.user_id!!)
                    .child(profilePhoto!!.lastPathSegment!!).putFile(profilePhoto!!)
                    .addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot>{
                        override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                            if (p0.isSuccessful){
                                val yuklenengorselreference = storage.reference.child("kullanicilar").child(GelenkullaniciBilgileri.user_id!!).child(profilePhoto!!.lastPathSegment!!)
                                yuklenengorselreference.downloadUrl.addOnSuccessListener {
                                    val downloadUrl = it
                                    mDatabase.collection("kullanicilar").document(GelenkullaniciBilgileri.user_id!!).update("user_details.profile_pucture",downloadUrl.toString())
                                }
                                diaogYukleniyor.dismiss()
                                KullaniciAdiniGuncelle(true)
                            }
                        }
                    })
                    .addOnFailureListener(object : OnFailureListener{
                        override fun onFailure(p0: Exception) {
                            Log.i("hata",p0.localizedMessage.toString())
                            KullaniciAdiniGuncelle(false)
                        }

                    })

            }else {
                KullaniciAdiniGuncelle(null)
            }

        }

        binding?.icCloseProfileEditFragment?.setOnClickListener {
            activity?.onBackPressed()
        }
        return binding?.root
    }
    // profie resmi degisti
    // true : basarili bir sekilde resim storege yuklenmis ve veritabanina yazilmistir
    // false : resim yuklenirken hata olusmustur
    // null : kullanici resmini degistirmek istememistir
    private fun KullaniciAdiniGuncelle(profileResmiDegisti: Boolean?) {

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
                                userNameKUllanimdaMI  = true
                                profileBilgileriniGuncelle(profileResmiDegisti,false)
                            }
                            break

                        }
                        if (userNameKUllanimdaMI == false ){
                            val user_name = binding!!.KullaniciAdEditText.text.toString()
                            mDatabase.collection("kullanicilar").document(GelenkullaniciBilgileri.user_id!!).update("user_Name",user_name)
                            profileBilgileriniGuncelle(profileResmiDegisti,true)
                        }

                    }

                }
            }
        }
        else{

            profileBilgileriniGuncelle(profileResmiDegisti,null)
        }

    }

    private fun profileBilgileriniGuncelle(profileResmiDegisti: Boolean?, userNameDegisti: Boolean?) {

        var kullanidiAdiGulcellendimi:Boolean? = null

        val adsoyad = binding?.adEditText?.text.toString()
        val biografi = binding!!.BiyografiEditText.text.toString()
        val web_site = binding!!.internetStesiEditText.text.toString()

        var adsoyad_ayni = GelenkullaniciBilgileri.AdSoyad!!.equals(adsoyad)
        var biyografi_ayni = GelenkullaniciBilgileri.user_details?.biography.equals(biografi)
        var website_ayni = GelenkullaniciBilgileri.user_details?.web_site.equals(web_site)


        val fieldMap: MutableMap<String, String> = mutableMapOf()
        if (!adsoyad_ayni) fieldMap.put("adSoyad", adsoyad)
        if (!biyografi_ayni) fieldMap.put("user_details.biography", biografi)
        if (!website_ayni) fieldMap.put("user_details.web_site", web_site)

        if (fieldMap.size > 0) {
            mDatabase.collection("kullanicilar").document(GelenkullaniciBilgileri.user_id!!).update(
                fieldMap as Map<String, Any>
            )
                .addOnSuccessListener {
                    kullanidiAdiGulcellendimi = true
                    if (profileResmiDegisti == null && userNameDegisti == null && kullanidiAdiGulcellendimi == null ){
                        Toast.makeText(requireContext(),"Değişiklik Yok ",Toast.LENGTH_LONG).show()
                    }else if (userNameDegisti == false && ( kullanidiAdiGulcellendimi == true || profileResmiDegisti == true)){
                        Toast.makeText(requireContext(),"Bilgiler güncellendi ama kullanıcı adı KULLANIMDA ",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(activity?.applicationContext,"kullanıcı  güncellendi  ",Toast.LENGTH_LONG).show()
                        activity?.onBackPressed()
                    }
                }

        }





    }



    ////////////////////////////////////////////////// konrton /////////////////////////////////////////////////


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


            profilePhoto = data.data
            binding?.profileFragmentImage?.setImageURI(profilePhoto)

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


//class ProfileEditFragment : Fragment() {
//    private var _binding : FragmentProfileEditBinding? = null
//    private val binding get() = _binding
//    lateinit var mDatabase : FirebaseFirestore
//    lateinit var GelenkullaniciBilgileri : Users
//    lateinit var storage : FirebaseStorage
//    var profilePhoto : Uri? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mDatabase = FirebaseFirestore.getInstance()
//        storage = FirebaseStorage.getInstance()
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentProfileEditBinding.inflate(layoutInflater,container,false)
//
//        setupKullaniciBilgileri()
//        binding?.fotografiDegister?.setOnClickListener {
//            gorselYap()
//        }
//
//        ////////////////////////////////////////////////// konrton /////////////////////////////////////////////////
//        binding?.icCheckEditProfileFragment?.setOnClickListener {
//
//            if (profilePhoto != null ){
//                var diaogYukleniyor = YukleniyorFragment()
//                activity?.let { it1 -> diaogYukleniyor.show(it1.supportFragmentManager,"yukleniyorFragmenti") }
//                diaogYukleniyor.isCancelable = false
//
//
//                storage.reference.child("kullanicilar").child(GelenkullaniciBilgileri.user_id!!)
//                    .child(profilePhoto!!.lastPathSegment!!).putFile(profilePhoto!!)
//                    .addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot>{
//                        override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
//                            if (p0.isSuccessful){
//                                val yuklenengorselreference = storage.reference.child("kullanicilar").child(GelenkullaniciBilgileri.user_id!!).child(profilePhoto!!.lastPathSegment!!)
//                                yuklenengorselreference.downloadUrl.addOnSuccessListener {
//                                    val downloadUrl = it
//                                    mDatabase.collection("kullanicilar").document(GelenkullaniciBilgileri.user_id!!).update("user_details.profile_pucture",downloadUrl.toString())
//                                }
//                                diaogYukleniyor.dismiss()
//                                KullaniciAdiniGuncelle(true)
//                            }
//                        }
//                    })
//                    .addOnFailureListener(object : OnFailureListener{
//                        override fun onFailure(p0: Exception) {
//                            Log.i("hata",p0.localizedMessage.toString())
//                            KullaniciAdiniGuncelle(false)
//                        }
//
//                    })
//
//            }else {
//                KullaniciAdiniGuncelle(null)
//            }
//
//        }
//
//        binding?.icCloseProfileEditFragment?.setOnClickListener {
//            activity?.onBackPressed()
//        }
//        return binding?.root
//    }
//    // profie resmi degisti
//    // true : basarili bir sekilde resim storege yuklenmis ve veritabanina yazilmistir
//    // false : resim yuklenirken hata olusmustur
//    // null : kullanici resmini degistirmek istememistir
//    private fun KullaniciAdiniGuncelle(profileResmiDegisti: Boolean?) {
//
//        if (!GelenkullaniciBilgileri.user_Name.equals(binding!!.KullaniciAdEditText.text.toString())){
//
//            mDatabase.collection("kullanicilar").orderBy("user_Name").addSnapshotListener { value, error ->
//
//                if (error != null ){
//                    Toast.makeText(requireContext(),error.localizedMessage.toString(),Toast.LENGTH_LONG).show()
//                }else{
//                    if (value != null ){
//
//                        var  userNameKUllanimdaMI = false
//                        for ( dc in value){
//
//                            val okunanKullaniciAdi = dc.toObject(Users::class.java).user_Name
//                            if(okunanKullaniciAdi.equals(binding!!.KullaniciAdEditText.text.toString())){
//                                userNameKUllanimdaMI  = true
//                                profileBilgileriniGuncelle(profileResmiDegisti,false)
//                            }
//                            break
//
//                        }
//                        if (userNameKUllanimdaMI == false ){
//                            val user_name = binding!!.KullaniciAdEditText.text.toString()
//                            mDatabase.collection("kullanicilar").document(GelenkullaniciBilgileri.user_id!!).update("user_Name",user_name)
//                            profileBilgileriniGuncelle(profileResmiDegisti,true)
//                        }
//
//                    }
//
//                }
//            }
//        }
//        else{
//
//            profileBilgileriniGuncelle(profileResmiDegisti,null)
//        }
//
//    }
//
//    private fun profileBilgileriniGuncelle(profileResmiDegisti: Boolean?, userNameDegisti: Boolean?) {
//
//        var kullanidiAdiGulcellendimi:Boolean? = null
//
//        if (!GelenkullaniciBilgileri.AdSoyad!!.equals(binding?.adEditText?.text.toString())){
//            val adsoyad = binding?.adEditText?.text.toString()
//            mDatabase.collection("kullanicilar").document(GelenkullaniciBilgileri.user_id!!).update("adSoyad",adsoyad)
//            kullanidiAdiGulcellendimi = true
//        }
//        if (!GelenkullaniciBilgileri.user_details?.biography.equals(binding!!.BiyografiEditText.text.toString())){
//            val biografi = binding!!.BiyografiEditText.text.toString()
//            mDatabase.collection("kullanicilar").document(GelenkullaniciBilgileri.user_id!!).update("user_details.biography",biografi)
//            kullanidiAdiGulcellendimi = true
//        }
//        if(!GelenkullaniciBilgileri.user_details?.web_site.equals(binding!!.internetStesiEditText.text.toString())){
//            val web_site = binding!!.internetStesiEditText.text.toString()
//            mDatabase.collection("kullanicilar").document(GelenkullaniciBilgileri.user_id!!).update("user_details.web_site",web_site)
//            kullanidiAdiGulcellendimi = true
//        }
//
//
//        if (profileResmiDegisti == null && userNameDegisti == null && kullanidiAdiGulcellendimi == null ){
//            Toast.makeText(requireContext(),"Değişiklik Yok ",Toast.LENGTH_LONG).show()
//        }else if (userNameDegisti == false && ( kullanidiAdiGulcellendimi == true || profileResmiDegisti == true)){
//            Toast.makeText(requireContext(),"Bilgiler güncellendi ama kullanıcı adı KULLANIMDA ",Toast.LENGTH_LONG).show()
//        }else{
//            Toast.makeText(activity?.applicationContext,"kullanıcı  güncellendi  ",Toast.LENGTH_LONG).show()
//            //activity?.onBackPressed()
//        }
//    }
//
//
//
//    ////////////////////////////////////////////////// konrton /////////////////////////////////////////////////
//
//
//    fun gorselYap(){
//        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
//
//        }else {
//            val GaleriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(GaleriIntent,2)
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
//
//
//            profilePhoto = data.data
//            binding?.profileFragmentImage?.setImageURI(profilePhoto)
//
//        }
//
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//    private fun setupKullaniciBilgileri() {
//        binding?.adEditText?.setText(GelenkullaniciBilgileri.AdSoyad.toString())
//        binding?.KullaniciAdEditText?.setText(GelenkullaniciBilgileri.user_Name.toString())
//        if (!GelenkullaniciBilgileri.user_details?.biography.isNullOrEmpty()){
//            binding?.BiyografiEditText?.setText(GelenkullaniciBilgileri.user_details?.biography.toString())
//        }
//        if (!GelenkullaniciBilgileri.user_details?.web_site.isNullOrEmpty()){
//            binding?.internetStesiEditText?.setText(GelenkullaniciBilgileri.user_details?.web_site.toString())
//        }
//        val defultImage = R.drawable.ic_person_add_24
//        val imgURL = GelenkullaniciBilgileri.user_details?.profile_pucture ?: "$defultImage"
//        binding?.profileFragmentImage?.let {
//            UniversalImageLoader.setImage(imgURL,it,binding?.progressBar,"")
//        }
//
//
//    }
//
//
//    ///////////////////// EventBus//////////////////////////////////////
//    @Subscribe(sticky = true)
//    internal fun onKullaniciBilgileriEvent(kullanicibilgileri:EvenstBusDataEvents.kullaniciBilgileriniGonder){
//
//        GelenkullaniciBilgileri = kullanicibilgileri.kullanici
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        EventBus.getDefault().register(this)
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        EventBus.getDefault().unregister(this)
//    }
//
//
//
//
//
//
//
//}