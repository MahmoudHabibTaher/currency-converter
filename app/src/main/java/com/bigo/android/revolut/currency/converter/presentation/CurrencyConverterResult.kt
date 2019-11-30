package com.bigo.android.revolut.currency.converter.presentation

import com.bigo.android.revolut.currency.converter.domain.entities.CalculatedResult
import com.bigo.android.revolut.currency.core.presentation.Result
import com.bigo.android.revolut.currency.converter.domain.entities.CurrencyRate

sealed class CurrencyConverterResult : Result {
    sealed class GetCurrencyRateResult : CurrencyConverterResult() {
        object Loading : GetCurrencyRateResult()
        data class Success(val currencyRate: CalculatedResult) : GetCurrencyRateResult()
        data class Error(val error: String) : GetCurrencyRateResult()
    }
}