package com.kai.kaitwse.common

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnAttach
import androidx.core.view.updatePadding

fun View.applyStatusBarPadding() {
    val initialTopPadding = paddingTop

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val statusBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
        view.updatePadding(top = initialTopPadding + statusBarInsets.top)
        windowInsets
    }

    requestInsetsWhenAttached()
}

fun View.applyNavigationBarPadding() {
    val initialBottomPadding = paddingBottom

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val navigationBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
        view.updatePadding(bottom = initialBottomPadding + navigationBarInsets.bottom)
        windowInsets
    }

    requestInsetsWhenAttached()
}

private fun View.requestInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        doOnAttach { it.requestApplyInsets() }
    }
}
