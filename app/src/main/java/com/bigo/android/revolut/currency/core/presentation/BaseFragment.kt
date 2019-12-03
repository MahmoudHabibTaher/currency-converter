package com.bigo.android.revolut.currency.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow

abstract class BaseFragment<I : Intent, V : ViewState> : Fragment() {
    protected abstract val viewModel: BaseViewModel<V, I>

    protected abstract val layoutResId: Int?

    abstract fun render(state: V)

    abstract fun initView(view: View)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = layoutResId?.let { inflater.inflate(it, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        viewModel.states().observe(this, Observer {
            render(it)
        })
    }

    protected fun dispatchIntent(intent: I) {
        viewModel.onIntent(intent)
    }
}