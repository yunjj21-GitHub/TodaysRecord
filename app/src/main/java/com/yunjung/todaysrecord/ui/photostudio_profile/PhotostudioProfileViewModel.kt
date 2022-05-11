package com.yunjung.todaysrecord.ui.photostudio_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotostudioProfileViewModel : ViewModel() {
    private val _userArea = MutableLiveData<String>() // 지역설정 정보 저장
    private val _sortOption = MutableLiveData<String>() // 정렬옵션 정보 저장
    private val _photoStudioList = MutableLiveData<List<PhotoStudio>>()

    val userArea : LiveData<String>
        get() = _userArea

    val sortOption : LiveData<String>
        get() = _sortOption

    val photoStudioList : LiveData<List<PhotoStudio>>
        get() = _photoStudioList

    fun updateUserArea(area : String){
        _userArea.value = area
    }

    fun updateSortOption(selected : String){
        _sortOption.value = selected
    }

    fun updatePhotoStudioList(){
        // 지역설정과 정렬옵션에 따라 사진관 리스트 가져오기
        when (sortOption.value) {
            "기본순" -> {
                getPsListOfUserAreaInBasicOrder()
            }
            "인기순" -> {
                getPsListOfUserAreaInPopularityOrder()
            }
            "가격순" -> {
                getPsListOfUserAreaInCostOrder()
            }
        }
    }

    // 기본순으로 선택한 지역 사진관 가져오기
    private fun getPsListOfUserAreaInBasicOrder(){
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                try {
                    RetrofitManager.service.getPhotoStudioByAreaAndType(area = userArea.value, type = "프로필")
                }
                catch (e : Throwable){
                    listOf()
                }
            }
            _photoStudioList.value = response
        }
    }

    // 인기순으로 선택한 지역 사진관 가져오기
    private fun getPsListOfUserAreaInPopularityOrder(){
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                try {
                    RetrofitManager.service.getPsListOfUserAreaInPopularityOrder(area = userArea.value, type = "프로필")
                }
                catch (e : Throwable){
                    listOf()
                }
            }
            _photoStudioList.value = response
        }
    }

    // 가격순으로 선택한 지역 사진관 가져오기
    private fun getPsListOfUserAreaInCostOrder(){
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                try {
                    RetrofitManager.service.getPsListOfUserAreaInCostOrder(area = userArea.value, type = "프로필")
                }
                catch (e : Throwable){
                    listOf()
                }
            }
            _photoStudioList.value = response
        }
    }
}