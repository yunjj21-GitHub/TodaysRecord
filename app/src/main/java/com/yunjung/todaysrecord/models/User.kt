package com.yunjung.todaysrecord.models

data class User(
    var _id : String? = null,
    var nickName : String? = null,
    var profileImage : String? = null,
    var interests : List<String>? = null
)
