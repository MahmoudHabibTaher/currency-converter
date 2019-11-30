package com.bigo.android.revolut.currency.converter.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bigo.android.revolut.currency.converter.domain.CalculateRate
import com.bigo.android.revolut.currency.converter.domain.CalculateRateParams
import com.bigo.android.revolut.currency.converter.domain.CurrencyRateParams
import com.bigo.android.revolut.currency.converter.domain.ObserveCurrencyRate
import com.bigo.android.revolut.currency.converter.domain.entities.CurrencyRate
import com.bigo.android.revolut.currency.core.presentation.ActionToResultMapper
import com.bigo.android.revolut.currency.core.presentation.BaseViewModel
import com.bigo.android.revolut.currency.core.presentation.IntentToActionMapper
import com.bigo.android.revolut.currency.core.presentation.ResultToStateMapper
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class CurrencyConverterViewModel @Inject constructor(
    private val currencyRate: ObserveCurrencyRate,
    private val calculateRate: CalculateRate
) : BaseViewModel<CurrencyConverterViewState, CurrencyConverterIntent>() {

    private var value: Double = 100.0

    private var currency = "EUR"

    private var rate: CurrencyRate? = null

    private var state = CurrencyConverterViewState()

    private var latest: Job? = null

    private val intentsMapper: IntentToActionMapper<CurrencyConverterIntent,
            CurrencyConverterAction> = { intent ->
        when (intent) {
            is CurrencyConverterIntent.LoadRates -> {
                CurrencyConverterAction.GetCurrencyRate(intent.currency)
            }
            is CurrencyConverterIntent.CalculateRates -> {
                CurrencyConverterAction.CalculateRates(intent.value)
            }
        }
    }

    private val actionsMapper: suspend ProducerScope<CurrencyConverterResult>.(CurrencyConverterAction) -> Unit =
        { action ->
            when (action) {
                is CurrencyConverterAction.GetCurrencyRate -> {
                    getCurrencyRate(currency)
                }

                is CurrencyConverterAction.CalculateRates -> {
                    value = action.value

                    calculateRate(rate, value)
                }
            }
        }

    private val resultMapper: ResultToStateMapper<CurrencyConverterResult,
            CurrencyConverterViewState> = { result ->
        when (result) {
            is CurrencyConverterResult.GetCurrencyRateResult.Loading ->
                state.copy(rate = state.rate.toLoading())
            is CurrencyConverterResult.GetCurrencyRateResult.Success ->
                state.copy(rate = state.rate.toSuccess(result.currencyRate))
            is CurrencyConverterResult.GetCurrencyRateResult.Error ->
                state.copy(rate = state.rate.toError(result.error))
        }
    }

    override fun processIntents(intents: Flow<CurrencyConverterIntent>) {
        intents.logEvents()
            .intentToAction(intentsMapper)
            .logEvents()
            .actionToResultWithChannel(actionsMapper)
            .logEvents()
            .resultToState(resultMapper)
            .logEvents()
            .onEach {
                state = it
                notifyState(it)
            }.catch {
                Log.e("HomeViewModel", "Error in intents stream", it)
            }.launchIn(viewModelScope)
    }

    private fun <T> Flow<T>.logEvents() = onEach {
        Log.d("HomeViewModel", "onEvent $it")
    }

    private suspend fun ProducerScope<CurrencyConverterResult>.getCurrencyRate(
        currency: String
    ) {
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