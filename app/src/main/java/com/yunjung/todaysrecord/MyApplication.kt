package com.yunjung.todaysrecord

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.kakao.sdk.common.KakaoSdk
import com.yunjung.todaysrecord.models.User
import android.app.Activity

import android.content.SharedPreferences
import com.yunjung.todaysrecord.main.MainActivity
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.*


class MyApplication : Application() {
    val user = MutableLiveData<User>(User("anonymous", "로그인해주세요", null, null))

    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화
        KakaoSdk.init(this, "7c93cbfeb40b9cd61f2fa335d6c8d04a")
    }
}