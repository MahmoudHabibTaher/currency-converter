package com.bigo.android.revolut.currency.converter.domain

import android.util.Log
import com.bigo.android.revolut.currency.converter.data.CurrenciesRepository
import com.bigo.android.revolut.currency.core.domain.FlowUseCase
import com.bigo.android.revolut.currency.converter.domain.entities.CurrencyRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class ObserveCurrencyRate @Inject constructor(
    private val currenciesDataSource: CurrenciesRepository
) : FlowUseCase<CurrencyRate, CurrencyRateParams>() {
    override fun buildFlowUseCase(params: CurrencyRateParams?): Flow<CurrencyRate> =
        currenciesDataSource.observeRate(params?.base ?: "")
            .catch {
                Log.e("UseCase", "Error getting rates", it)
            }

}