package com.android.sample.tvmaze.base

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * BaseCompatActivity is an abstract class for providing [DataBindingUtil].
 * provides implementations of only [ViewDataBinding] from an abstract information.
 * Do not modify this class. This is a first-level abstraction class.
 * If you want to add more specifications, make another class which extends [BaseBindingActivity].
 */
abstract class BaseBindingActivity : AppCompatActivity() {

    protected abstract val binding: ViewDataBinding

    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy { DataBindingUtil.setContentView<T>(this, resId) }
}