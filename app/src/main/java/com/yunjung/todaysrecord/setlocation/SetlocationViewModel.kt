package com.yunjung.todaysrecord.setlocation

import com.yunjung.todaysrecord.models.AreaLarge
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.AreaMedium
import com.yunjung.todaysrecord.models.AreaSmall

class SetlocationViewModel : ViewModel() {
    private val _areaLargeList = MutableLiveData<List<AreaLarge>?>()
    private val _areaMediumList = MutableLiveData<List<AreaMedium>?>()
    private val _areaSmallList = MutableLiveData<List<AreaSmall>?>()

    val areaLargeList: LiveData<List<AreaLarge>?>
        get() = _areaLargeList

    val areaMediumList: LiveData<List<AreaMedium>?>
        get() = _areaMediumList

    val areaSmallList: LiveData<List<AreaSmall>?>
        get() = _areaSmallList

    fun getAreaLargeValue(areaLargeTmpList:List<AreaLarge>?) {
        _areaLargeList.value = areaLargeTmpList
    }

    fun getAreaMediumValue(areaMediumTmpList : List<AreaMedium>?){
        _areaMediumList.value = areaMediumTmpList
    }

    fun getAreaSmallValue(areaSmallTmpList : List<AreaSmall>?){
        _areaSmallList.value = areaSmallTmpList
    }
}