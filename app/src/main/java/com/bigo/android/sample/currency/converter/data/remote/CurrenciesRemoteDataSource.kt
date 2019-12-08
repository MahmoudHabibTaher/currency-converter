package com.bigo.android.sample.currency.converter.data.remote

import android.util.Log
import com.bigo.android.sample.currency.converter.domain.CurrenciesDataSource
import com.bigo.android.sample.currency.converter.domain.entities.CurrencyRate
import com.bigo.android.sample.currency.converter.domain.entities.Rate
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrenciesRemoteDataSource @Inject constructor(
    private val api: CurrencyApi
) : CurrenciesDataSource {

    @ExperimentalCoroutinesApi
    override fun observeRate(currency: String): Flow<CurrencyRate> = channelFlow {
        var latest: Deferred<Unit>? = null
        while (true) {
            var delay = 1000L
            try {
                latest?.cancel()
                latest = async {
                    val rate = getCurrencyRate(currency)
                    send(rate)
                }

            } catch (ex: Exception) {
                Log.e("Remote", "Error loading rates", ex)
                delay = 0
            }
            delay(delay)
        }
    }

    private suspend fun getCurrencyRate(currency: String) =
        api.getRates(currency).let {
            CurrencyRate(
                base = it.base,
                date = it.date,
                rates = it.rates.map { (currency, rate) -> Rate(currency, rate) }
            )
        }

}