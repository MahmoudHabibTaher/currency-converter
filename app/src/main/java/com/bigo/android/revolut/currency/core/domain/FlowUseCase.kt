package com.bigo.android.revolut.currency.core.domain

import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<T, P : Params> {

    protected abstract fun buildFlowUseCase(params: P?): Flow<T>

    fun getFlow(params: P? = null): Flow<T> = buildFlowUseCase(params)
}