package com.bigo.android.sample.currency.core.data.remote.error

interface ErrorHandler {
    fun handle(throwable: Throwable): Throwable
}