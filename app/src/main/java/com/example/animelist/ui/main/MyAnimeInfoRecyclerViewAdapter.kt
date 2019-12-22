package com.example.animelist.ui.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.animelist.databinding.ListItemAnimeinfoBinding
import com.example.animelist.entity.AnimeInfo

class MyAnimeInfoRecyclerViewAdapter(
    private var mValues: List<AnimeInfo>,
    private val mListener: AnimeInfoListener?
) : RecyclerView.Adapter<MyAnimeInfoRecyclerViewAdapter.ViewHolder>() {

    fun setNewData(newValues: List<AnimeInfo>) {
        mValues = newValues
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.bind(item, mListener)
    }

    override fun getItemCount(): Int = mValues.size

    class ViewHolder(val binding: ListItemAnimeinfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnimeInfo, clickListener: AnimeInfoListener?) {
            binding.animeInfo = item
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAnimeinfoBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class AnimeInfoListener(val clickListener: (sleepId: String?) -> Unit) {
    fun onClick(animeInfo: AnimeInfo) = clickListener(animeInfo.id)
}
