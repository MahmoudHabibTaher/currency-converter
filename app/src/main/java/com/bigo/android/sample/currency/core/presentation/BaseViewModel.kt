package com.bigo.android.sample.currency.core.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*

typealias IntentToActionMapper<I, A> = suspend (I) -> A

typealias ActionToResultMapper<A, R> = suspend ProducerScope<R>.(A) -> Unit

typealias ResultToStateMapper<R, V> = suspend (R) -> V

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<V : ViewState, I : Intent> : ViewModel() {

    private val states = MutableLiveData<V>()

    fun states(): LiveData<V> = states

    abstract fun processIntents(intents: Flow<I>)

    protected fun notifyState(state: V) {
        launch {
            states.value = state
        }
    }

    protected inline fun <reified A : Action> Flow<I>.intentToAction(
        noinline transform: IntentToActionMapper<I, A>
    ): Flow<A> = map { intent ->
        transform(intent)
    }.buffer().flowOn(Dispatchers.Main)

    protected inline fun <reified A : Action, reified R : Result> Flow<A>.actionToResult(
        noinline transformer: ActionToResultMapper<A, R>
    ): Flow<R> = channelFlow {
        collect {
            return@collect transformer(it)
        }
    }

    protected inline fun <reified R : Result> Flow<R>.resultToState(
        noinline transform: ResultToStateMapper<R, V>
    ): Flow<V> = map { result ->
        transform(result)
    }

    protected fun launch(
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch {
        supervisorScope {
            block()
        }
    }

    protected fun CoroutineScope.ioJob(
        block: suspend CoroutineScope.() -> Unit
    ) = async(Dispatchers.IO) {
        supervisorScope {
            block()
        }
    }
}