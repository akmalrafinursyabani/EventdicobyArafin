package com.example.eventdicobyarafin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventdicobyarafin.data.api.responses.ListEventsItem
import com.example.eventdicobyarafin.databinding.RowEventVerticalBinding

class EventAdapter(private val onClick: (ListEventsItem) -> Unit) :
    ListAdapter<ListEventsItem, EventAdapter.EventViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val binding =
            RowEventVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }


    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        if (event != null) {
            holder.bind(event, onClick)
        }
    }

    class EventViewHolder(private val binding: RowEventVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onClick: (ListEventsItem) -> Unit) {
            binding.tvEventName.text = event.name
            binding.tvEventSummary.text = event.summary

            Glide.with(itemView.context).load(event.imageLogo ?: event.mediaCover)
                .into(binding.imgEventLogo)

            itemView.setOnClickListener {
                onClick(event)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}