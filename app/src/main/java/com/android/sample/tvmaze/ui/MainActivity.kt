package com.android.sample.tvmaze.ui

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.base.BaseActivity
import com.android.sample.tvmaze.databinding.ActivityMainBinding
import com.android.sample.tvmaze.util.Resource
import com.android.sample.tvmaze.util.applyExitMaterialTransform
import com.android.sample.tvmaze.util.hide
import com.android.sample.tvmaze.util.show
import com.android.sample.tvmaze.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.getViewModel

class MainActivity : BaseActivity<MainViewModel>() {

    override val binding: ActivityMainBinding by binding(R.layout.activity_main)

    override val viewModel: MainViewModel
        get() = getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        applyExitMaterialTransform()
        super.onCreate(savedInstanceState)
        binding.apply {
            vm = viewModel
        }

        val viewModelAdapter = MainAdapter(this)
        binding.recyclerView.adapter = viewModelAdapter

        lifecycleScope.launch {
            viewModel.stateFlow.collect { resource ->
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
                        when {
                            resource.data.isNullOrEmpty() -> {
                                binding.loadingSpinner.hide()
                                binding.errorLayout.show()
                                binding.errorMsg.text = resource.message
                            }
                            savedInstanceState == null -> {
                                Toast.makeText(
                                    applicationContext,
                                    resource.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            else -> {
                                binding.loadingSpinner.hide()
                                binding.errorLayout.hide()
                                viewModelAdapter.submitList(resource.data)
                            }
                        }
                    }
                }
            }
        }
    }
}
