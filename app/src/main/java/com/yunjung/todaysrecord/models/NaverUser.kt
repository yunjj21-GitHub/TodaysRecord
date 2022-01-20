package com.yunjung.todaysrecord.models

data class NaverUser(
    val resultcode : String,
    val message : String,
    val response : Response
)

data class Response(
    val email : String,
    val nickname : String,
    val profile_image : String,
    val age : String,
    val gender : String,
    val id : String,
    val name : String,
    val birthday : String,
    val birthyear : String,
    val mobile : String
)