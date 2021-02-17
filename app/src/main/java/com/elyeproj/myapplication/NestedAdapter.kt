package com.elyeproj.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import com.google.android.material.tabs.TabLayoutMediator

class NestedAdapter : BaseAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_view_pager, parent, false)
        val internalViewPager = view.findViewById<ViewPager2>(R.id.internal_viewpager)
        internalViewPager.apply {
            adapter = MyAdapterFullLength()
            orientation = ORIENTATION_VERTICAL
        }

        TabLayoutMediator(view.findViewById(R.id.view_pager_inner_tabs), internalViewPager) { tab, position ->
            tab.text = position.toString()
        }.attach()

        return MyViewHolder(view)
    }
}
