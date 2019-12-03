package com.bigo.android.revolut.currency.converter.presentation

import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigo.android.revolut.currency.R
import com.bigo.android.revolut.currency.core.presentation.BaseFragment
import com.bigo.android.revolut.currency.core.presentation.BaseViewModel
import com.bigo.android.revolut.currency.di.component
import com.bigo.android.revolut.currency.di.fragmentViewModel
import kotlinx.android.synthetic.main.fragment_currency_converter.*

class CurrencyConverterFragment :
    BaseFragment<CurrencyConverterIntent, CurrencyConverterViewState>() {

    private val ratesAdapter by lazy {
        RatesAdapter({
            dispatchIntent(CurrencyConverterIntent.CalculateRates(it))
        }, {
            dispatchIntent(CurrencyConverterIntent.LoadRates(it))
        })
    }

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