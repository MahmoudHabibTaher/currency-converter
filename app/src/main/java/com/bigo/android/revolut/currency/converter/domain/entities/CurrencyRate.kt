package com.bigo.android.revolut.currency.converter.domain.entities

data class CurrencyRate(
    val base: String,
    val date: String,
    val rates: List<Rate>
)