package com.yunjung.todays_record.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yunjung.todays_record.information.InformationFragment
import com.yunjung.todays_record.review.ReviewFragment

class ViewpagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    var pageFragments : ArrayList<Fragment> = ArrayList()
    // 초기화
    init{
        pageFragments.add(InformationFragment())
        pageFragments.add(ReviewFragment())
    }

    // 페이지의 개수를 넘겨주는 메소드
    override fun getItemCount(): Int {
        return pageFragments.size
    }

    // 디스플레이되는 position에 맞는 페이지를 반환
    override fun createFragment(position: Int): Fragment {
        return pageFragments[position]
    }
}