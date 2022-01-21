package com.yunjung.todaysrecord.moreimage

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoreImageViewModel  : ViewModel() {
    private val _photoStudio = MutableLiveData<PhotoStudio>()
    private val _imageReviewList = MutableLiveData<List<Review>>()

    val photoStudio : LiveData<PhotoStudio>
        get() = _photoStudio

    val imageReviewList: LiveData<List<Review>>
        get() = _imageReviewList

    fun updatePhotoStudio(photoStudio: PhotoStudio){
        _photoStudio.value = photoStudio
    }

    fun getImageReviewList(){
        // 서버로 부터 받아온 사진관 정보와 대응되는 사진을 포함한 리뷰 리스트를 가져옴
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                try{
                    RetrofitManager.service.getImageReviewByPsId(photoStudio.value!!._id)
                }
                catch (e : Throwable){
                    listOf()
                }
            }
            _imageReviewList.value = response
        }
    }
}