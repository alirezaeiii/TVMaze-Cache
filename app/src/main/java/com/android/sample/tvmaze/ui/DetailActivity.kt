package com.android.sample.tvmaze.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.base.BaseBindingActivity
import com.android.sample.tvmaze.databinding.ActivityDetailBinding
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.util.applyMaterialTransform

class DetailActivity : BaseBindingActivity() {

    override val binding: ActivityDetailBinding by binding(R.layout.activity_detail)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val showItem = intent.extras?.getParcelable<Show>(SHOW_KEY)
        applyMaterialTransform(showItem?.name!!)
        binding.apply {
            show = showItem
            activity = this@DetailActivity
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val SHOW_KEY = "showKey"
        fun startActivityModel(context: Context?, startView: View, show: Show) {
            if (context is Activity) {
                val intent = Intent(context, DetailActivity::class.java)
                    .putExtra(SHOW_KEY, show)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    context,
                    startView, show.name
                )
                context.startActivity(intent, options.toBundle())
            }
        }
    }
}