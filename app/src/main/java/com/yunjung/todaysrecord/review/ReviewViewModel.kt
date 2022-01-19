package com.yunjung.todaysrecord.review

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class ReviewViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    private val _photoStudio = MutableLiveData<PhotoStudio>()
    private val _reviewList = MutableLiveData<List<Review>>() // 사진관의 리뷰리스트
    private val _reviewNum = MutableLiveData(0) // 리뷰의 개수
    private val _starNum = MutableLiveData<MutableList<Int>>(mutableListOf(0, 0, 0, 0, 0)) // 각 별점의 개수
    private val _starRatio = MutableLiveData<MutableList<Int>>(mutableListOf(0, 0, 0, 0, 0)) // 각 별점의 비율을 저장
    private val _reviewAvg = MutableLiveData<Int>(0)

    val user : LiveData<User>
        get() = _user

    val photoStudio : LiveData<PhotoStudio>
        get() = _photoStudio

    val reviewList : LiveData<List<Review>>
        get() = _reviewList

    val reviewNum : LiveData<Int>
        get() = _reviewNum

    val starNum : LiveData<MutableList<Int>>
        get() = _starNum

    val starRatio : LiveData<MutableList<Int>>
        get() = _starRatio

    val reviewAvg : LiveData<Int>
        get() = _reviewAvg


    fun updateUser(user : User){
        _user.value = user
    }

    fun updatePhotoStudio(photoStudio: PhotoStudio){
        _photoStudio.value = photoStudio
    }

    fun updateReviewList(){
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                RetrofitManager.service.getReviewByPsId(photoStudio.value!!._id)
            }
            _reviewList.value = response ?: listOf()
            _reviewNum.value = (response ?: listOf()).size
            updateStarNum()
        }
    }

    fun updateStarNum(){
        if(reviewList.value!!.isEmpty()) return

        var total = 0
        for(review in reviewList.value!!) {
            total += review.rating!!
            when(review.rating){
                5 -> _starNum.value!![0]++
                4 -> _starNum.value!![1]++
                3 -> _starNum.value!![2]++
                2 -> _starNum.value!![3]++
                1 -> _starNum.value!![4]++
            }
        }
        updateStarRatio()
        updateReviewAvg(total)
    }

    private fun updateStarRatio(){
        if(reviewList.value!!.isEmpty()) return

        _starRatio.value!![0] = (starNum.value!![0]*100)/reviewList.value!!.size
        _starRatio.value!![1] = (starNum.value!![1]*100)/reviewList.value!!.size
        _starRatio.value!![2] = (starNum.value!![2]*100)/reviewList.value!!.size
        _starRatio.value!![3] = (starNum.value!![3]*100)/reviewList.value!!.size
        _starRatio.value!![4] = (starNum.value!![4]*100)/reviewList.value!!.size
    }

    private fun updateReviewAvg(total : Int){
        if(reviewList.value!!.isEmpty()) return

        _reviewAvg.value = total / _reviewList.value!!.size
    }
}