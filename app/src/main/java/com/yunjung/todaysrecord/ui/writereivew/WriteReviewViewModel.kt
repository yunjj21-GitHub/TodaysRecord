package com.yunjung.todaysrecord.ui.writereivew

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.User
import java.io.File

class WriteReviewViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    private val _psId = MutableLiveData<String>()
    private val _reviewImageBitmap = MutableLiveData<Bitmap>()

    val user : LiveData<User>
        get() = _user

    val psId : LiveData<String>
        get() = _psId

    val reviewImageBitmap : LiveData<Bitmap>
        get() = _reviewImageBitmap

    fun updateUser(user : User) {
        _user.value = user
    }

    fun updatePsId(id : String){
        _psId.value = id
    }

    fun updateReviewImageBitmap(newBitmap : Bitmap){
        _reviewImageBitmap.value = newBitmap
    }
}