package com.yunjung.todays_record.models

data class Review(var _id : Int? = null,
                  var userId : String? = null,
                  var rating : Int? = null,
                  var content : String? = null,
                  var createdTime : Int? = null)