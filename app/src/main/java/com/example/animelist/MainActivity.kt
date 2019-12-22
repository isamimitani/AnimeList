package com.example.animelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.animelist.ui.main.AnimeInfoFragment

class MainActivity : AppCompatActivity() {

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

}
