package com.yunjung.todaysrecord.booth

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunjung.todaysrecord.models.PhotoBooth
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class BoothViewModel : ViewModel() {
    private val _adjBoothList = MutableLiveData<List<PhotoBooth>>()

    val adjBoothList: LiveData<List<PhotoBooth>>
        get() = _adjBoothList

    fun updateAdjBoothList(lng : String, lat : String) {
        viewModelScope.launch{
            val response = withContext(Dispatchers.IO){
                RetrofitManager.service.getPhotoboothByLocation(lng, lat)
            }
            _adjBoothList.value = response ?: listOf()
        }
    }
}