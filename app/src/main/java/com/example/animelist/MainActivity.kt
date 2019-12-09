package com.example.animelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.animelist.ui.detail.AnimeDetailFragment
import com.example.animelist.ui.main.AnimeInfoFragment
import com.example.animelist.ui.main.dummy.DummyContent

class MainActivity : AppCompatActivity(), AnimeInfoFragment.OnListFragmentInteractionListener {

    companion object {
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

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        showAnimeDetailFragment()
    }

    fun showAnimeDetailFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AnimeDetailFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

}
