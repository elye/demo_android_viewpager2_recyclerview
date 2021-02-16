package com.elyeproj.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewpager2.widget.ViewPager2

class InfiniteViewPager2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyle, defStyleRes) {

    private val viewPager2: ViewPager2 by lazy {
        findViewById(R.id.view_pager_infinite)
    }

    private val internalRecyclerView by lazy {
        viewPager2.getChildAt(0) as RecyclerView
    }

    private var totalItemCount = 0

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.just_viewpager2, this, true)
    }

    fun <T : RecyclerView.ViewHolder> setAdapter(adapter: Adapter<T>) {
        viewPager2.adapter = adapter
        viewPager2.setCurrentItem(1, false)
        totalItemCount = adapter.itemCount

        internalRecyclerView.apply {
            addOnScrollListener(
                InfiniteScrollBehaviour(
                    totalItemCount,
                    layoutManager as LinearLayoutManager
                )
            )
        }
    }

    fun addScrollListener(scrollListener: RecyclerView.OnScrollListener) {
        internalRecyclerView.addOnScrollListener(scrollListener)
    }

    fun removeScrollListener(scrollListener: RecyclerView.OnScrollListener) {
        internalRecyclerView.removeOnScrollListener(scrollListener)
    }

    fun getCurrentItem(): Int {
        return when (viewPager2.currentItem) {
            0 -> totalItemCount - 3
            totalItemCount - 1 -> 0
            else -> viewPager2.currentItem - 1
        }
    }

    inner class InfiniteScrollBehaviour(
        private val itemCount: Int,
        private val layoutManager: LinearLayoutManager
    ) : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
            val lastItemVisible = layoutManager.findLastVisibleItemPosition()
            if (firstItemVisible == (itemCount - 1) && dx > 0) {
                recyclerView.scrollToPosition(1)
            } else if (lastItemVisible == 0 && dx < 0) {
                recyclerView.scrollToPosition(itemCount - 2)
            }
        }
    }
}
