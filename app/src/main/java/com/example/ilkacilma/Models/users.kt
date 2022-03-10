package com.example.ilkacilma.Models

class users {

    var user_id : String? = null
    var AdSoyad : String? = null
    var pasword : String? = null
    var user_Name : String? = null
    var email : String? = null

    constructor(email:String?, pasword:String?, user_Name:String?, AdSoyad: String?, user_id: String?){
        this.email = email
        this.pasword = pasword
        this.AdSoyad = AdSoyad
        this.user_Name = user_Name
        this.user_id = user_id
    }
}