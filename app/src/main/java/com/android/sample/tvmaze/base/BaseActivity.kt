package com.android.sample.tvmaze.base

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * BaseActivity is an abstract class for providing [DataBindingUtil].
 */
abstract class BaseActivity : AppCompatActivity() {

    protected abstract val binding: ViewDataBinding

    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy { DataBindingUtil.setContentView<T>(this, resId) }
}