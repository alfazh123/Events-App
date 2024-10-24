package com.example.submisiakhir.ui.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submisiakhir.data.remote.response.ListEventsItem
import com.example.submisiakhir.databinding.CarouselEventBinding
import com.example.submisiakhir.ui.utils.adapter.CarouselAdapter.EventViewHolder
import com.example.submisiakhir.R

class CarouselAdapter : ListAdapter<ListEventsItem, EventViewHolder>(DIFF_CALLBACK) {

    private lateinit var onEventClickCallback: OnItemClickback

    interface OnItemClickback {
        fun onItemClicked(event: ListEventsItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickback) {
        this.onEventClickCallback = onItemClickCallback
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CarouselEventBinding.bind(itemView)

        fun bind(event: ListEventsItem) {
            binding.tvTitle.text = event.name
            Glide.with(itemView.context)
                .load(event.imageLogo)
                .into(binding.imgPoster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.carousel_event, parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

        holder.itemView.setOnClickListener {
            onEventClickCallback.onItemClicked(event)
        }
    }


}