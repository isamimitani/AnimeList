package com.example.animelist.ui.detail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.animelist.MyApplication
import com.example.animelist.R
import com.example.animelist.ui.AnimeViewModel
import javax.inject.Inject

class AnimeDetailFragment : Fragment() {

    val TAG = "AnimeDetailFragment"

    companion object {
        val TAG = AnimeDetailFragment::class.simpleName
        fun newInstance() = AnimeDetailFragment()
    }

    @Inject
    lateinit var viewModel: AnimeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Make Dagger instantiate @Inject fields in MainActivity
        (context.applicationContext as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.anime_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
