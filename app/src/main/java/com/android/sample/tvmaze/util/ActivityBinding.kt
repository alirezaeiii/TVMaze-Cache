package com.android.sample.tvmaze.util

import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.android.sample.tvmaze.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialContainerTransformSharedElementCallback

fun AppCompatActivity.simpleToolbarWithHome(toolbar: MaterialToolbar, title_: String = "") {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        setDisplayHomeAsUpEnabled(true)
        title = title_
    }
}

/** apply material entered container transformation. */
fun AppCompatActivity.applyMaterialTransform(transitionName: String) {
    window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
    ViewCompat.setTransitionName(findViewById<View>(android.R.id.content), transitionName)

    // set up shared element transition
    setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
    window.sharedElementEnterTransition = getContentTransform()
    window.sharedElementReturnTransition = getContentTransform()
}

/** apply material exit container transformation. */
fun AppCompatActivity.applyExitMaterialTransform() {
    window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
    setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
    window.sharedElementsUseOverlay = false
}

/** get a material container arc transform. */
fun getContentTransform(): MaterialContainerTransform {
    return MaterialContainerTransform().apply {
        addTarget(android.R.id.content)
        duration = 450
        pathMotion = MaterialArcMotion()
    }
}