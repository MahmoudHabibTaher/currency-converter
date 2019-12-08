package com.bigo.android.sample.currency.converter.domain

import com.bigo.android.sample.currency.converter.domain.entities.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface CurrenciesDataSource {
    fun observeRate(currency: String): Flow<CurrencyRate>
}