package com.example.ilkacilma.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class SharePagerAdapter(fm : FragmentManager,lc:Lifecycle ):FragmentStateAdapter(fm,lc) {

    private val mFragmentlist : ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int {
        return mFragmentlist.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentlist.get(position)
    }

    fun addFragments(fragment: Fragment){
        mFragmentlist.add(fragment)
    }

}