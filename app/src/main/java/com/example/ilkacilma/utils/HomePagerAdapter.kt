package com.example.ilkacilma.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePagerAdapter(val fm : FragmentManager, val lf : Lifecycle):FragmentStateAdapter(fm,lf) {

    private var mFragmentList:  ArrayList<Fragment> = ArrayList()


    // Fragment pager adapterin yazmayi zorunlu kildigi fonksiyonlar
    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    // Fragment pager adapterin yazmayi zorunlu kildigi fonksiyonlar
    override fun createFragment(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    // kisisel fonksiyonlar
    fun addFragments(fragment: Fragment){
        mFragmentList.add(fragment)
    }



}