package com.bigo.android.sample.currency.converter.presentation

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigo.android.sample.currency.R
import com.bigo.android.sample.currency.core.presentation.BaseFragment
import com.bigo.android.sample.currency.core.presentation.BaseViewModel
import com.bigo.android.sample.currency.di.component
import com.bigo.android.sample.currency.di.fragmentViewModel
import kotlinx.android.synthetic.main.fragment_currency_converter.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
class CurrencyConverterFragment :
    BaseFragment<CurrencyConverterIntent, CurrencyConverterViewState>() {

    private val ratesAdapter by lazy {
        RatesAdapter(onValueChanged = { value ->
            dispatchIntent(CurrencyConverterIntent.CalculateRates(value))
        }, onCurrencySelected = { currency ->
            dispatchIntent(CurrencyConverterIntent.LoadRates(currency))
        })
    }

    @FlowPreview
    override val viewModel: BaseViewModel<CurrencyConverterViewState, CurrencyConverterIntent>
            by fragmentViewModel(this) {
                component.currencyConverterViewModel
            }

    override val layoutResId: Int? =
        R.layout.fragment_currency_converter

    override fun initView(view: View) {
        rates_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ratesAdapter
        }

        dispatchIntent(CurrencyConverterIntent.Initial)
    }

    override fun render(state: CurrencyConverterViewState) {
        if (state.rate.loading) {
            Toast.makeText(context, "Loading...", Toast.LENGTH_LONG).show()
        }

        state.rate.rate?.let {
            ratesAdapter.submitList(it.values)
        }
    }
}