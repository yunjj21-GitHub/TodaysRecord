package com.yunjung.todaysrecord.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Kotlin의 data class : getter(), setter() 등이 자동으로 생성된다.
// PhotoStudio 데이터의 모양을 정의함
@Parcelize
data class PhotoStudio (
    var _id : Int? = null,
    var name : String? = null,
    var address : String? = null,
    var cost : Int? = null,
    var content : String? = null,
    var phoneNumber : String? = null,
    var homePage : String? = null,
    var instarId : String? = null
) : Parcelable