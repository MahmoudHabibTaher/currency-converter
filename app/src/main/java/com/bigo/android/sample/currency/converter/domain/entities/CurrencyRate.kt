package com.bigo.android.sample.currency.converter.domain.entities

data class CurrencyRate(
    val base: String,
    val date: String,
    val rates: List<Rate>
)