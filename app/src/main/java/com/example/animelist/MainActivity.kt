package com.example.animelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.animelist.ui.AnimeViewModel
import com.example.animelist.ui.detail.AnimeDetailFragment
import com.example.animelist.ui.main.AnimeInfoFragment
import com.example.animelist.ui.main.dummy.DummyContent
import javax.inject.Inject

class MainActivity : AppCompatActivity(), AnimeInfoFragment.OnListFragmentInteractionListener {

    companion object {
        val TAG = MainActivity::class.simpleName
    }

    // You want Dagger to provide an instance of AnimeViewModel from the graph
    @Inject
    lateinit var animeViewModel: AnimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Make Dagger instantiate @Inject fields in MainActivity
        (applicationContext as MyApplication).appComponent.inject(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AnimeInfoFragment.newInstance(1))
                .commitNow()
        }
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        showAnimeDetailFragment()
    }

    fun showAnimeDetailFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AnimeDetailFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

}
