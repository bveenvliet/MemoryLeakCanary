package com.example.memoryleakcanary

import android.app.Application
import android.view.View

class MyApplication : Application() {

    // holding references to views (bad idea!)
    val leakedViews = mutableListOf<View>()

}