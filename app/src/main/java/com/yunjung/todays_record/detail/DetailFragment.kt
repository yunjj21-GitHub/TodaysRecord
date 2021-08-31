package com.yunjung.todays_record.detail

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.yunjung.todays_record.R
import com.yunjung.todays_record.databinding.FragmentDetailBinding
import com.yunjung.todays_record.information.InformationFragment
import com.yunjung.todays_record.models.PhotoStudio
import com.yunjung.todays_record.review.ReviewFragment
import com.yunjung.todays_record.viewpager.ViewpagerAdapter

class DetailFragment : Fragment(){
    // DataBinding & ViewModel 관련 변수
    lateinit var binding : FragmentDetailBinding
    lateinit var viewModel: DetailViewModel

    // Navigaion component safe args 관련 변수
    private val args : DetailFragmentArgs by navArgs()
    private lateinit var photoStudio: PhotoStudio

    // '찜'버튼 이벤트 관련 변수
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

        /* DataBinding & ViewModel 관련 */
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        binding.viewModel = viewModel

        /* Navigaion component safe args 관련 */
        photoStudio = args.photoStudio!! // 이전 프래그먼트에서 보낸값을 받아옴
        binding.studioName.text = photoStudio.name // view에 적용

        /* 그외 코드 */
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

        /* ViewPager & TabLayout 관련 */
        // 뷰페이저에 어댑터 부착
        val pagerAdapter = ViewpagerAdapter(requireActivity())
        pagerAdapter.addFragment(InformationFragment())
        pagerAdapter.addFragment(ReviewFragment())
        binding.viewPager.adapter = pagerAdapter

        // 뷰페이저에 탭레이아웃 부착
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            // 탭 아이템의 이름 지정
            when(position){
                0 -> tab.text = "상세정보"
                1 -> tab.text = "리뷰"
            }
        }.attach()

        // 뷰페이저 페이지 변경 이벤트 설정(resize ViewPager)
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
}