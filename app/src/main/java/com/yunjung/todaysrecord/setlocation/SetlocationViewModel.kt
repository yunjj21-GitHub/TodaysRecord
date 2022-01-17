package com.yunjung.todaysrecord.setlocation

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import com.yunjung.todaysrecord.models.AreaLarge
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.AreaMedium
import com.yunjung.todaysrecord.models.AreaSmall
import com.yunjung.todaysrecord.network.RetrofitManager
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
        val call : Call<List<AreaLarge>>? = RetrofitManager.iRetrofit?.getAreaLarge()
        call?.enqueue(object : retrofit2.Callback<List<AreaLarge>>{
            // 응답 성공시
            override fun onResponse(
                call: Call<List<AreaLarge>>,
                response: Response<List<AreaLarge>>
            ) {
                _cityList.value = response.body()!!
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<AreaLarge>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }

    fun updateTownList(clickedArea : String){
        val call : Call<List<AreaMedium>>? = RetrofitManager.iRetrofit?.getAreaMediumByBelong(clickedArea)
        call?.enqueue(object : retrofit2.Callback<List<AreaMedium>> {
            // 응답 성공시
            override fun onResponse(
                call: Call<List<AreaMedium>>,
                response: Response<List<AreaMedium>>
            ) {
                // townList 업데이트
                _townList.value = response.body() ?: listOf()
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<AreaMedium>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }

    fun updateVillageList(clickedArea: String){
        if(clickedArea == "초기화") {
            _villageList.value = listOf()
            return
        }

        val call : Call<List<AreaSmall>>? = RetrofitManager.iRetrofit?.getAreaSmallByBelong(clickedArea)
        call?.enqueue(object : retrofit2.Callback<List<AreaSmall>>{
            // 응답 성공시
            override fun onResponse(
                call: Call<List<AreaSmall>>,
                response: Response<List<AreaSmall>>
            ) {
                // villageList 업데이트
                _villageList.value = response.body() ?: listOf()
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<AreaSmall>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }
}