package com.example.submisiakhir.ui.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submisiakhir.R
import com.example.submisiakhir.data.remote.response.ListEventsItem
import com.example.submisiakhir.databinding.ListEventBinding
import com.example.submisiakhir.ui.utils.adapter.ListEventAdapter.EventViewHolder

class ListEventAdapter : ListAdapter<ListEventsItem, EventViewHolder>(DIFF_CALLBACK) {

    private lateinit var onEventClickCallback: OnItemClickback

    interface OnItemClickback {
        fun onItemClicked(data: ListEventsItem)
    }

    fun setOnClickCallback(onItemClickback: OnItemClickback) {
        this.onEventClickCallback = onItemClickback
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
        private val binding = ListEventBinding.bind(itemView)

        fun bind(event: ListEventsItem) {
            binding.tvTitle.text = event.name
            val desscription = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.tvDescription.text = desscription
            Glide.with(itemView.context)
                .load(event.imageLogo)
                .into(binding.imgPoster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.list_event, parent, false)
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