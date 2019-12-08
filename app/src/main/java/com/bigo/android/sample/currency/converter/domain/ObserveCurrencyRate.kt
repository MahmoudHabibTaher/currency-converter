package com.bigo.android.sample.currency.converter.domain

import com.bigo.android.sample.currency.converter.data.CurrenciesRepository
import com.bigo.android.sample.currency.converter.domain.entities.CurrencyRate
import com.bigo.android.sample.currency.core.domain.FlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrencyRate @Inject constructor(
    private val currenciesDataSource: CurrenciesRepository
) : FlowUseCase<CurrencyRate, CurrencyRateParams>() {
    override fun buildFlowUseCase(params: CurrencyRateParams?): Flow<CurrencyRate> =
        currenciesDataSource.observeRate(params?.base ?: "")

}