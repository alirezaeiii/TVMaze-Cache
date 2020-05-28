package com.android.sample.tvmaze.ui

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.base.BaseActivity
import com.android.sample.tvmaze.databinding.ActivityMainBinding
import com.android.sample.tvmaze.util.Result
import com.android.sample.tvmaze.util.applyExitMaterialTransform
import com.android.sample.tvmaze.viewmodel.MainViewModel
import org.koin.android.viewmodel.ext.android.getViewModel
import timber.log.Timber

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

        viewModel.liveData.observe(this, Observer { result ->
            if (result is Result.Error) {
                Toast.makeText(this, getString(R.string.failed_loading_msg), Toast.LENGTH_LONG).show()
                Timber.e(result.exception)
            }
        })

        binding.apply {
            vm = viewModel
            lifecycleOwner = this@MainActivity
            recyclerView.adapter = viewModelAdapter
        }
    }
}
