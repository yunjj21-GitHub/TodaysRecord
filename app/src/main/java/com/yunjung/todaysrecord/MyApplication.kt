package com.yunjung.todaysrecord

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yunjung.todaysrecord.models.User

class MyApplication : Application() {
    val user = MutableLiveData<User>(User("anonymous", "로그인해주세요", null, null))
}