package com.elyeproj.myapplication

import androidx.recyclerview.widget.RecyclerView
import com.elyeproj.myapplication.MainActivity.Companion.TOTAL_ITEM

abstract class BaseAdapter : RecyclerView.Adapter<MyViewHolder>() {

    override fun getItemCount(): Int {
        return TOTAL_ITEM
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Do nothing
    }
}
