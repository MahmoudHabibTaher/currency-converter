package com.bigo.android.sample.currency.core.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flowOn

abstract class ActionDispatcher<A : Action, R : Result> {
    abstract fun processActions(actions: Flow<A>): Flow<R>
}

inline fun <reified A : Action, reified R : Result> Flow<A>.mapActions(
    actionDispatcher: ActionDispatcher<A, R>
) = let { actionDispatcher.processActions(it).buffer().flowOn(Dispatchers.IO) }