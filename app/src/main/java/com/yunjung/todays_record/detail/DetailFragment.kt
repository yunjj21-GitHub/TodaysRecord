package com.yunjung.todays_record.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.yunjung.todays_record.R
import com.yunjung.todays_record.databinding.FragmentDetailBinding
import com.yunjung.todays_record.information.InformationFragment
import com.yunjung.todays_record.review.ReviewFragment
import com.yunjung.todays_record.viewpager.ViewpagerAdapter

class DetailFragment : Fragment(){
    lateinit var binding : FragmentDetailBinding
    lateinit var viewModel: DetailViewModel
    var heartState : Boolean = false

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

        // 찜 버튼 클릭 이벤트 설정
        binding.heartBtn.setOnClickListener {
            if(heartState == false) {
                binding.heartBtn.setBackgroundResource(R.drawable.ic_heart_filled_red)
                heartState = true
            }else{
                binding.heartBtn.setBackgroundResource(R.drawable.ic_heart_empty_gray)
                heartState = false
            }
        }

        // 뷰페이저 적용
        val pagerAdapter = ViewpagerAdapter(requireActivity())

        pagerAdapter.addFragment(InformationFragment())
        pagerAdapter.addFragment(ReviewFragment())

        binding.viewPager.adapter = pagerAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })

        // (뷰페이저 관련) TabLayout attach
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            if(position == 0) tab.text = "상세정보"
            else tab.text = "리뷰"
        }.attach()
    }
}