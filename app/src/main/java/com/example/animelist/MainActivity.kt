package com.example.animelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.animelist.entity.AnimeInfo
import com.example.animelist.ui.detail.AnimeDetailFragment
import com.example.animelist.ui.main.AnimeInfoFragment

class MainActivity : AppCompatActivity(), AnimeInfoFragment.OnListFragmentInteractionListener {

    companion object {
        @JvmField
        val TAG = MainActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AnimeInfoFragment.newInstance(1))
                .commitNow()
        }
    }

    override fun onListFragmentInteraction(item: AnimeInfo?) {
        showAnimeDetailFragment(item?.id)
    }

    fun showAnimeDetailFragment(animeId: String?) {
        if (animeId != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AnimeDetailFragment.newInstance(animeId))
                .addToBackStack(null)
                .commit()
        }
    }

}
