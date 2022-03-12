package com.example.ilkacilma.Models

class users {

    var user_id : String? = null
    var AdSoyad : String? = null
    var pasword : String? = null
    var user_Name : String? = null
    var email : String? = null
    var user_details : userDetails? = null

    constructor(){}

    constructor(
        user_id: String?,
        AdSoyad: String?,
        pasword: String?,
        user_Name: String?,
        email: String?,
        user_details: userDetails?
    ) {
        this.user_id = user_id
        this.AdSoyad = AdSoyad
        this.pasword = pasword
        this.user_Name = user_Name
        this.email = email
        this.user_details = user_details
    }

    override fun toString(): String {
        return "users(user_id=$user_id, AdSoyad=$AdSoyad, pasword=$pasword, user_Name=$user_Name, email=$email, user_details=$user_details)"
    }


}