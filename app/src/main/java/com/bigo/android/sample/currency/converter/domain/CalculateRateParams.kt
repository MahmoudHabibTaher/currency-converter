package com.bigo.android.sample.currency.converter.domain

import com.bigo.android.sample.currency.converter.domain.entities.CurrencyRate
import com.bigo.android.sample.currency.core.domain.Params

data class CalculateRateParams(
    val value: Double,
    val currencyRate: CurrencyRate
) : Params