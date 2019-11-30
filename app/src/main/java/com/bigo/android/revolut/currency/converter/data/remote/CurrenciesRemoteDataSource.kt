package com.bigo.android.revolut.currency.converter.data.remote

import android.util.Log
import com.bigo.android.revolut.currency.converter.domain.CurrenciesDataSource
import com.bigo.android.revolut.currency.converter.domain.entities.CurrencyRate
import com.bigo.android.revolut.currency.converter.domain.entities.Rate
import com.bigo.android.revolut.currency.core.errors.ConnectivityException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrenciesRemoteDataSource @Inject constructor(
    private val api: CurrencyApi
) : CurrenciesDataSource {
    override fun observeRate(currency: String): Flow<CurrencyRate> = flow {
        while (true) {
//            try {
            val rate = api.getRates(currency).let {
                CurrencyRate(
                    base = it.base,
                    date = it.date,
                    rates = it.rates.map { (currency, rate) -> Rate(currency, rate) }
                )
            }

            emit(rate)
//            } catch (ex: Exception) {
//                Log.e("Remote", "Error loading rates", ex)
//                throw ex
//        }
            delay(1000)
        }
    }
}