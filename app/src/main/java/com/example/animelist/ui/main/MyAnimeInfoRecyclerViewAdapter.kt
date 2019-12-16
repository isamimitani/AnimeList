package com.example.animelist.ui.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.animelist.R
import com.example.animelist.entity.AnimeInfo

import com.example.animelist.ui.main.AnimeInfoFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_animeinfo.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyAnimeInfoRecyclerViewAdapter(
    private var mValues: List<AnimeInfo>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyAnimeInfoRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as AnimeInfo
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    fun setNewData(newValues : List<AnimeInfo>){
        mValues = newValues
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_animeinfo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTypeView.text = item.type
        holder.mNameView.text = item.name

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTypeView: TextView = mView.anime_type
        val mNameView: TextView = mView.anime_name

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }

}
