package com.yunjung.todaysrecord.setlocation

import com.yunjung.todaysrecord.models.AreaLarge
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.AreaMedium
import com.yunjung.todaysrecord.models.AreaSmall

class SetlocationViewModel : ViewModel() {
    var areaLargeList = MutableLiveData<List<AreaLarge>>() // '시도' 지역 리스트를 저장
    var areaMediumList = MutableLiveData<List<AreaMedium>>() // '시군구' 지역 리스트를 저장
    var areaSmallList = MutableLiveData<List<AreaSmall>>() // '동읍면' 지역 리스트를 저장
}