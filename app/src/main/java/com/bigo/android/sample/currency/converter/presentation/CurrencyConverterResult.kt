package com.bigo.android.sample.currency.converter.presentation

import com.bigo.android.sample.currency.converter.domain.entities.CalculatedResult
import com.bigo.android.sample.currency.core.presentation.Result

sealed class CurrencyConverterResult : Result {
    sealed class GetCurrencyRateResult : CurrencyConverterResult() {
        object Loading : GetCurrencyRateResult()
        data class Success(val currencyRate: CalculatedResult) : GetCurrencyRateResult()
        data class Error(val error: String) : GetCurrencyRateResult()
    }
}