package com.yunjung.todaysrecord.booth

import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentBoothBinding
import com.yunjung.todaysrecord.information.InformationFragment
import com.yunjung.todaysrecord.models.PhotoBooth
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.recyclerview.PhotoStudioAdapter
import retrofit2.Call
import retrofit2.Response

class BoothFragment : Fragment(), OnMapReadyCallback{
    lateinit var binding : FragmentBoothBinding
    lateinit var viewModel: BoothViewModel

    // 네이버 지도 사용자 위치 표시 관련 변수
    lateinit var locationSource : FusedLocationSource
    lateinit var naverMap: NaverMap

    // 임의의 사용자 현재 위치 (서울 시청 좌표)
    var userLongitude : Double = 126.97794744812339
    var userLatitude : Double = 37.566307981726325

    companion object{
        fun newInstance() : BoothFragment{
            return BoothFragment()
        }

        // 네이버 지도 사용자 위치 표시 관련
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

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(BoothViewModel::class.java)
        binding.viewModel = viewModel

        /* 네이버 지도 관련 */
        // 네이버 지도 객체 생성
        val fragmentManager = childFragmentManager
        val mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fragmentManager.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this) // 비동기로 naverMap 객체를 얻어옴

        // 사용자 현재 위치 추가
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        // '내 주변 사진부스 찾기' 버튼 클릭 이벤트 설정
        binding.boothSearchBtn.setOnClickListener {
            if(this::naverMap.isInitialized){

                // 서버에서 사용자의 위치와 가까운 photobooth 리스트를 가져옴
                val call : Call<List<PhotoBooth>>? = RetrofitManager.iRetrofit?.getPhotoboothByLocation(userLongitude.toString(), userLatitude.toString())
                call?.enqueue(object : retrofit2.Callback<List<PhotoBooth>>{
                    // 응답 성공시
                    override fun onResponse(
                        call: Call<List<PhotoBooth>>,
                        response: Response<List<PhotoBooth>>
                    ) {
                        val result : List<PhotoBooth> = response.body()!!
                        for(res in result){
                            val marker = Marker()
                            marker.position = LatLng(res.location!![1], res.location!![0])
                            marker.captionText = res.name.toString()
                            marker.captionRequestedWidth = 200

                            when (res.brand) {
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

                            // 마커 클릭 이벤트 설정
                            marker.setOnClickListener { overlay ->
                                Log.e(TAG, "클릭 이벤트")
                                true
                            }

                            marker.map = naverMap
                        }
                    }

                    // 응답 실패시
                    override fun onFailure(call: Call<List<PhotoBooth>>, t: Throwable) {
                        Log.e(ContentValues.TAG, t.localizedMessage)
                    }
                })
            }
        }
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
        // API 호출 (NaverMap 객체 얻어오기)
        this.naverMap = naverMap

        // 현재 위치 표시 관련
        this.naverMap.locationSource = locationSource
        requestPermissions(PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)

        // 사용자 위치가 변경될 때 마다 실행
        this.naverMap.addOnLocationChangeListener { location ->
            userLatitude = location.latitude
            userLongitude = location.longitude
        }
    }
}