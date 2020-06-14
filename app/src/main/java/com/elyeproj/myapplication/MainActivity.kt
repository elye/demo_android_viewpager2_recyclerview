package com.elyeproj.myapplication

import android.content.res.Resources
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        snap_method.setOnCheckedChangeListener { _, checkedId ->
            recycler_view.onFlingListener = null
            recycler_view.clearOnScrollListeners()
            recycler_view_full_length.onFlingListener = null
            recycler_view_full_length.clearOnScrollListeners()
            when (findViewById<RadioButton>(checkedId).id) {
                R.id.radio_linear_snap -> {
                    LinearSnapHelper().attachToRecyclerView(recycler_view)
                    LinearSnapHelper().attachToRecyclerView(recycler_view_full_length)
                }
                R.id.radio_page_snap -> {
                    PagerSnapHelper().attachToRecyclerView(recycler_view)
                    PagerSnapHelper().attachToRecyclerView(recycler_view_full_length)
                }
            }
        }

        recycler_view.apply {
            adapter = MyAdapter()
            addItemDecoration(MarginItemDecoration(resources.dpToPx(10)))
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        recycler_view_full_length.apply {
            adapter = MyAdapterFullLength()
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        view_pager.apply {
            adapter = MyAdapterFullLength()
        }
    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

class MarginItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                left = margin
            }
            top = margin
            right = margin
            bottom = margin
        }
    }
}

class MyAdapter : BaseAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }
}

class MyAdapterFullLength : BaseAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_full_length, parent, false)
        return MyViewHolder(view)
    }
}

abstract class BaseAdapter : RecyclerView.Adapter<MyViewHolder>() {

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Do nothing
    }
}

inline fun <reified T> Resources.dpToPx(value: Int): T {
    val result = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(), displayMetrics
    )

    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}