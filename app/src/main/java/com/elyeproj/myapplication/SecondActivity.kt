package com.elyeproj.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.elyeproj.myapplication.MainActivity.Companion.TOTAL_ITEM
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {
    private var lastValue = 0f


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        view_pager_touch_pad.setOnTouchListener { _, event ->
            handleOnTouchEvent(event)
        }

        view_pager_quick_scroll.apply {
            adapter = MyAdapterFullLength()
        }

        TabLayoutMediator(view_pager_tabs, view_pager_quick_scroll) { tab, position ->
            tab.text = position.toString()
        }.attach()

        view_pager_preview.apply {
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

        TabLayoutMediator(view_pager_preview_tabs, view_pager_preview) { tab, position ->
            tab.text = position.toString()
        }.attach()

        view_pager_nested.apply {
            adapter = NestedAdapter()
        }

        TabLayoutMediator(view_pager_nested_tabs, view_pager_nested) { tab, position ->
            tab.text = position.toString()
        }.attach()

        infinite_view_pager2.apply {
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
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastValue = event.x
                view_pager_quick_scroll.beginFakeDrag()
            }

            MotionEvent.ACTION_MOVE -> {
                val value = event.x
                val delta = value - lastValue
                view_pager_quick_scroll.fakeDragBy(delta * TOTAL_ITEM)
                lastValue = value
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                view_pager_quick_scroll.endFakeDrag()
            }
        }
        return true
    }
}
