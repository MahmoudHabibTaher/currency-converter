package com.bigo.android.sample.currency.converter.domain

import com.bigo.android.sample.currency.converter.domain.entities.CalculatedRate
import com.bigo.android.sample.currency.converter.domain.entities.CalculatedResult
import com.bigo.android.sample.currency.core.domain.UseCase
import javax.inject.Inject

class CalculateRate @Inject constructor() : UseCase<CalculatedResult, CalculateRateParams>() {
    override suspend fun run(params: CalculateRateParams?): CalculatedResult {
        val value = params?.value ?: 1.0

        val values = params?.currencyRate?.let {
            val base = CalculatedRate(it.base, value)

            val calculatedValues = params.currencyRate.rates.map {
                CalculatedRate(
                    currency = it.currency,
                    value = it.rate * value
                )
            }

            listOf(base) + calculatedValues
        } ?: listOf()

        return CalculatedResult(values)
    }
}