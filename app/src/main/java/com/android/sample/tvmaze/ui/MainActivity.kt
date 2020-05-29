package com.android.sample.tvmaze.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.base.BaseActivity
import com.android.sample.tvmaze.databinding.ActivityMainBinding
import com.android.sample.tvmaze.util.Result
import com.android.sample.tvmaze.util.applyExitMaterialTransform
import com.android.sample.tvmaze.util.hide
import com.android.sample.tvmaze.util.show
import com.android.sample.tvmaze.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
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

        viewModel.shows.observe(this, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.loadingSpinner.hide()
                    viewModelAdapter.submitList(result.data)
                }
                Result.Status.LOADING -> binding.loadingSpinner.show()
                Result.Status.ERROR -> {
                    binding.loadingSpinner.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        binding.recyclerView.adapter = viewModelAdapter

        binding.apply {
            vm = viewModel
            lifecycleOwner = this@MainActivity
        }
    }
}
