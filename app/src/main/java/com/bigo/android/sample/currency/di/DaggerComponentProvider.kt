package com.bigo.android.sample.currency.di

import android.app.Activity
import androidx.fragment.app.Fragment

interface DaggerComponentProvider {
    val component: AppComponent
}

val Activity.component get() = (application as DaggerComponentProvider).component

val Fragment.component get() = activity!!.component