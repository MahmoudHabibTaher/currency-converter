package com.bigo.android.revolut.currency.core.presentation

import android.view.View
import kotlinx.coroutines.flow.channelFlow

fun View.clicksFlow() = channelFlow<View> {
    setOnClickListener {
        offer(it)
    }
}