package com.yunjung.todaysrecord.information

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentInformationBinding
import com.yunjung.todaysrecord.detail.DetailFragmentArgs
import com.yunjung.todaysrecord.models.PhotoStudio

class InformationFragment : Fragment(), OnMapReadyCallback{
    lateinit var binding : FragmentInformationBinding
    lateinit var viewModel: InformationViewModel

    private lateinit var naverMap: NaverMap

    companion object{
        // Fragment가 생성될 때 DetailFragmentArgs 객체를 전달 받음
        lateinit var photoStudio: PhotoStudio
        fun newInstance(args: DetailFragmentArgs) : InformationFragment {
            photoStudio = args.photoStudio!!
            return InformationFragment()
        }
    }

    // 프래그먼트가 생성될 때 실행
    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_information, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(InformationViewModel::class.java)
        binding.viewModel = viewModel

        // photoStudio 객체 사용가능
        viewModel.updateValue(photoStudio)

        // null data 처리
        if(viewModel.photoStudio.value!!.cost == null) binding.costText.text = "가격 정보 없음"

        /* 네이버 지도 관련  */
        val fragmentManager = childFragmentManager

        // 카메라 초기 위치 설정
        val options = NaverMapOptions()
            .camera(CameraPosition(LatLng(photoStudio.location!![1], photoStudio.location!![0]), 16.0))

        // 네이버 지도 객체 생성
        val mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance(options).also {
                fragmentManager.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this) // 비동기로 naverMap 객체를 얻어옴
    }

    // NaverMap 객체를 얻어 올 수 있음
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // 해당 사진관 마커 추가
        val marker = Marker()
        marker.position = LatLng(photoStudio.location!![1], photoStudio.location!![0]) // LatLng(latitude, longitude)
        marker.captionText = photoStudio.name.toString()
        marker.captionRequestedWidth = 200
        marker.map = this.naverMap
    }

    // 해당 프래그먼트가 재개될 때 실행
    override fun onResume() {
        super.onResume()

        if(this::naverMap.isInitialized) { // naverMap 객체가 초기화 되었다면
            // 해당 사진관 마커 다시 추가
            val marker = Marker()
            marker.position = LatLng(photoStudio.location!![1], photoStudio.location!![0])
            marker.captionText = photoStudio.name.toString()
            marker.captionRequestedWidth = 200
            marker.map = this.naverMap
        }
    }
}