package com.yunjung.todaysrecord.models

data class PhotoBooth (
    var _id : String? = null,
    var brand : String? = null,
    var name : String? = null, // '인생네컷 - 홍대점'과 같은 풀네임을 저장
    var address : String? = null,
    var image : String? = null,
    var location : List<Double>? = null // 지도에 표시하기 위한 좌표
)
