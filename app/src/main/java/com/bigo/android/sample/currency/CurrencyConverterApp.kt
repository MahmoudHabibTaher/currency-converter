package com.bigo.android.sample.currency

import android.app.Application
import com.bigo.android.sample.currency.di.AppComponent
import com.bigo.android.sample.currency.di.DaggerAppComponent
import com.bigo.android.sample.currency.di.DaggerComponentProvider

class CurrencyConverterApp : Application(), DaggerComponentProvider {
    override val component: AppComponent by lazy {
        DaggerAppComponent.factory()
            .create(applicationContext)
    }
}