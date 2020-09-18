package com.android.sample.tvmaze.util

import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialContainerTransformSharedElementCallback

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
internal fun getContentTransform(): MaterialContainerTransform {
    return MaterialContainerTransform().apply {
        addTarget(android.R.id.content)
        duration = 450
        pathMotion = MaterialArcMotion()
    }
}

fun <T> resultLiveData(databaseQuery: () -> LiveData<T>,
                       networkCall: suspend () -> Resource<T>, contextProvider: CoroutineContextProvider
): LiveData<Resource<T>> =
    liveData(contextProvider.IO) {
        emit(Resource.loading())
        val source = databaseQuery.invoke().map { Resource.success(it) }
        emitSource(source)

        val result = networkCall.invoke()
        if (result.status == Resource.Status.ERROR) {
            emit(Resource.error(result.message!!))
            emitSource(source)
        }
    }