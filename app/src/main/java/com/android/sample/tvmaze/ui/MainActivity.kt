package com.android.sample.tvmaze.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.base.BaseActivity
import com.android.sample.tvmaze.databinding.ActivityMainBinding
import com.android.sample.tvmaze.util.Resource
import com.android.sample.tvmaze.util.applyExitMaterialTransform
import com.android.sample.tvmaze.util.hide
import com.android.sample.tvmaze.util.show
import com.android.sample.tvmaze.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.getViewModel

@ExperimentalCoroutinesApi
class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)

    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        applyExitMaterialTransform()
        super.onCreate(savedInstanceState)
        val viewModel = getViewModel<MainViewModel>()

        val viewModelAdapter = MainAdapter(this)
        binding.recyclerView.adapter = viewModelAdapter

        binding.retryButton.setOnClickListener {
            viewModel.refreshShows()
        }

        lifecycleScope.launch {
            viewModel.shows.collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        binding.loadingSpinner.hide()
                        binding.errorLayout.hide()
                        viewModelAdapter.submitList(resource.data)
                    }
                    Resource.Status.LOADING -> {
                        binding.loadingSpinner.show()
                        binding.errorLayout.hide()
                    }
                    Resource.Status.ERROR -> {
                        binding.loadingSpinner.hide()
                        binding.errorLayout.show()
                        binding.errorMsg.text = resource.message
                    }
                    Resource.Status.UPDATE -> {
                        viewModelAdapter.submitList(resource.data)
                    }
                    Resource.Status.WARNING -> {
                        Snackbar.make(binding.root, resource.message!!, Snackbar.LENGTH_LONG).show()
                    }
                    Resource.Status.IDLE -> {
                    }
                }
            }
        }

        binding.apply {
            vm = viewModel
            lifecycleOwner = this@MainActivity
        }
    }
}
