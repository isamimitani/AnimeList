package com.example.animelist.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.animelist.MyApplication
import com.example.animelist.R
import com.example.animelist.databinding.AnimeDetailFragmentBinding
import com.example.animelist.ui.AnimeViewModel
import javax.inject.Inject

class AnimeDetailFragment : Fragment() {

    companion object {
        @JvmField
        val TAG = AnimeDetailFragment::class.simpleName
    }

    @Inject
    lateinit var viewModel: AnimeViewModel

    private lateinit var animeDetailFragmentBinding: AnimeDetailFragmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Make Dagger instantiate @Inject fields in MainActivity
        (context.applicationContext as MyApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var args = AnimeDetailFragmentArgs.fromBundle(arguments!!)
        viewModel.loadAnimeDetail(args.animeId)
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

    fun openUrl(view: View) {
        if (view is TextView && view.text != "") {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(view.text.toString())
            startActivity(intent)
        }
    }

}
