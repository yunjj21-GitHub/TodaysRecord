package com.yunjung.todaysrecord.booth

import android.Manifest
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentBoothBinding
import com.yunjung.todaysrecord.detail.DetailFragment

class BoothFragment : Fragment(), OnMapReadyCallback{
    // DataBinding & ViewModle 관련 변수
    lateinit var binding : FragmentBoothBinding
    lateinit var viewModel: BoothViewModel

    // 네이버 지도 객체
    lateinit var mapFragment : MapFragment

    // 지도 현재위치 표시 관련 변수
    lateinit var locationSource : FusedLocationSource
    lateinit var naverMap: NaverMap

    // 사용자의 현재 위치 (임의의 값으로 초기화)
    var userLongitude : Double = 126.97794744812339
    var userLatitude : Double = 37.566307981726325

    companion object{
        fun newInstance() : BoothFragment{
            return BoothFragment()
        }

        // 지도 현재위치 표시 관련
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private val PERMISSIONS : Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
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

    // 뷰가 완전히 생성 되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(BoothViewModel::class.java)
        binding.viewModel = viewModel

        initMapFragment() // 네이버 지도 객체 생성

        mapFragment.getMapAsync(this) // 비동기로 naverMap 객체를 얻어옴

        // 사용자 현재 위치 추가
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        // boothSearchBtn 클릭 이벤트 설정
        initBoothSearchBtn()

        viewModel.adjBoothList.observe(viewLifecycleOwner, Observer {
            // 주변 사진부스 디스플레이
            displayAdjPhotoBooth()

            // 검색 결과를 알림
            showSearchResult()

            // 네이버 지도 카메라 위치 변경
            if(viewModel.adjBoothList.value!!.isNotEmpty()) changeNaverMapCameraPosition()
        })
    }

    private fun initMapFragment(){
        val fragmentManager = childFragmentManager
        mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fragmentManager.beginTransaction().add(R.id.map, it).commit()
            }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        requestPermissions(PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE) // 필요한 권한을 사용자에게 물음
        this.naverMap.locationSource = locationSource
        // 사용자 위치가 변경 될 때마다 실행
        this.naverMap.addOnLocationChangeListener { location ->
            userLatitude = location.latitude
            userLongitude = location.longitude
        }
    }

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

    private fun initBoothSearchBtn(){
        binding.boothSearchBtn.setOnClickListener {
            if (this::naverMap.isInitialized) {
                // 주변 사진부스 리스트 업데이트
                viewModel.updateAdjBoothList(userLongitude.toString(), userLatitude.toString())
            }
        }
    }

    private fun displayAdjPhotoBooth(){
        if(viewModel.adjBoothList.value == null) return

        for(photoBooth in viewModel.adjBoothList.value!!){
            val marker = Marker()
            marker.position = LatLng(photoBooth.location!![1], photoBooth.location!![0])
            marker.captionText = photoBooth.name.toString()
            marker.captionRequestedWidth = 200

            when (photoBooth.brand) {
                "인생네컷" -> {
                    marker.icon = MarkerIcons.BLACK
                    marker.iconTintColor =  Color.rgb(255, 51, 153)
                }
                "셀픽스" -> {
                    marker.icon = MarkerIcons.BLACK
                    marker.iconTintColor = Color.YELLOW
                }
                "포토이즘" -> {
                    marker.icon = MarkerIcons.BLACK
                    marker.iconTintColor = Color.BLACK
                }
            }
            marker.map = naverMap
        }
    }

    private fun showSearchResult(){
        val contents = viewModel.adjBoothList.value!!.size.toString() +"개의 사진부스가 검색되었습니다."
        Toast.makeText(context, contents, Toast.LENGTH_SHORT).show()
    }

    private fun changeNaverMapCameraPosition(){
        val lat = viewModel.adjBoothList.value!![0].location!![1]
        val log = viewModel.adjBoothList.value!![0].location!![0]
        naverMap.cameraPosition = CameraPosition(LatLng(lat, log), 16.0)
    }
}