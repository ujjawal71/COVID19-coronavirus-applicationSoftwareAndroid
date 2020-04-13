package com.csecoder.covid19.UjjuBoard.cseboard

import android.view.View
import com.csecoder.covid19.UjjuBoard.SafeClickListener

fun View.gone() = this.apply { visibility = View.GONE }

fun View.visible() = this.apply { visibility = View.VISIBLE }

fun View.invisible() = this.apply { visibility = View.INVISIBLE }

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}