package com.android.sample.tvmaze.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.base.BaseActivity
import com.android.sample.tvmaze.databinding.ActivityMainBinding
import com.android.sample.tvmaze.util.Resource
import com.android.sample.tvmaze.util.applyExitMaterialTransform
import com.android.sample.tvmaze.util.hide
import com.android.sample.tvmaze.util.show
import com.android.sample.tvmaze.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.getViewModel

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        applyExitMaterialTransform()
        super.onCreate(savedInstanceState)
        val viewModel = getViewModel<MainViewModel>()

        val viewModelAdapter = MainAdapter(this)
        binding.recyclerView.adapter = viewModelAdapter

        viewModel.shows.observe(this, Observer { result ->
            when (result.status) {
                Resource.Status.SUCCESS -> {
                    binding.loadingSpinner.hide()
                    viewModelAdapter.submitList(result.data)
                }
                Resource.Status.LOADING -> binding.loadingSpinner.show()
                Resource.Status.ERROR -> {
                    binding.loadingSpinner.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        binding.apply {
            vm = viewModel
            lifecycleOwner = this@MainActivity
        }
    }
}
