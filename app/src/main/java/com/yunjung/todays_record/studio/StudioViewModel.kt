package com.yunjung.todays_record.studio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yunjung.todays_record.models.PhotoStudio

class StudioViewModel : ViewModel() {
    // 수정 가능한 라이브 데이터 (클래스 내부에서만 사용)
    private val _photoStudioList = MutableLiveData<List<PhotoStudio>>()

    // 수정 불 가능한 라이브 데이터 (클래스 외부에서 접근 시 사용)
    val photoStudioList : LiveData<List<PhotoStudio>>
        get() = _photoStudioList // 클래스 내부에서 사용하는 변수를 get()으로 가져와 반환

    // 초기화
    private val tempList = ArrayList<PhotoStudio>()
    init {
        tempList.add(PhotoStudio("유명사진관", "15000", "권선구 탑동 00-00, 000호"))
        tempList.add(PhotoStudio("인기사진관", "8000", "영통구 매탄동 00-0"))
        tempList.add(PhotoStudio("무지개사진관", "12000", "팔달구 인계동 00-00, (시청역 부근)"))
        tempList.add(PhotoStudio("태양사진관", "10000", "팔달구 화서동 00-00, 0층"))
        tempList.add(PhotoStudio("사랑사진관", "5000", "장안구 정자동 00-00, 00빌딩 000호"))

        _photoStudioList.value = tempList
    }
}