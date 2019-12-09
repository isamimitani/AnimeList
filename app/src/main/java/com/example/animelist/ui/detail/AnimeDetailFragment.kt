package com.example.animelist.ui.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.animelist.R
import com.example.animelist.ui.AnimeViewModel

class AnimeDetailFragment : Fragment() {

    val TAG = "AnimeDetailFragment"

    companion object {
        val TAG = AnimeDetailFragment::class.simpleName
        fun newInstance() = AnimeDetailFragment()
    }

    private lateinit var viewModel: AnimeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.anime_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AnimeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
