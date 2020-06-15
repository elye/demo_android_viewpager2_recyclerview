package com.elyeproj.myapplication

import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {

    private val pageTransformer = ViewPager2.PageTransformer { page, position ->
        val absPos = Math.abs(position)
        page.apply {
            translationX = -absPos*width
            val scale = if (absPos > 1) 0.8F else 1 - absPos*0.2F
            scaleX = scale
            scaleY = scale
            alpha = (1 - absPos)
        }
    }

    private val defaultPageTransformer = ViewPager2.PageTransformer { page, position ->
        val absPos = Math.abs(position)
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
    }
}
