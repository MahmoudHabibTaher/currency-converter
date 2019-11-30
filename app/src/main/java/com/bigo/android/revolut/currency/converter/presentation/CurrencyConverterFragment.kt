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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ProducerScope

@ExperimentalCoroutinesApi
class CurrencyConverterFragment :
    BaseFragment<CurrencyConverterIntent, CurrencyConverterViewState>() {

    private val ratesAdapter by lazy {
        RatesAdapter()
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
    }

    override suspend fun ProducerScope<CurrencyConverterIntent>.intentsProducer() {
        offer(CurrencyConverterIntent.LoadRates("EUR"))

        value_edit_text.addTextChangedListener {
            if (!it.isNullOrEmpty()) {
                offer(CurrencyConverterIntent.CalculateRates(it.toString().toDouble()))
            }
        }
    }

    override fun render(state: CurrencyConverterViewState) {
        if (state.rate.loading) {
            Toast.makeText(context, "Loading...", Toast.LENGTH_LONG).show()
        }

        state.rate.rate?.let {
            ratesAdapter.submitList(it.values)
            ratesAdapter.notifyDataSetChanged()
        }
    }
}