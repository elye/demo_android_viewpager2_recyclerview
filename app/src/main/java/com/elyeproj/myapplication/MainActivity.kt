package com.elyeproj.myapplication

import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class MainActivity : FragmentActivity() {

    private val pageTransformer = ViewPager2.PageTransformer { page, position ->
        val absPos = abs(position)
        page.apply {
            translationX = if (orientation_transition.checkedRadioButtonId == R.id.radio_horizontal_orientation)
                -absPos * width else 0f
            translationY = if (orientation_transition.checkedRadioButtonId == R.id.radio_vertical_orientation)
                -absPos  *height else 0f
            val scale = if (absPos > 1) 0.8F else 1 - absPos*0.2F
            scaleX = scale
            scaleY = scale
            alpha = (1 - absPos)
        }
    }

    private val defaultPageTransformer = ViewPager2.PageTransformer { page, position ->
        page.apply {
            translationX = 0f
            translationY = 0f
            scaleX = 1f
            scaleY = 1f
            alpha = 1f
        }
    }

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

        transform_method.setOnCheckedChangeListener { _, checkedId ->
            when (findViewById<RadioButton>(checkedId).id) {
                R.id.radio_no_transform -> {
                    view_pager.setPageTransformer(defaultPageTransformer)
                    view_pager_fragment.setPageTransformer(defaultPageTransformer)
                }
                R.id.radio_do_transform -> {
                    view_pager.setPageTransformer(pageTransformer)
                    view_pager_fragment.setPageTransformer(pageTransformer)
                }
            }
        }

        orientation_transition.setOnCheckedChangeListener { _, checkedId ->
            when (findViewById<RadioButton>(checkedId).id) {
                R.id.radio_horizontal_orientation -> {
                    view_pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    view_pager_fragment.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                }
                R.id.radio_vertical_orientation -> {
                    view_pager.orientation = ViewPager2.ORIENTATION_VERTICAL
                    view_pager_fragment.orientation = ViewPager2.ORIENTATION_VERTICAL
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

        view_pager_fragment.apply {
            adapter = MyFragmentStateAdapter(this@MainActivity)
        }

        TabLayoutMediator(view_pager_fragment_tabs, view_pager_fragment) { tab, position ->
            tab.text = position.toString()
        }.attach()
    }
}
