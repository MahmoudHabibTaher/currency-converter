package com.bigo.android.sample.currency.converter.presentation

import com.bigo.android.sample.currency.core.presentation.Action

sealed class CurrencyConverterAction : Action {
    data class GetCurrencyRate(
        val currency: String
    ) : CurrencyConverterAction()

    data class CalculateRates(
        val value: Double
    ) : CurrencyConverterAction()
}