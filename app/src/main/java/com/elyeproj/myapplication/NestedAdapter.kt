package com.elyeproj.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.list_item_view_pager.view.*

class NestedAdapter : BaseAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_view_pager, parent, false)
        view.internal_viewpager.apply {
            adapter = MyAdapterFullLength()
            orientation = ORIENTATION_VERTICAL
        }

        TabLayoutMediator(view.view_pager_inner_tabs, view.internal_viewpager) { tab, position ->
            tab.text = position.toString()
        }.attach()

        return MyViewHolder(view)
    }
}
