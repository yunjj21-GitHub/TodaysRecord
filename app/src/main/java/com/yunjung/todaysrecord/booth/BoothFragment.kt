package com.yunjung.todaysrecord.booth

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentBoothBinding

class BoothFragment : Fragment(), OnMapReadyCallback{
    lateinit var binding : FragmentBoothBinding
    lateinit var viewModel: BoothViewModel

    // 네이버 지도 사용자 위치 표시 관련 변수
    lateinit var locationSource : FusedLocationSource
    lateinit var naverMap: NaverMap

    companion object{
        fun newInstance() : BoothFragment{
            return BoothFragment()
        }

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000 // 네이버 지도 사용자 위치 표시 관련
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booth, container, false)

        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(BoothViewModel::class.java)
        binding.viewModel = viewModel

        /* 네이버 지도 관련 */
        // 사용자 현재 위치 추가
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    /* 네이버 지도 사용자 위치 표시 관련 */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if(locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) { // 권한이 거부 되었을 때
                naverMap.locationTrackingMode = LocationTrackingMode.None
                return
            }
            else{ // 권한을 얻었을 때
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource

        // 실내 지도 활성화
        this.naverMap.isIndoorEnabled = true
    }
}