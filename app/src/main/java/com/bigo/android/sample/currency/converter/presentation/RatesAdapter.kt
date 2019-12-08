package com.bigo.android.sample.currency.converter.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bigo.android.sample.currency.R
import com.bigo.android.sample.currency.converter.domain.entities.CalculatedRate
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.layout_rates_list_item.view.*
import java.text.DecimalFormat

class RatesAdapter(
    private val onValueChanged: (Double) -> Unit,
    private val onCurrencySelected: (String) -> Unit
) : ListAdapter<CalculatedRate, RatesAdapter.RateViewHolder>(RateItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder =
        RateViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_rates_list_item,
                parent, false
            )
        )

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bind(getItem(position), onValueChanged, position == 0, onCurrencySelected)
    }

    class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var disposable: Disposable? = null
        fun bind(
            rate: CalculatedRate,
            onValueChanged: (Double) -> Unit,
            canChangeValue: Boolean = false,
            onCurrencySelected: (String) -> Unit
        ) {
            itemView.currency_text_view.text = rate.currency
            disposable?.dispose()

            val decimalFormat = DecimalFormat("#.###")
            itemView.value_text_view.setText(decimalFormat.format(rate.value))

            if (canChangeValue) {
                disposable = itemView.value_text_view.textChanges()
                    .skipInitialValue()
                    .filter { it.isNotEmpty() }
                    .map { it.toString() }
                    .map { it.toDoubleOrNull() ?: 1.0 }
                    .distinct()
                    .subscribe(onValueChanged)
            }

            itemView.setOnClickListener {
                onCurrencySelected(rate.currency)
            }
        }
    }

    class RateItemCallback : DiffUtil.ItemCallback<CalculatedRate>() {
        override fun getChangePayload(oldItem: CalculatedRate, newItem: CalculatedRate): Any? =
            newItem.value

        override fun areItemsTheSame(oldItem: CalculatedRate, newItem: CalculatedRate): Boolean =
            oldItem.currency == newItem.currency

        override fun areContentsTheSame(oldItem: CalculatedRate, newItem: CalculatedRate): Boolean =
            oldItem == newItem

    }
}