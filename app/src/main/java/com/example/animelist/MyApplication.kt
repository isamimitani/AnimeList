package com.example.animelist

import android.app.Application
import com.example.animelist.di.DaggerApplicationComponent


class MyApplication : Application() {
    // Reference to the application graph that is used across the whole app
    val appComponent = DaggerApplicationComponent.create()
}