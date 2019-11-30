package com.bigo.android.revolut.currency.converter.domain

import com.bigo.android.revolut.currency.converter.domain.entities.CurrencyRate
import com.bigo.android.revolut.currency.core.domain.Params

data class CalculateRateParams(
    val value: Double,
    val currencyRate: CurrencyRate
) : Params