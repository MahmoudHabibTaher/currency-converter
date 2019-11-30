package com.bigo.android.revolut.currency.converter.domain

import com.bigo.android.revolut.currency.converter.domain.entities.CalculatedRate
import com.bigo.android.revolut.currency.converter.domain.entities.CalculatedResult
import com.bigo.android.revolut.currency.core.domain.UseCase
import javax.inject.Inject

class CalculateRate @Inject constructor() : UseCase<CalculatedResult, CalculateRateParams>() {
    override suspend fun run(params: CalculateRateParams?): CalculatedResult {
        val value = params?.value ?: 1.0
        val calculatedValues = params?.currencyRate?.rates?.map {
            CalculatedRate(
                currency = it.currency,
                value = it.rate * value
            )
        } ?: listOf()
        return CalculatedResult(calculatedValues)
    }
}