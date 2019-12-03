package com.bigo.android.revolut.currency.converter.presentation

import com.bigo.android.revolut.currency.core.presentation.Intent

sealed class CurrencyConverterIntent : Intent {
    object Initial : CurrencyConverterIntent()
    data class LoadRates(
        val currency: String
    ) : CurrencyConverterIntent()

    data class CalculateRates(
        val value: Double
    ) : CurrencyConverterIntent()
}