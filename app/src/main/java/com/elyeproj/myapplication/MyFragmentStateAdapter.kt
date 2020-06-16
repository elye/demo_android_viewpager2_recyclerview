package com.elyeproj.myapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.elyeproj.myapplication.MainActivity.Companion.TOTAL_ITEM

class MyFragmentStateAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return MyItemFragment()
    }

    override fun getItemCount(): Int {
        return TOTAL_ITEM
    }
}
