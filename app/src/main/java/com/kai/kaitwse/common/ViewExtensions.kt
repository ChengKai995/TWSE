package com.kai.kaitwse.common

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnAttach
import androidx.core.view.updatePadding

fun View.applyStatusBarPadding() {
    val initialLeftPadding = paddingLeft
    val initialRightPadding = paddingRight
    val initialTopPadding = paddingTop

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val topInsets = windowInsets.getInsets(
            WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.displayCutout(),
        )
        view.updatePadding(
            left = initialLeftPadding + topInsets.left,
            top = initialTopPadding + topInsets.top,
            right = initialRightPadding + topInsets.right,
        )
        windowInsets
    }

    requestInsetsWhenAttached()
}

fun View.applyNavigationBarPadding() {
    val initialLeftPadding = paddingLeft
    val initialRightPadding = paddingRight
    val initialBottomPadding = paddingBottom

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val navigationBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
        view.updatePadding(
            left = initialLeftPadding + navigationBarInsets.left,
            right = initialRightPadding + navigationBarInsets.right,
            bottom = initialBottomPadding + navigationBarInsets.bottom,
        )
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
