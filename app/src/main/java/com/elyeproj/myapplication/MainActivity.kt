package com.elyeproj.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs

class MainActivity : FragmentActivity() {

    companion object {
        const val TOTAL_ITEM = 10
    }

    val orientationTransition by lazy {
        findViewById<RadioGroup>(R.id.orientation_transition)
    }

    private var lastValue = 0f

    private val pageTransformer = ViewPager2.PageTransformer { page, position ->
        val absPos = abs(position)
        page.apply {
            translationX = if (orientationTransition.checkedRadioButtonId == R.id.radio_horizontal_orientation)
                -absPos * width else 0f
            translationY = if (orientationTransition.checkedRadioButtonId == R.id.radio_vertical_orientation)
                -absPos  *height else 0f
            val scale = if (absPos > 1) 0.8F else 1 - absPos*0.2F
            scaleX = scale
            scaleY = scale
            alpha = (1 - absPos)
        }
    }

    private val defaultPageTransformer = ViewPager2.PageTransformer { page, _ ->
        page.apply {
            translationX = 0f
            translationY = 0f
            scaleX = 1f
            scaleY = 1f
            alpha = 1f
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val snapMethod = findViewById<RadioGroup>(R.id.snap_method)

        snapMethod.setOnCheckedChangeListener { _, checkedId ->
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            val recyclerViewFullLength = findViewById<RecyclerView>(R.id.recycler_view_full_length)
            recyclerView.onFlingListener = null
            recyclerView.clearOnScrollListeners()
            recyclerViewFullLength.onFlingListener = null
            recyclerViewFullLength.clearOnScrollListeners()
            when (findViewById<RadioButton>(checkedId).id) {
                R.id.radio_linear_snap -> {
                    LinearSnapHelper().attachToRecyclerView(recyclerView)
                    LinearSnapHelper().attachToRecyclerView(recyclerViewFullLength)
                }
                R.id.radio_page_snap -> {
                    PagerSnapHelper().attachToRecyclerView(recyclerView)
                    PagerSnapHelper().attachToRecyclerView(recyclerViewFullLength)
                }
            }
        }

        val transformMethod = findViewById<RadioGroup>(R.id.transform_method)

        transformMethod.setOnCheckedChangeListener { _, checkedId ->
            val viewPager = findViewById<ViewPager2>(R.id.view_pager)
            val viewPagerFragment = findViewById<ViewPager2>(R.id.view_pager_fragment)
            when (findViewById<RadioButton>(checkedId).id) {
                R.id.radio_no_transform -> {
                    viewPager.setPageTransformer(defaultPageTransformer)
                    viewPagerFragment.setPageTransformer(defaultPageTransformer)
                }
                R.id.radio_do_transform -> {
                    viewPager.setPageTransformer(pageTransformer)
                    viewPagerFragment.setPageTransformer(pageTransformer)
                }
            }
        }

        val orientationTransition = findViewById<RadioGroup>(R.id.orientation_transition)

        orientationTransition.setOnCheckedChangeListener { _, checkedId ->
            val viewPager = findViewById<ViewPager2>(R.id.view_pager)
            val viewPagerFragment = findViewById<ViewPager2>(R.id.view_pager_fragment)
            when (findViewById<RadioButton>(checkedId).id) {
                R.id.radio_horizontal_orientation -> {
                    viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    viewPagerFragment.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                }
                R.id.radio_vertical_orientation -> {
                    viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
                    viewPagerFragment.orientation = ViewPager2.ORIENTATION_VERTICAL
                }
            }
        }

        val viewPagerFragmentTouchPad = findViewById<View>(R.id.view_pager_fragment_touch_pad)
        viewPagerFragmentTouchPad.setOnTouchListener { _, event ->
            handleOnTouchEvent(event)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val recyclerViewFullLength = findViewById<RecyclerView>(R.id.recycler_view_full_length)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val viewPagerFragment = findViewById<ViewPager2>(R.id.view_pager_fragment)

        recyclerView.apply {
            adapter = MyAdapter()
            addItemDecoration(MarginItemDecoration(resources.dpToPx(10)))
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        recyclerViewFullLength.apply {
            adapter = MyAdapterFullLength()
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        viewPager.apply {
            adapter = MyAdapterFullLength()
        }

        viewPagerFragment.apply {
            adapter = MyFragmentStateAdapter(this@MainActivity)
        }

        val viewPagerFragmentTabs = findViewById<TabLayout>(R.id.view_pager_fragment_tabs)

        TabLayoutMediator(viewPagerFragmentTabs, viewPagerFragment) { tab, position ->
            tab.text = position.toString()
        }.attach()
    }

    private fun handleOnTouchEvent(event: MotionEvent): Boolean {
        val viewPagerFragment = findViewById<ViewPager2>(R.id.view_pager_fragment)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastValue = event.x
                viewPagerFragment.beginFakeDrag()
            }

            MotionEvent.ACTION_MOVE -> {
                val value = event.x
                val delta = value - lastValue
                viewPagerFragment.fakeDragBy(delta)
                lastValue = value
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                viewPagerFragment.endFakeDrag()
            }
        }
        return true
    }
}
