package com.bigo.android.revolut.currency.di

import android.content.Context
import com.bigo.android.revolut.currency.converter.presentation.CurrencyConverterViewModel
import com.bigo.android.revolut.currency.core.network.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    val currencyConverterViewModel: CurrencyConverterViewModel
}