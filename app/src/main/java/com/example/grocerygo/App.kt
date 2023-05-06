package com.example.grocerygo

import android.app.Application
import com.example.grocerygo.data.PrefStore
import com.example.grocerygo.network.NetworkInteraction
import com.facebook.drawee.backends.pipeline.Fresco

class App : Application()  {

    override fun onCreate() {
        super.onCreate()
        PrefStore.initialize(this)
        Fresco.initialize(this)
        NetworkInteraction.initialize()
    }
}