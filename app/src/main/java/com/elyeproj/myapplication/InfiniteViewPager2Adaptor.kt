package com.elyeproj.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class InfiniteViewPager2Adaptor(itemListOriginal: List<String>) :
    RecyclerView.Adapter<MyViewHolder>() {

    private val itemList: List<String> = listOf(itemListOriginal.last() + "-Faked") +
                itemListOriginal +
                listOf(itemListOriginal.first() + "-Faked")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_full_length, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.txt_label.text = itemList[position]
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}
