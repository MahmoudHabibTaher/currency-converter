package com.bigo.android.revolut.currency.core.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector

abstract class BaseViewModel<V : ViewState, I : Intent> : ViewModel() {

    private val states = MutableLiveData<V>()

    fun states(): LiveData<V> = states

    abstract fun onIntent(intent: I)

    protected fun notifyState(state: V) {
        launch {
            states.value = state
        }
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