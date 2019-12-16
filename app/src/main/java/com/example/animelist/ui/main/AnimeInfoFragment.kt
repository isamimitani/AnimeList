package com.example.animelist.ui.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.animelist.MyApplication
import com.example.animelist.R
import com.example.animelist.entity.AnimeInfo
import com.example.animelist.ui.AnimeViewModel

import kotlinx.android.synthetic.main.fragment_animeinfo_list.*
import kotlinx.android.synthetic.main.fragment_animeinfo_list.view.*
import javax.inject.Inject

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [AnimeInfoFragment.OnListFragmentInteractionListener] interface.
 */
class AnimeInfoFragment : Fragment() {

    companion object {
        @JvmField
        val TAG = AnimeInfoFragment::class.simpleName
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            AnimeInfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    @Inject
    lateinit var viewModel: AnimeViewModel

    private var columnCount = 1

    private lateinit var mAdapter : MyAnimeInfoRecyclerViewAdapter
    private var listener: OnListFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Make Dagger instantiate @Inject fields in AnimeInfoFragment
        (context.applicationContext as MyApplication).appComponent.inject(this)

        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT, 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_animeinfo_list, container, false)

        // Set the adapter
        if (view.anime_list is RecyclerView) {
            with(view.anime_list) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                mAdapter = MyAnimeInfoRecyclerViewAdapter(listOf(), listener)
                adapter = mAdapter
//                adapter = MyAnimeInfoRecyclerViewAdapter(listOf(), listener)
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getAnimeListLiveData().observe(viewLifecycleOwner, Observer<List<AnimeInfo>> { animes ->
            if (animes.isNotEmpty()) {
                //anime_list.adapter = MyAnimeInfoRecyclerViewAdapter(animes, listener)
                mAdapter.setNewData(animes)
                progress_circular.visibility = View.INVISIBLE
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: AnimeInfo?)
    }

}
