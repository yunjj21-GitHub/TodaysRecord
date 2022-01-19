package com.yunjung.todaysrecord.studio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentStudioBinding
import com.yunjung.todaysrecord.photostudid.PhotostudioIdFragment
import com.yunjung.todaysrecord.photostudiofamily.PhotostudioFamilyFragment
import com.yunjung.todaysrecord.photostudioother.PhotostudioOtherFragment
import com.yunjung.todaysrecord.photostudioprofile.PhotostudioProfileFragment
import com.yunjung.todaysrecord.viewpager.ViewpagerAdapter

class StudioFragment : Fragment(){
    lateinit var binding : FragmentStudioBinding
    lateinit var viewModel : StudioViewModel

    companion object{
        fun newInstance() : StudioFragment{
            return StudioFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_studio, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(StudioViewModel::class.java)
        binding.viewModel = viewModel

        // 뷰페이저에 어댑터 부착
        initViewPager()
    }

    private fun initViewPager(){
        // 뷰페이저에 어댑터 부착
        val pagerAdapter = ViewpagerAdapter(requireActivity())
        pagerAdapter.addFragment(PhotostudioIdFragment.newInstance())
        pagerAdapter.addFragment(PhotostudioProfileFragment.newInstance())
        pagerAdapter.addFragment(PhotostudioFamilyFragment.newInstance())
        pagerAdapter.addFragment(PhotostudioOtherFragment.newInstance())
        binding.viewPager.adapter = pagerAdapter

        // 뷰페이저에 탭레이아웃 부착
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            // 탭 아이템의 이름 지정
            when(position){
                0 -> tab.text = "증명사진"
                1 -> tab.text = "프로필사진"
                2 -> tab.text = "가족 커플 우정 사진"
                3 -> tab.text = "기타"
            }
        }.attach()
    }
}
