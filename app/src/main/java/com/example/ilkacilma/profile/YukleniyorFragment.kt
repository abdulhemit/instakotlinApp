package com.example.ilkacilma.profile

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.DialogCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.FragmentKayitislemleriBinding
import com.example.ilkacilma.databinding.FragmentYukleniyorBinding


class YukleniyorFragment : DialogFragment() {

    private var _binding : FragmentYukleniyorBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentYukleniyorBinding.inflate(layoutInflater,container,false)

        binding?.progressBar2?.indeterminateDrawable?.setColorFilter(ContextCompat.getColor(requireContext(),R.color.siyah),PorterDuff.Mode.SRC_IN)
        return binding?.root
    }


}