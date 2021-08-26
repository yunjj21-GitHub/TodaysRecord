package com.yunjung.todays_record.models

// Kotlin의 data class : getter(), setter() 등이 자동으로 생성된다.
// PhotoStudio 데이터의 모양을 정의함
data class PhotoStudio (var _id : Int? = null,
                        var name : String? = null,
                        var address : String? = null,
                        var cost : Int? = null)