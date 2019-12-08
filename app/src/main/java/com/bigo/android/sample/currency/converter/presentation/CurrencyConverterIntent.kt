package com.bigo.android.sample.currency.converter.presentation

import com.bigo.android.sample.currency.core.presentation.Intent

sealed class CurrencyConverterIntent : Intent {
    object Initial : CurrencyConverterIntent()
    data class LoadRates(
        val currency: String
    ) : CurrencyConverterIntent()

    data class CalculateRates(
        val value: Double
    ) : CurrencyConverterIntent()
}