package com.yunjung.todaysrecord.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    var _id : String? = null,
    var psId : String? = null,
    var userId : String? = null,
    var rating : Int? = null,
    var content : String? = null,
    var image : String? = null
) : Parcelable