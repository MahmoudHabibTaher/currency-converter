package com.bigo.android.revolut.currency.converter.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bigo.android.revolut.currency.converter.domain.CalculateRate
import com.bigo.android.revolut.currency.converter.domain.CalculateRateParams
import com.bigo.android.revolut.currency.converter.domain.CurrencyRateParams
import com.bigo.android.revolut.currency.converter.domain.ObserveCurrencyRate
import com.bigo.android.revolut.currency.converter.domain.entities.CurrencyRate
import com.bigo.android.revolut.currency.core.presentation.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CurrencyConverterViewModel @Inject constructor(
    private val currencyRate: ObserveCurrencyRate,
    private val calculateRate: CalculateRate
) : BaseViewModel<CurrencyConverterViewState, CurrencyConverterIntent>() {

    private val initialCurrency = "EUR"

    private val initialValue = 100.0

    private var value: Double = initialValue

    private var currency = initialCurrency

    private var rate: CurrencyRate? = null

    private var state = CurrencyConverterViewState()

    private var latest: Job? = null

    override fun onIntent(intent: CurrencyConverterIntent) {
        launch {
            Log.d("HomeViewModel", "onIntent $intent")
            intent.let(::mapIntent).let { action ->
                Log.d("HomeViewModel", "onAction $action")
                when (action) {
                    is CurrencyConverterAction.GetCurrencyRate -> {
                        getCurrencyRate(action.currency)
                    }

                    is CurrencyConverterAction.CalculateRates -> {
                        ioJob {
                            value = action.value

                            calculateRate(rate, value)
                        }
                    }
                }
            }
        }
    }

    private fun mapIntent(intent: CurrencyConverterIntent) =
        when (intent) {
            is CurrencyConverterIntent.Initial -> {
                CurrencyConverterAction.GetCurrencyRate(initialCurrency)
            }
            is CurrencyConverterIntent.LoadRates -> {
                CurrencyConverterAction.GetCurrencyRate(intent.currency)
            }
            is CurrencyConverterIntent.CalculateRates -> {
                CurrencyConverterAction.CalculateRates(intent.value)
            }
        }

    private fun getCurrencyRate(
        currency: String
    ) {
        onResult(CurrencyConverterResult.GetCurrencyRateResult.Loading)
        latest?.cancel()
        latest = currencyRate.getFlow(CurrencyRateParams(currency)).mapLatest {
            rate = it
            calculateRate(rate, value)
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private fun onResult(result: CurrencyConverterResult) {
        Log.d("HomeViewModel", "onResult $result")
        state = when (result) {
            is CurrencyConverterResult.GetCurrencyRateResult.Loading ->
                state.copy(rate = state.rate.toLoading())
            is CurrencyConverterResult.GetCurrencyRateResult.Success ->
                state.copy(rate = state.rate.toSuccess(result.currencyRate))
            is CurrencyConverterResult.GetCurrencyRateResult.Error ->
                state.copy(rate = state.rate.toError(result.error))
        }

        notifyState(state)
    }

    private suspend fun calculateRate(
        rate: CurrencyRate?,
        value: Double
    ) {
        Log.d("HomeViewModel", "calculateRate $rate, $value")
        try {
            rate?.let {
                val calculatedRate = calculateRate(CalculateRateParams(value, rate))

                onResult(CurrencyConverterResult.GetCurrencyRateResult.Success(calculatedRate))
            }
        } catch (ex: Exception) {
            onResult(
                CurrencyConverterResult.GetCurrencyRateResult.Error(
                    ex.message ?: ""
                )
            )
        }
    }
}