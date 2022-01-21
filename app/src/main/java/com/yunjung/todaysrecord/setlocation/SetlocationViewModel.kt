package com.yunjung.todaysrecord.setlocation

import android.content.ContentValues
import android.util.Log
import com.yunjung.todaysrecord.models.AreaLarge
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunjung.todaysrecord.models.AreaMedium
import com.yunjung.todaysrecord.models.AreaSmall
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class SetlocationViewModel : ViewModel() {
    // 유저가 선택한 지역명을 저장
    private val _city = MutableLiveData("")
    private val _town = MutableLiveData("")
    private val _village = MutableLiveData("")

    // 보여져야 하는 지역 리스트를 저장
    private val _cityList = MutableLiveData<List<AreaLarge>>()
    private val _townList = MutableLiveData<List<AreaMedium>>()
    private val _villageList = MutableLiveData<List<AreaSmall>>()

    val city : LiveData<String>
        get() = _city

    val town : LiveData<String>
        get()= _town

    val village : LiveData<String>
        get() = _village

    val cityList : LiveData<List<AreaLarge>>
        get() = _cityList

    val townList : LiveData<List<AreaMedium>>
        get() = _townList

    val villageList : LiveData<List<AreaSmall>>
        get() = _villageList

    fun updateCity(selected : String){
        _city.value = selected
    }

    fun updateTown(selected : String){
        _town.value = selected
    }

    fun updateVillage(selected : String){
        _village.value = selected
    }

    fun updateCityList(){
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                RetrofitManager.service.getAreaLarge()
            }
            _cityList.value = response
        }
    }

    fun updateTownList(clickedArea : String){
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                try {
                    RetrofitManager.service?.getAreaMediumByBelong(clickedArea)
                }
                catch (e : Throwable){
                    listOf()
                }
            }
            _townList.value = response
        }
    }

    fun updateVillageList(clickedArea: String){
        if(clickedArea == "초기화") {
            _villageList.value = listOf()
            return
        }

        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                try{
                    RetrofitManager.service?.getAreaSmallByBelong(clickedArea)
                }
               catch (e : Throwable){
                   listOf()
               }
            }
            _villageList.value = response
        }
    }
}