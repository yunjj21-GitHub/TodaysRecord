package com.yunjung.todaysrecord

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.common.KakaoSdk
import com.yunjung.todaysrecord.models.User
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    val user = MutableLiveData<User>(User())

    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화
        KakaoSdk.init(this, "7c93cbfeb40b9cd61f2fa335d6c8d04a")
    }
}