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
import com.example.submisiakhir.data.local.entity.FavoriteEntity
import com.example.submisiakhir.databinding.CardEventBinding
import com.example.submisiakhir.ui.utils.adapter.BookmarkAdapter.EventViewHolder

class BookmarkAdapter: ListAdapter<FavoriteEntity, EventViewHolder>(DIFF_CALLBACK) {
    private lateinit var onEventClickCallback: OnItemClickBack

    interface OnItemClickBack {
        fun onItemClicked(event: FavoriteEntity)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickBack) {
        this.onEventClickCallback = onItemClickCallback
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CardEventBinding.bind(itemView)

        fun bind(event: FavoriteEntity) {
            binding.tvTitle.text = event.name
            val description = HtmlCompat.fromHtml(event.summary, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.tvDescription.text = description
            Glide.with(itemView.context)
                .load(event.logo)
                .into(binding.imgPoster)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.card_event, parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

        holder.itemView.setOnClickListener {
            onEventClickCallback.onItemClicked(getItem(holder.adapterPosition))
        }
    }
}