package com.bigo.android.sample.currency.converter.presentation

import com.bigo.android.sample.currency.converter.domain.CalculateRate
import com.bigo.android.sample.currency.converter.domain.CalculateRateParams
import com.bigo.android.sample.currency.converter.domain.CurrencyRateParams
import com.bigo.android.sample.currency.converter.domain.ObserveCurrencyRate
import com.bigo.android.sample.currency.converter.domain.entities.CurrencyRate
import com.bigo.android.sample.currency.core.presentation.ActionDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CurrencyConverterActionDispatcher @Inject constructor(
    private val currencyRate: ObserveCurrencyRate,
    private val calculateRate: CalculateRate
) :
    ActionDispatcher<CurrencyConverterAction,
            CurrencyConverterResult>() {
    private var latest: Job? = null

    private var value: Double = 100.0

    private var rate: CurrencyRate? = null

    override fun processActions(actions: Flow<CurrencyConverterAction>): Flow<CurrencyConverterResult> =
        channelFlow {
            actions.collect { action ->
                when (action) {
                    is CurrencyConverterAction.GetCurrencyRate -> {
                        getCurrencyRate(action.currency)
                    }

                    is CurrencyConverterAction.CalculateRates -> {
                        value = action.value

                        calculateRate(rate, value)
                    }
                }
            }
        }

    private suspend fun ProducerScope<CurrencyConverterResult>.getCurrencyRate(currency: String) {
        send(CurrencyConverterResult.GetCurrencyRateResult.Loading)
        latest?.cancel()
        latest = currencyRate.getFlow(CurrencyRateParams(currency)).onEach {
            rate = it
            calculateRate(rate, value)
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    private suspend fun ProducerScope<CurrencyConverterResult>.calculateRate(
        rate: CurrencyRate?,
        value: Double
    ) {
        try {
            rate?.let {
                val calculatedRate = calculateRate(CalculateRateParams(value, rate))

                send(CurrencyConverterResult.GetCurrencyRateResult.Success(calculatedRate))
            }
        } catch (ex: Exception) {
            send(
                CurrencyConverterResult.GetCurrencyRateResult.Error(
                    ex.message ?: ""
                )
            )
        }
    }
}