package com.bigo.android.revolut.currency.converter.domain

import com.bigo.android.revolut.currency.converter.domain.entities.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface CurrenciesDataSource {
    fun observeRate(currency: String): Flow<CurrencyRate>
}