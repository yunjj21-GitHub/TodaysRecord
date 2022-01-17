package com.yunjung.todaysrecord.moreimage

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.review.ReviewFragment
import retrofit2.Call
import retrofit2.Response

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
        val call : Call<List<Review>>? = RetrofitManager.iRetrofit?.getImageReviewByPsId(
            photoStudio.value!!._id)
        call?.enqueue(object : retrofit2.Callback<List<Review>> {
            // 응답 성공시
            override fun onResponse(
                call: Call<List<Review>>,
                response: Response<List<Review>>
            ) {
                _imageReviewList.value = response.body() ?: listOf()
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }
}