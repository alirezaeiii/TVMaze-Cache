package com.android.sample.tvmaze.util

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * Binding adapter used to hide the spinner once data is available
 */
@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

@BindingAdapter("loadImage")
fun bindLoadImage(view: AppCompatImageView, url: String) {
    Glide.with(view.context).load(url).into(view)
}

@Suppress("DEPRECATION")
@BindingAdapter("showHTMLText")
fun TextView.bindText(url: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(url, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(url)
    }
}