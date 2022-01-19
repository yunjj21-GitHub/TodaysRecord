package com.yunjung.todaysrecord.detail

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentDetailBinding
import com.yunjung.todaysrecord.information.InformationFragment
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.review.ReviewFragment
import com.yunjung.todaysrecord.viewpager.ViewpagerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class DetailFragment : Fragment(){
    lateinit var binding : FragmentDetailBinding
    lateinit var viewModel: DetailViewModel

    private val args : DetailFragmentArgs by navArgs()

    private lateinit var pagerAdapter : ViewpagerAdapter

    companion object{
        fun newInstance() : DetailFragment{
            return DetailFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        binding.viewModel = viewModel

        // user 업데이트
        viewModel.updateUser((requireContext().applicationContext as MyApplication).user.value!!)

        // photoStudio 업데이트
        viewModel.updatePhotoStudio(args.photoStudio!!)

        // 사진관 이미지 설정
        setPhotoStudioImage()

        // 뷰페이저 초기설정
        initViewPager()

        // 페이지 변경 이벤트 설정 (리사이즈)
        setPageChangeEvent()

        // 찜버튼 관련 설정
        initHeartBtn()

        // shareBtn 클릭 이벤트 설정
        initShareBtn()
    }

    private fun initViewPager(){
        // 뷰페이저에 어댑터 부착
        pagerAdapter = ViewpagerAdapter(requireActivity())
        pagerAdapter.addFragment(InformationFragment.newInstance(args)) // Information Fragment 생성시 args 객체를 전달
        pagerAdapter.addFragment(ReviewFragment.newInstance(args)) // Review Fragment 생성시 args 객체를 전달
        binding.viewPager.adapter = pagerAdapter

        // 뷰페이저에 탭레이아웃 부착
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            // 탭 아이템의 이름 지정
            when(position){
                0 -> tab.text = "상세정보"
                1 -> tab.text = "리뷰"
            }
        }.attach()
    }

    private fun setPageChangeEvent(){
        // 페이지가 변경 될 때마다 실행
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val view = pagerAdapter.pageFragments[position].view

                view?.post {
                    val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                    val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    view.measure(wMeasureSpec, hMeasureSpec)
                    if (binding.viewPager.layoutParams.height != view.measuredHeight) {
                        binding.viewPager.layoutParams = (binding.viewPager.layoutParams).also { lp -> lp.height = view.measuredHeight }
                    }
                }
            }
        })
    }

    private fun setPhotoStudioImage(){
        var photoStudioImage : String = viewModel.photoStudio.value!!.image!![0]
        Glide.with(this).load(photoStudioImage).into(binding.photoStudioImage)

        // 왼오 버튼 클릭 이벤트 설정
        var idx = 0 // 보여질 사진관의 이미지의 인덱스를 저장
        binding.leftBtn.setOnClickListener { // 왼 버튼 클릭 이벤트 설정
            if(idx == 0){
                idx = viewModel.photoStudio.value!!.image!!.size - 1
            }
            else idx--

            photoStudioImage = viewModel.photoStudio.value!!.image!![idx]
            Glide.with(this).load(photoStudioImage).into(binding.photoStudioImage)
        }

        binding.rightBtn.setOnClickListener { // 오 버튼 클릭 이벤트 설정
            if(idx == viewModel.photoStudio.value!!.image!!.size-1){
                idx = 0
            }
            else idx++

            photoStudioImage = viewModel.photoStudio.value!!.image!![idx]
            Glide.with(this).load(photoStudioImage).into(binding.photoStudioImage)
        }
    }

    private fun initShareBtn(){
        binding.shareBtn.setOnClickListener {
            // 공유하기 창을 띄움
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT,
                    viewModel.photoStudio.value!!.name + "\n주소 : "
                            + viewModel.photoStudio.value!!.address + "\n전화번호 : "
                            + viewModel.photoStudio.value!!.phoneNumber +"\n홈페이지 : "
                            + viewModel.photoStudio.value!!.siteAddress)
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }
    }

    private fun initHeartBtn(){
        displayHeartState()

        // 찜 버튼 클릭 이벤트 설정
        binding.heartBtn.setOnClickListener {
            if(viewModel.user.value!!._id != "anonymous"){ // 로그인 되어 있다면
                if(viewModel.heartState.value!!){ // 찜 되어 있었다면
                    removeUserIdByPhotoStudioInterested()
                }
                else { // 찜 되어 있지 않았다면
                    addUserIdInPhotoStudioInterested()
                }
            }else{ // 로그인 되어 있지 않다면
                Toast.makeText(context, "로그인을 먼저 해주세요", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun displayHeartState(){
        // 찜버튼 디스플레이 결정
        if(viewModel.user.value!!._id != "anonymous") viewModel.updateHeartState()
        viewModel.heartState.observe(viewLifecycleOwner, Observer {
            if(viewModel.heartState.value == true){ // 찜 하고 있다면
                binding.heartBtn.setBackgroundResource(R.drawable.ic_heart_filled_red)
            }else{
                binding.heartBtn.setBackgroundResource(R.drawable.ic_heart_empty_gray)
            }
        })
    }

    // 유저의 아이디를 사진관의 interested 목록에서 제거
    private fun removeUserIdByPhotoStudioInterested(){
        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO){
                RetrofitManager.service.pullUserIdInPhotostudioInterested(
                    _id = viewModel.photoStudio.value!!._id, userId = viewModel.user.value!!._id)
            }
            viewModel.updateHeartState() // 찜상태 업데이트
            binding.heartBtn.setBackgroundResource(R.drawable.ic_heart_empty_gray) // 찜버튼 디스플레이 변경
        }
    }

    // 유저의 아이디를 사진관의 interested 목록에 추가
    private fun addUserIdInPhotoStudioInterested(){
        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO){
                RetrofitManager.service.addUserIdInPhotostudioInterested(
                    _id = viewModel.photoStudio.value!!._id, userId = viewModel.user.value!!._id)
            }
            viewModel.updateHeartState() // 찜상태 업데이트
            binding.heartBtn.setBackgroundResource(R.drawable.ic_heart_filled_red) // 찜버튼 디스플레이 변경
        }
    }
}