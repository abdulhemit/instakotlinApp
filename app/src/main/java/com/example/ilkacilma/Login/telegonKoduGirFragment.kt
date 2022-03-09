package com.example.ilkacilma.Login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ilkacilma.R
import com.example.ilkacilma.utils.EvenstBusDataEvents
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class telegonKoduGirFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_telegon_kodu_gir, container, false)
    }
    @Subscribe (sticky = true)
    internal fun onTelefonNOEvent(Kayitbilgileri:EvenstBusDataEvents.KayitBilgileriniGonder){

        val gelentelNO =Kayitbilgileri.telNO
        Log.i("ibo","Gelen tel NO"+gelentelNO)

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