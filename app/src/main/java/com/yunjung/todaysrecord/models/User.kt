package com.yunjung.todaysrecord.models

data class User(
    var _id : String? = null,
    var profileImage : String? = null,
    var nickname : String? = null,
    var email : String? = null,
    var password : String? = null
)
