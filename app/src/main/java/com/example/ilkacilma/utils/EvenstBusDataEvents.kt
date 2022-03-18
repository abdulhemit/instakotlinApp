package com.example.ilkacilma.utils

import com.example.ilkacilma.Models.Users

class EvenstBusDataEvents {

    internal class KayitBilgileriniGonder(val telNO : String?,val email:String?,val varificationId:String?,val code:String?,val emailkayit:Boolean)
    internal class  kullaniciBilgileriniGonder(val kullanici:Users)
}