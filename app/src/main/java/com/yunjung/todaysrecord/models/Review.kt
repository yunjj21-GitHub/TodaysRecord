package com.yunjung.todaysrecord.models

data class Review(
    var _id : String? = null,
    var psId : String? = null,
    var userId : String? = null,
    var rating : Int? = null,
    var content : String? = null,
    var image : String? = null
)