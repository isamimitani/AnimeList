package com.example.animelist.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.animelist.R
import com.example.animelist.ui.AnimeViewModel

class AnimeListFragment : Fragment() {

    companion object {
        fun newInstance() = AnimeListFragment()
    }

    private lateinit var viewModel: AnimeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.anime_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AnimeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
