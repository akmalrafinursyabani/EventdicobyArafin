package com.example.eventdicobyarafin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventdicobyarafin.data.api.responses.ListEventsItem
import com.example.eventdicobyarafin.databinding.RowEventHorizontalBinding

class CarouselAdapter(
    private val onItemClick: (ListEventsItem) -> Unit
) : ListAdapter<ListEventsItem, CarouselAdapter.CarouselViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = RowEventHorizontalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val event = getItem(position)
        if (event != null) {
            holder.bind(event, onItemClick)
        }
    }

    class CarouselViewHolder(
        private val binding: RowEventHorizontalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ListEventsItem, onItemClick: (ListEventsItem) -> Unit) {
            binding.tvCarouselTitle.text = event.name

            Glide.with(itemView.context)
                .load(event.mediaCover ?: event.imageLogo)
                .into(binding.imgMediaCover)

            itemView.setOnClickListener {
                onItemClick(event)
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