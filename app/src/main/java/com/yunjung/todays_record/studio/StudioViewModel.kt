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
        idenTempList.add(PhotoStudio(1,"태양 사진관", "권선구 탑동 9-21 202호",15000,
            "사랑과 정성을 다하는 우리는 태양사진관~!\n 저렵한 가격에 잊을수 없는 추억을 선물합니다.\n믿고 맡겨 주시면 최선을 다할게요^^",
            "010-5047-7784", "http://sunPhotoStudio.co.kr", "sunnyDayPhoto"))
        idenTempList.add(PhotoStudio(2,"새로나 스튜디오", "영통구 매탄동 77-5", 8000,
            "안녕하세요.\n역에서 10분 거리!\n새학기 학생증 사진관은 저희 새로나 스튜디오!\n",
            "010-8808-2929", "http://newPhotoStudio.co.kr", "newnewPhoto"))
        idenTempList.add(PhotoStudio(3,"오디션 사진관", "팔달구 인계동 65-2 (시청역 부근)",12000,
            "믿을 수 있는 품격, 오디션 사진관\n수원의 역사깊은 오디션 사진관\n믿고 찾아주세요^^.\n",
            "010-3700-2521", "http://auditionPhotoStudio.co.kr", "auditionPhoto"))
        idenTempList.add(PhotoStudio(4,"시현하다", "팔달구 화서동 41-11번지 3층", 10000,
            "평벙한 증명사진은 가라!\n작가와 1:1 컨설팅을 통하여 맞춤 촬영을 진행합니다.\n여유로운 예약 부탁드립니다.",
            "010-6541-0901", "http://anotherPhotoStudio.co.kr", "anotherDay"))
        idenTempList.add(PhotoStudio(5,"smile photo studio", "장안구 정자동 35-4 골드빌딩 101호", 5000,
            "웃음이 끊이질 않는 smile photo studio~!\n유쾌한 사진작가와 잊을수 없는 추억을 만들어 보세요!\n후 보정까지 고객만족 100퍼센트!!",
            "010-2397-1125", "http://smilePhotoStudio.co.kr", "smile_sunshine"))

        _idenStdioList.value = idenTempList

        // 프로필사진 사진관 리스트
        profileTempList.add(PhotoStudio(1,"프로필 잘 찍는곳", "권선구 탑동 9-21 202호",15000,
            "사랑과 정성을 다하는 우리는 태양사진관~!\n 저렵한 가격에 잊을수 없는 추억을 선물합니다.\n믿고 맡겨 주시면 최선을 다할게요^^",
            "010-5047-7784", "http://sunPhotoStudio.co.kr", "sunnyDayPhoto"))
        profileTempList.add(PhotoStudio(2,"태양 사진관", "영통구 매탄동 77-5", 8000,
            "안녕하세요.\n역에서 10분 거리!\n새학기 학생증 사진관은 저희 새로나 스튜디오!\n",
            "010-8808-2929", "http://newPhotoStudio.co.kr", "newnewPhoto",))
        profileTempList.add(PhotoStudio(3,"스타 프로필", "팔달구 인계동 65-2 (시청역 부근)",12000,
            "믿을 수 있는 품격, 오디션 사진관\n수원의 역사깊은 오디션 사진관\n믿고 찾아주세요^^.\n",
            "010-3700-2521", "http://auditionPhotoStudio.co.kr", "auditionPhoto"))
        profileTempList.add(PhotoStudio(4,"안녕 스튜디오", "팔달구 화서동 41-11번지 3층", 10000,
            "평벙한 증명사진은 가라!\n작가와 1:1 컨설팅을 통하여 맞춤 촬영을 진행합니다.\n여유로운 예약 부탁드립니다.",
            "010-6541-0901", "http://anotherPhotoStudio.co.kr", "anotherDay"))
        profileTempList.add(PhotoStudio(5,"선명한 사진관", "장안구 정자동 35-4 골드빌딩 101호", 5000,
            "웃음이 끊이질 않는 smile photo studio~!\n유쾌한 사진작가와 잊을수 없는 추억을 만들어 보세요!\n후 보정까지 고객만족 100퍼센트!!",
            "010-2397-1125", "http://smilePhotoStudio.co.kr", "smile_sunshine"))

        _profileStudioList.value = profileTempList

        // 2인 이상 사진 사진관 리스트
        moreTempList.add(PhotoStudio(1,"가족 사진관", "권선구 탑동 9-21 202호",15000,
            "사랑과 정성을 다하는 우리는 태양사진관~!\n 저렵한 가격에 잊을수 없는 추억을 선물합니다.\n믿고 맡겨 주시면 최선을 다할게요^^",
            "010-5047-7784", "http://sunPhotoStudio.co.kr", "sunnyDayPhoto"))
        moreTempList.add(PhotoStudio(2,"사랑이야기", "영통구 매탄동 77-5", 8000,
            "안녕하세요.\n역에서 10분 거리!\n새학기 학생증 사진관은 저희 새로나 스튜디오!\n",
            "010-8808-2929", "http://newPhotoStudio.co.kr", "newnewPhoto"))
        moreTempList.add(PhotoStudio(3,"오르골 사진관", "팔달구 인계동 65-2 (시청역 부근)",12000,
            "믿을 수 있는 품격, 오디션 사진관\n수원의 역사깊은 오디션 사진관\n믿고 찾아주세요^^.\n",
            "010-3700-2521", "http://auditionPhotoStudio.co.kr", "auditionPhoto"))
        moreTempList.add(PhotoStudio(4,"찰칵 스튜디오", "팔달구 화서동 41-11번지 3층", 10000,
            "평벙한 증명사진은 가라!\n작가와 1:1 컨설팅을 통하여 맞춤 촬영을 진행합니다.\n여유로운 예약 부탁드립니다.",
            "010-6541-0901", "http://anotherPhotoStudio.co.kr", "anotherDay"))
        moreTempList.add(PhotoStudio(5,"마리오 스튜디오", "장안구 정자동 35-4 골드빌딩 101호", 5000,
            "웃음이 끊이질 않는 smile photo studio~!\n유쾌한 사진작가와 잊을수 없는 추억을 만들어 보세요!\n후 보정까지 고객만족 100퍼센트!!",
            "010-2397-1125", "http://smilePhotoStudio.co.kr", "smile_sunshine"))

        _moreStudioList.value = moreTempList

        // 기타 사진 사진관
        otherTempList.add(PhotoStudio(1,"철수 스냅샷", "권선구 탑동 9-21 202호",15000,
            "사랑과 정성을 다하는 우리는 태양사진관~!\n 저렵한 가격에 잊을수 없는 추억을 선물합니다.\n믿고 맡겨 주시면 최선을 다할게요^^",
            "010-5047-7784", "http://sunPhotoStudio.co.kr", "sunnyDayPhoto"))
        otherTempList.add(PhotoStudio(2,"영희 스냅샷", "영통구 매탄동 77-5", 8000,
            "안녕하세요.\n역에서 10분 거리!\n새학기 학생증 사진관은 저희 새로나 스튜디오!\n",
            "010-8808-2929", "http://newPhotoStudio.co.kr", "newnewPhoto"))
        otherTempList.add(PhotoStudio(3,"유영 스튜디오", "팔달구 인계동 65-2 (시청역 부근)",12000,
            "믿을 수 있는 품격, 오디션 사진관\n수원의 역사깊은 오디션 사진관\n믿고 찾아주세요^^.\n",
            "010-3700-2521", "http://auditionPhotoStudio.co.kr", "auditionPhoto"))
        otherTempList.add(PhotoStudio(4,"보름달 사진관", "팔달구 화서동 41-11번지 3층", 10000,
            "평벙한 증명사진은 가라!\n작가와 1:1 컨설팅을 통하여 맞춤 촬영을 진행합니다.\n여유로운 예약 부탁드립니다.",
            "010-6541-0901", "http://anotherPhotoStudio.co.kr", "anotherDay"))
        otherTempList.add(PhotoStudio(5,"lol 스튜디오", "장안구 정자동 35-4 골드빌딩 101호", 5000,
            "웃음이 끊이질 않는 smile photo studio~!\n유쾌한 사진작가와 잊을수 없는 추억을 만들어 보세요!\n후 보정까지 고객만족 100퍼센트!!",
            "010-2397-1125", "http://smilePhotoStudio.co.kr", "smile_sunshine"))

        _otherStudioList.value = otherTempList
    }
}