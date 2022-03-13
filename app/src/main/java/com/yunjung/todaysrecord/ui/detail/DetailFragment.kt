package com.yunjung.todaysrecord.ui.detail

import android.content.ContentValues.TAG
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
import com.google.android.material.tabs.TabLayoutMediator
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentDetailBinding
import com.yunjung.todaysrecord.ui.information.InformationFragment
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.ui.photostudioimg.PhotoStudioImgFragment
import com.yunjung.todaysrecord.ui.review.ReviewFragment
import com.yunjung.todaysrecord.viewpager.ViewpagerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailFragment : Fragment(){
    lateinit var binding : FragmentDetailBinding
    lateinit var viewModel: DetailViewModel

    private val args : DetailFragmentArgs by navArgs()

    private lateinit var detailViewPagerAdapter : ViewpagerAdapter
    private lateinit var imageViewPagerAdapter : ViewpagerAdapter

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

        // 사진관 이미지 뷰페이저 설정
        initImageViewPager()

        // imgViewPager 페이지 변경 이벤트 설정 (인디케이터)
        setPageOfImagVPChangeEvent()

        // 사진관 상세 설명 뷰페이저 설정
        initDetailViewPager()

        // detailViewPager 페이지 변경 이벤트 설정 (리사이즈)
        setPageOfDetailVPChangeEvent()

        // 찜버튼 관련 설정
        initHeartBtn()

        // shareBtn 클릭 이벤트 설정
        initShareBtn()
    }

    private fun initImageViewPager(){
        imageViewPagerAdapter = ViewpagerAdapter(requireActivity())
        for(photoStudioImage in viewModel.photoStudio.value!!.image!!){
            Log.e(TAG, photoStudioImage)
            imageViewPagerAdapter.addFragment(PhotoStudioImgFragment(photoStudioImage))
        }
        binding.imageViewPager.adapter = imageViewPagerAdapter

        // 인디케이터에게 페이지 수를 알림
        binding.pageIndicatorView.count = viewModel.photoStudio.value!!.image!!.size
    }

    private fun setPageOfImagVPChangeEvent(){
        binding.imageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                binding.pageIndicatorView.selection = position
            }
        })
    }

    private fun initDetailViewPager(){
        // 뷰페이저에 어댑터 부착
        detailViewPagerAdapter = ViewpagerAdapter(requireActivity())
        detailViewPagerAdapter.addFragment(InformationFragment.newInstance(args)) // Information Fragment 생성시 args 객체를 전달
        detailViewPagerAdapter.addFragment(ReviewFragment.newInstance(args)) // Review Fragment 생성시 args 객체를 전달
        binding.detailViewPager.adapter = detailViewPagerAdapter

        // 뷰페이저에 탭레이아웃 부착
        TabLayoutMediator(binding.tabLayout, binding.detailViewPager){ tab, position ->
            // 탭 아이템의 이름 지정
            when(position){
                0 -> tab.text = "상세정보"
                1 -> tab.text = "리뷰"
            }
        }.attach()
    }

    private fun setPageOfDetailVPChangeEvent(){
        // detailViewPager의 페이지가 변경 될 때마다 실행
        binding.detailViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val view = detailViewPagerAdapter.pageFragments[position].view

                view?.post {
                    val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                    val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    view.measure(wMeasureSpec, hMeasureSpec)
                    if (binding.detailViewPager.layoutParams.height != view.measuredHeight) {
                        binding.detailViewPager.layoutParams = (binding.detailViewPager.layoutParams).also { lp -> lp.height = view.measuredHeight }
                    }
                }
            }
        })
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
            if(viewModel.heartState.value!!){ // 찜 되어 있었다면
                removeUserIdByPhotoStudioInterested()
            }
            else { // 찜 되어 있지 않았다면
                addUserIdInPhotoStudioInterested()
            }
        }
    }

    private fun displayHeartState(){
        // 찜버튼 디스플레이 결정
        viewModel.updateHeartState()
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
            withContext(Dispatchers.IO){
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
            withContext(Dispatchers.IO){
                RetrofitManager.service.addUserIdInPhotostudioInterested(
                    _id = viewModel.photoStudio.value!!._id, userId = viewModel.user.value!!._id)
            }
            viewModel.updateHeartState() // 찜상태 업데이트
            binding.heartBtn.setBackgroundResource(R.drawable.ic_heart_filled_red) // 찜버튼 디스플레이 변경
        }
    }
}