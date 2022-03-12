package com.yunjung.todaysrecord.ui.booth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunjung.todaysrecord.models.PhotoBooth
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoothViewModel : ViewModel() {
    private val _adjBoothList = MutableLiveData<List<PhotoBooth>>()

    val adjBoothList: LiveData<List<PhotoBooth>>
        get() = _adjBoothList

    fun updateAdjBoothList(userLng : String, userLat : String) {
        viewModelScope.launch{
            val response = withContext(Dispatchers.IO){
                try {
                    RetrofitManager.service.getPhotoboothByLocation(userLng, userLat)
                } catch (e : Throwable){
                    listOf()
                }
            }
            _adjBoothList.value = response
        }
    }
}