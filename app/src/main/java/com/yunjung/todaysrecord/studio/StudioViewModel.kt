package com.yunjung.todaysrecord.studio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todaysrecord.models.PhotoStudio

class StudioViewModel : ViewModel() {
    // 수정 가능한 라이브 데이터 (클래스 내부에서만 사용)
    private val _idenStdioList = MutableLiveData<List<PhotoStudio>>()
    private val _profileStudioList = MutableLiveData<List<PhotoStudio>>()
    private val _moreStudioList = MutableLiveData<List<PhotoStudio>>()
    private val _otherStudioList = MutableLiveData<List<PhotoStudio>>()

    // 수정 불 가능한 라이브 데이터 (클래스 외부에서 접근 시 사용)
    val idenStdioList : LiveData<List<PhotoStudio>>
        get() = _idenStdioList
    val profileStudioList : LiveData<List<PhotoStudio>>
        get() = _profileStudioList
    val moreStudioList : LiveData<List<PhotoStudio>>
        get() = _moreStudioList
    val otherStudioList : LiveData<List<PhotoStudio>>
        get() = _otherStudioList

    // 초기화
    private val idenTempList = ArrayList<PhotoStudio>()
    private val profileTempList = ArrayList<PhotoStudio>()
    private val moreTempList = ArrayList<PhotoStudio>()
    private val otherTempList = ArrayList<PhotoStudio>()
    init {
        // 증명사진 사진관 리스트
        idenTempList.add(PhotoStudio(name = "태양 사진관", address = "권선구 탑동 9-21 202호",cost = "15000",
            intro = "사랑과 정성을 다하는 우리는 태양사진관~!\n 저렵한 가격에 잊을수 없는 추억을 선물합니다.\n믿고 맡겨 주시면 최선을 다할게요^^",
            phoneNumber = "010-5047-7784", siteAddress = "http://sunPhotoStudio.co.kr", instagramId = "sunnyDayPhoto"))

        _idenStdioList.value = idenTempList

        // 프로필사진 사진관 리스트
        profileTempList.add(PhotoStudio(name = "프로필 잘 찍는곳", address = "권선구 탑동 9-21 202호",cost = "15000",
            intro = "사랑과 정성을 다하는 우리는 태양사진관~!\n 저렵한 가격에 잊을수 없는 추억을 선물합니다.\n믿고 맡겨 주시면 최선을 다할게요^^",
            phoneNumber="010-5047-7784", siteAddress = "http://sunPhotoStudio.co.kr", instagramId = "sunnyDayPhoto"))

        _profileStudioList.value = profileTempList

        // 2인 이상 사진 사진관 리스트
        moreTempList.add(PhotoStudio(name = "가족 사진관", address = "권선구 탑동 9-21 202호",cost = "15000",
            intro = "사랑과 정성을 다하는 우리는 태양사진관~!\n 저렵한 가격에 잊을수 없는 추억을 선물합니다.\n믿고 맡겨 주시면 최선을 다할게요^^",
            phoneNumber="010-5047-7784", siteAddress = "http://sunPhotoStudio.co.kr", instagramId = "sunnyDayPhoto"))

        _moreStudioList.value = moreTempList

        // 기타 사진 사진관
        otherTempList.add(PhotoStudio(name = "철수 스냅샷", address = "권선구 탑동 9-21 202호",cost = "15000",
            intro = "사랑과 정성을 다하는 우리는 태양사진관~!\n 저렵한 가격에 잊을수 없는 추억을 선물합니다.\n믿고 맡겨 주시면 최선을 다할게요^^",
            phoneNumber="010-5047-7784", siteAddress = "http://sunPhotoStudio.co.kr", instagramId = "sunnyDayPhoto"))

        _otherStudioList.value = otherTempList
    }
}