package com.android.sample.tvmaze.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.base.BaseActivity
import com.android.sample.tvmaze.databinding.ActivityMainBinding
import com.android.sample.tvmaze.util.applyExitMaterialTransform
import com.android.sample.tvmaze.viewmodel.MainViewModel
import org.koin.android.viewmodel.ext.android.getViewModel

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)

    /**
     * RecyclerView Adapter for converting a list of shows to cards.
     */
    private lateinit var viewModelAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        applyExitMaterialTransform()
        super.onCreate(savedInstanceState)
        val viewModel = getViewModel<MainViewModel>()

        viewModelAdapter = MainAdapter(this)

        viewModel.shows.observe(this, Observer { shows ->
            viewModelAdapter.submitList(shows)
        })

        binding.apply {
            vm = viewModel
            lifecycleOwner = this@MainActivity
            recyclerView.adapter = viewModelAdapter
        }
    }
}
