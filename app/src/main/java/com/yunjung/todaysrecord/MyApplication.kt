package com.yunjung.todaysrecord

import android.app.Application
import androidx.lifecycle.MutableLiveData

class MyApplication : Application() {
    val userId = MutableLiveData<String>("anonymous")
}