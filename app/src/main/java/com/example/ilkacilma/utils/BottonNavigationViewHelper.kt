package com.example.ilkacilma.utils

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import com.example.ilkacilma.R
import com.example.ilkacilma.home.MainActivity
import com.example.ilkacilma.news.newsActivity
import com.example.ilkacilma.profile.profileActivity
import com.example.ilkacilma.search.srarchActivity
import com.example.ilkacilma.share.shareActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class BottonNavigationViewHelper {

    companion object{

        fun BottonNavigationView(bottonNavigationView: BottomNavigationView){


        }
        fun setupBottonNavigation(context: Context,bottonNavigationView: BottomNavigationView){
           bottonNavigationView.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener{
               override fun onNavigationItemSelected(item: MenuItem): Boolean {

                   when( item.itemId){

                       R.id.ic_home ->{
                           val intent = Intent(context,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                           context.startActivity(intent)
                           return true
                       }
                       R.id.ic_likes ->{
                           val intent = Intent(context,newsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                           context.startActivity(intent)
                           return true
                       }
                       R.id.ic_profile ->{
                           val intent = Intent(context,profileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                           context.startActivity(intent)
                           return true
                       }
                       R.id.ic_search ->{
                           val intent = Intent(context,srarchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                           context.startActivity(intent)
                           return true
                       }
                       R.id.ic_share ->{
                           val intent = Intent(context,shareActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                           context.startActivity(intent)
                           return true
                       }





                   }

                   return false
               }


           })

        }
    }

}