package com.bigo.android.sample.currency.core.domain

abstract class UseCase<T, P : Params> {

    protected abstract suspend fun run(params: P?): T

    suspend operator fun invoke(params: P? = null) = run(params)
}