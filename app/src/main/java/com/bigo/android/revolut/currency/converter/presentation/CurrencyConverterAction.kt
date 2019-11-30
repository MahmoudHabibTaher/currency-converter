package com.bigo.android.revolut.currency.converter.presentation

import com.bigo.android.revolut.currency.core.presentation.Action

sealed class CurrencyConverterAction : Action {
    data class GetCurrencyRate(
        val currency: String
    ) : CurrencyConverterAction()

    data class CalculateRates(
        val value: Double
    ) : CurrencyConverterAction()
}