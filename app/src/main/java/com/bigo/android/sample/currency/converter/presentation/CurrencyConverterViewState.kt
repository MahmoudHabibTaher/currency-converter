package com.bigo.android.sample.currency.converter.presentation

import com.bigo.android.sample.currency.converter.domain.entities.CalculatedResult
import com.bigo.android.sample.currency.core.presentation.ViewState

data class CurrencyConverterViewState(
    val currency: String = "EUR",
    val value: Double = 100.0,
    val rate: CurrencyRateState = CurrencyRateState()
) : ViewState {
    data class CurrencyRateState(
        val loading: Boolean = false,
        val rate: CalculatedResult? = null,
        val error: String? = null
    ) {
        fun toLoading() = copy(loading = true, error = null)

        fun toSuccess(rate: CalculatedResult) = copy(loading = false, rate = rate, error = null)

        fun toError(error: String) = copy(loading = false, error = error)
    }
}