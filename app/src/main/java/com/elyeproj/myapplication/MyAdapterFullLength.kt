package com.elyeproj.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup

class MyAdapterFullLength : BaseAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_full_length, parent, false)
        return MyViewHolder(view)
    }
}