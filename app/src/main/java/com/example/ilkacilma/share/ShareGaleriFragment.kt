package com.example.ilkacilma.share

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.ilkacilma.R
import com.example.ilkacilma.databinding.FragmentShareGaleriBinding
import com.example.ilkacilma.databinding.FragmentYukleniyorBinding


class ShareGaleriFragment : Fragment() {

    private var _binding : FragmentShareGaleriBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShareGaleriBinding.inflate(layoutInflater,container,false)

        var klasorPaths = ArrayList<String>()
        val klasorAdlari = ArrayList<String>()

        var root = Environment.getExternalStorageDirectory().path
        var KameraResimleri = root+ "/DCIM/Camera"
        var IndirilenResimler = root+ "/Download"
        var whatsappResimleri = root+ "/WhatsApp/Media/WhatsApp Images"

        klasorPaths.add(KameraResimleri)
        klasorPaths.add(IndirilenResimler)
        klasorPaths.add(whatsappResimleri)

        klasorAdlari.add("Kamera")
        klasorAdlari.add("Indirilenler")
        klasorAdlari.add("WhatsApp")

        var spinnerArrayAdapter =ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,klasorAdlari)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding?.spinner?.adapter = spinnerArrayAdapter

        return binding?.root
    }


}