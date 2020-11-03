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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.getViewModel
import timber.log.Timber

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
                        Timber.d("S")
                        binding.loadingSpinner.hide()
                        binding.errorLayout.hide()
                        viewModelAdapter.submitList(resource.data)
                    }
                    Resource.Status.LOADING -> {
                        Timber.d("L")
                        binding.loadingSpinner.show()
                        binding.errorLayout.hide()
                    }
                    Resource.Status.ERROR -> {
                        Timber.d("E")
                        binding.loadingSpinner.hide()
                        binding.errorLayout.show()
                        binding.errorMsg.text = resource.message
                    }
                    Resource.Status.UPDATE -> {
                        Timber.d("U")
                        viewModelAdapter.submitList(resource.data)
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
