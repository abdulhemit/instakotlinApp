package com.example.ilkacilma.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.FragmentProfileEditBinding
import com.example.ilkacilma.utils.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener

class ProfileEditFragment : Fragment() {
    private var _binding : FragmentProfileEditBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileEditBinding.inflate(layoutInflater,container,false)
        setupProfilePicture()


        binding?.icCloseProfileEditFragment?.setOnClickListener {
            activity?.onBackPressed()
        }
        return binding?.root
    }



    private fun setupProfilePicture() {

        // internetten aldigimiz resmin adresi
        //https://stickker.net/wp-content/uploads/2021/10/Squid-Game-Soldier-Circle.png


        val imgURL ="stickker.net/wp-content/uploads/2021/10/Squid-Game-Soldier-Circle.png"


        UniversalImageLoader.setImage(imgURL,binding!!.profileFragmentImage,null,"https://")





    }


}