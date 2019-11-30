package com.bigo.android.revolut.currency

import android.app.Application
import com.bigo.android.revolut.currency.di.AppComponent
import com.bigo.android.revolut.currency.di.DaggerAppComponent
import com.bigo.android.revolut.currency.di.DaggerComponentProvider

class CurrencyConverterApp : Application(), DaggerComponentProvider {
    override val component: AppComponent by lazy {
        DaggerAppComponent.factory()
            .create(applicationContext)
    }
}