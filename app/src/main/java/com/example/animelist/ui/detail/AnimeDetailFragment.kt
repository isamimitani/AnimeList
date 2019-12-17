package com.example.animelist.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.animelist.MyApplication
import com.example.animelist.R
import com.example.animelist.databinding.AnimeDetailFragmentBinding
import com.example.animelist.entity.AnimeDetail
import com.example.animelist.ui.AnimeViewModel
import kotlinx.android.synthetic.main.anime_detail_fragment.*
import javax.inject.Inject

class AnimeDetailFragment : Fragment() {

    companion object {
        @JvmField
        val TAG = AnimeDetailFragment::class.simpleName
        const val ARG_ANIME_ID = "column-count"
        fun newInstance(animeId: String) = AnimeDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_ANIME_ID, animeId)
            }
        }
    }

    @Inject
    lateinit var viewModel: AnimeViewModel

    private lateinit var animeId: String

    private lateinit var animeDetailFragmentBinding: AnimeDetailFragmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Make Dagger instantiate @Inject fields in MainActivity
        (context.applicationContext as MyApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            animeId = it.getString(ARG_ANIME_ID, "")
        }
        viewModel.loadAnimeDetail(animeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        animeDetailFragmentBinding = DataBindingUtil
            .inflate(inflater, R.layout.anime_detail_fragment, container, false)
        animeDetailFragmentBinding.viewmodel = viewModel
        animeDetailFragmentBinding.fragment = this
        animeDetailFragmentBinding.lifecycleOwner = this
        return animeDetailFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.animeDetailLiveData
            .observe(viewLifecycleOwner, Observer<AnimeDetail> { animeDetail ->
                updateImage(animeDetail)
            })
        anime_id.text = animeId
    }

    private fun updateImage(animeDetail: AnimeDetail) {
        Glide.with(this)
            .load(Uri.parse(animeDetail.pictureUrl ?: ""))
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(anime_image)
    }

    fun openUrl(view: View) {
        if (view is TextView && view.text != "") {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(view.text.toString())
            startActivity(intent)
        }
    }

}
