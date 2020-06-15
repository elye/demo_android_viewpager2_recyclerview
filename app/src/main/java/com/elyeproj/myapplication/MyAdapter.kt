package com.elyeproj.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup

class MyAdapter : BaseAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }
}
