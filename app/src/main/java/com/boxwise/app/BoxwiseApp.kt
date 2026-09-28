package com.boxwise.app

import android.app.Application
import com.boxwise.app.data.BoxwiseDatabase

class BoxwiseApp : Application() {
    val db: BoxwiseDatabase by lazy { BoxwiseDatabase.get(this) }
}
