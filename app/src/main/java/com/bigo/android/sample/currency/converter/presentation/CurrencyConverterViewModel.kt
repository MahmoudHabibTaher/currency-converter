package com.bigo.android.sample.currency.converter.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bigo.android.sample.currency.converter.domain.CalculateRate
import com.bigo.android.sample.currency.converter.domain.CalculateRateParams
import com.bigo.android.sample.currency.converter.domain.CurrencyRateParams
import com.bigo.android.sample.currency.converter.domain.ObserveCurrencyRate
import com.bigo.android.sample.currency.converter.domain.entities.CurrencyRate
import com.bigo.android.sample.currency.core.presentation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class CurrencyConverterViewModel @Inject constructor(
    private val actionDispatcher: CurrencyConverterActionDispatcher
) : BaseViewModel<CurrencyConverterViewState, CurrencyConverterIntent>() {

    private val initialCurrency = "EUR"

    private var state = CurrencyConverterViewState()

    private val intentsMapper: IntentToActionMapper<CurrencyConverterIntent,
            CurrencyConverterAction> = { intent ->
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
        intents
            .intentToAction(intentsMapper)
            .mapActions(actionDispatcher)
            .resultToState(resultMapper)
            .onEach(::onState)
            .catch {
                Log.e("HomeViewModel", "Error in intents stream", it)
            }.launchIn(viewModelScope)
    }

    private suspend fun onState(state: CurrencyConverterViewState) {
        this.state = state
        notifyState(state)
    }

    private fun <T> Flow<T>.logEvents() = onEach {
        Log.d("HomeViewModel", "onEvent $it")
    }
}