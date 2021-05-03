package com.android.sample.tvmaze.base

import androidx.lifecycle.ViewModel

abstract class BaseActivity<VM : ViewModel> : BaseCompatActivity() {

    protected abstract val viewModel: VM
}