package com.yunjung.todays_record.studio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todays_record.R
import com.yunjung.todays_record.models.PhotoStudio

class StudioViewModel : ViewModel() {
    // 수정 가능한 라이브 데이터 (클래스 내부에서만 사용)
    private val _idenStdioList = MutableLiveData<List<PhotoStudio>>()
    private val _profileStudioList = MutableLiveData<List<PhotoStudio>>()
    private val _moreStudioList = MutableLiveData<List<PhotoStudio>>()
    private val _otherStudioList = MutableLiveData<List<PhotoStudio>>()

    // 수정 불 가능한 라이브 데이터 (클래스 외부에서 접근 시 사용)
    val idenStdioList : LiveData<List<PhotoStudio>>
        get() = _idenStdioList // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환
    val profileStudioList : LiveData<List<PhotoStudio>>
        get() = _profileStudioList // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환
    val moreStudioList : LiveData<List<PhotoStudio>>
        get() = _moreStudioList // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환
    val otherStudioList : LiveData<List<PhotoStudio>>
        get() = _otherStudioList // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환

    // 초기화
    private val idenTempList = ArrayList<PhotoStudio>()
    private val profileTempList = ArrayList<PhotoStudio>()
    private val moreTempList = ArrayList<PhotoStudio>()
    private val otherTempList = ArrayList<PhotoStudio>()
    init {
        // 증명사진 사진관 리스트
        idenTempList.add(PhotoStudio(1,"태양 사진관", "권선구 탑동 9-21 202호",15000))
        idenTempList.add(PhotoStudio(2,"새로나 스튜디오", "영통구 매탄동 77-5", 8000))
        idenTempList.add(PhotoStudio(3,"오디션 사진관", "팔달구 인계동 65-2 (시청역 부근)",12000))
        idenTempList.add(PhotoStudio(4,"시현하다", "팔달구 화서동 41-11번지 3층", 10000))
        idenTempList.add(PhotoStudio(5,"smile photo studio", "장안구 정자동 35-4 골드빌딩 101호", 5000))

        _idenStdioList.value = idenTempList

        // 프로필사진 사진관 리스트
        profileTempList.add(PhotoStudio(1,"프로필 잘 찍는곳", "권선구 탑동 9-21 202호",15000))
        profileTempList.add(PhotoStudio(2,"태양 사진관", "영통구 매탄동 77-5", 8000))
        profileTempList.add(PhotoStudio(3,"스타 프로필", "팔달구 인계동 65-2 (시청역 부근)",12000))
        profileTempList.add(PhotoStudio(4,"안녕 스튜디오", "팔달구 화서동 41-11번지 3층", 10000))
        profileTempList.add(PhotoStudio(5,"선명한 사진관", "장안구 정자동 35-4 골드빌딩 101호", 5000))

        _profileStudioList.value = profileTempList

        // 2인 이상 사진 사진관 리스트
        moreTempList.add(PhotoStudio(1,"가족 사진관", "권선구 탑동 9-21 202호",15000))
        moreTempList.add(PhotoStudio(2,"사랑이야기", "영통구 매탄동 77-5", 8000))
        moreTempList.add(PhotoStudio(3,"오르골 사진관", "팔달구 인계동 65-2 (시청역 부근)",12000))
        moreTempList.add(PhotoStudio(4,"찰칵 스튜디오", "팔달구 화서동 41-11번지 3층", 10000))
        moreTempList.add(PhotoStudio(5,"마리오 스튜디오", "장안구 정자동 35-4 골드빌딩 101호", 5000))

        _moreStudioList.value = moreTempList

        // 기타 사진 사진관
        otherTempList.add(PhotoStudio(1,"철수 스냅샷", "권선구 탑동 9-21 202호",15000))
        otherTempList.add(PhotoStudio(2,"영희 스냅샷", "영통구 매탄동 77-5", 8000))
        otherTempList.add(PhotoStudio(3,"유영 스튜디오", "팔달구 인계동 65-2 (시청역 부근)",12000))
        otherTempList.add(PhotoStudio(4,"보름달 사진관", "팔달구 화서동 41-11번지 3층", 10000))
        otherTempList.add(PhotoStudio(5,"lol 스튜디오", "장안구 정자동 35-4 골드빌딩 101호", 5000))

        _otherStudioList.value = otherTempList
    }
}