package com.bigo.android.sample.currency.converter.data

import com.bigo.android.sample.currency.converter.data.remote.CurrenciesRemoteDataSource
import com.bigo.android.sample.currency.converter.domain.CurrenciesDataSource
import com.bigo.android.sample.currency.converter.domain.entities.CurrencyRate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrenciesRepository @Inject constructor(
    private val remoteDataSource: CurrenciesRemoteDataSource
) : CurrenciesDataSource {
    override fun observeRate(currency: String): Flow<CurrencyRate> =
        remoteDataSource.observeRate(currency)
}