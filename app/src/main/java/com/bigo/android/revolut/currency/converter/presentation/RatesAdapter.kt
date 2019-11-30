package com.bigo.android.revolut.currency.converter.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bigo.android.revolut.currency.R
import com.bigo.android.revolut.currency.converter.domain.entities.CalculatedRate
import kotlinx.android.synthetic.main.layout_rates_list_item.view.*
import java.text.DecimalFormat

class RatesAdapter : ListAdapter<CalculatedRate, RatesAdapter.RateViewHolder>(RateItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder =
        RateViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_rates_list_item,
                parent, false
            )
        )

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(rate: CalculatedRate) {
            itemView.currency_text_view.text = rate.currency
            val decimalFormat = DecimalFormat("#.###")
            itemView.value_text_view.text = decimalFormat.format(rate.value)
        }
    }

    class RateItemCallback : DiffUtil.ItemCallback<CalculatedRate>() {
        override fun areItemsTheSame(oldItem: CalculatedRate, newItem: CalculatedRate): Boolean =
            oldItem.currency == newItem.currency

        override fun areContentsTheSame(oldItem: CalculatedRate, newItem: CalculatedRate): Boolean =
            oldItem == newItem

    }
}