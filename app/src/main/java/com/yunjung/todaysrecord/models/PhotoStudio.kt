package com.yunjung.todaysrecord.models

import android.os.Parcelable
import com.google.gson.JsonObject
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

// Kotlin의 data class : getter(), setter() 등이 자동으로 생성된다.
// PhotoStudio 데이터의 모양을 정의함
@Parcelize
data class PhotoStudio (
    var _id : String? = null,
    var area : List<String>? = null, // 해당 사진관이 보여질 지역
    var type : String? = null,
    var name : String? = null,
    var address : String? = null, // 상세주소
    var cost : Int? = null,
    var image : List<String>? = null, // 빈 배열이더라도 배열이 온다.
    var phoneNumber : String? = null,
    var siteAddress : String? = null, // 인터넷 주소
    var instagramId : String? = null,
    var intro : String? = null,
    var location : List<Double>? = null // 지도에 표시하기 위한 좌표
) : Parcelable