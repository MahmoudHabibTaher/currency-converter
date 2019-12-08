package com.bigo.android.sample.currency.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
abstract class BaseFragment<I : Intent, V : ViewState> : Fragment() {
    protected abstract val viewModel: BaseViewModel<V, I>

    protected abstract val layoutResId: Int?

    private val intentsChannel: Channel<I> = Channel()

    private val intentsFlow = channelFlow {

        val iterator = intentsChannel.iterator()

        while (iterator.hasNext()) {
            send(iterator.next())
        }

        awaitClose()
    }

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

        viewModel.processIntents(intentsFlow)
    }

    override fun onDestroy() {
        intentsChannel.close()

        super.onDestroy()
    }

    protected fun dispatchIntent(intent: I) {
        lifecycleScope.launch {
            intentsChannel.send(intent)
        }
    }
}