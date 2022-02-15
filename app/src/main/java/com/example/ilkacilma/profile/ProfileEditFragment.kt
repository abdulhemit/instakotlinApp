package com.example.ilkacilma.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.FragmentProfileEditBinding

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

        binding?.icCloseProfileEditFragment?.setOnClickListener {
            activity?.onBackPressed()
        }
        return binding?.root
    }


}