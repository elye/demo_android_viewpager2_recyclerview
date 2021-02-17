package com.elyeproj.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.elyeproj.myapplication.MainActivity.Companion.TOTAL_ITEM
import com.google.android.material.tabs.TabLayoutMediator

class SecondActivity : AppCompatActivity() {
    private var lastValue = 0f


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        findViewById<View>(R.id.view_pager_touch_pad).setOnTouchListener { _, event ->
            handleOnTouchEvent(event)
        }

        val viewPagerQuickScroll = findViewById<ViewPager2>(R.id.view_pager_quick_scroll)
        val viewPagerPreview = findViewById<ViewPager2>(R.id.view_pager_preview)
        val viewPagerNested = findViewById<ViewPager2>(R.id.view_pager_nested)

        viewPagerQuickScroll.apply {
            adapter = MyAdapterFullLength()
        }

        TabLayoutMediator(findViewById(R.id.view_pager_tabs), viewPagerQuickScroll) { tab, position ->
            tab.text = position.toString()
        }.attach()

        viewPagerPreview.apply {
            // Set offscreen page limit to at least 1, so adjacent pages are always laid out
            offscreenPageLimit = 1
            val recyclerView = getChildAt(0) as RecyclerView
            recyclerView.apply {
                val padding = resources.getDimensionPixelOffset(R.dimen.margin_card) +
                        resources.getDimensionPixelOffset(R.dimen.peek_offset_card)
                // setting padding on inner RecyclerView puts overscroll effect in the right place
                // Google will change not to rely on getChildAt(0) which might break
                setPadding(padding, 0, padding, 0)
                clipToPadding = false
            }
            adapter = MyAdapterFullLength()
        }

        TabLayoutMediator(findViewById(R.id.view_pager_preview_tabs), viewPagerPreview) { tab, position ->
            tab.text = position.toString()
        }.attach()

        viewPagerNested.apply {
            adapter = NestedAdapter()
        }

        TabLayoutMediator(findViewById(R.id.view_pager_nested_tabs), viewPagerNested) { tab, position ->
            tab.text = position.toString()
        }.attach()

        findViewById<InfiniteViewPager2>(R.id.infinite_view_pager2).apply {
            setAdapter(InfiniteViewPager2Adaptor(listOf("First", "Second", "Third", "Last")))
            addScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        Log.d("Tracking", "I am at item index ${getCurrentItem()}")
                    }
                }
            })
        }
    }

    private fun handleOnTouchEvent(event: MotionEvent): Boolean {
        val viewPagerQuickScroll = findViewById<ViewPager2>(R.id.view_pager_quick_scroll)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastValue = event.x
                viewPagerQuickScroll.beginFakeDrag()
            }

            MotionEvent.ACTION_MOVE -> {
                val value = event.x
                val delta = value - lastValue
                viewPagerQuickScroll.fakeDragBy(delta * TOTAL_ITEM)
                lastValue = value
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                viewPagerQuickScroll.endFakeDrag()
            }
        }
        return true
    }
}
