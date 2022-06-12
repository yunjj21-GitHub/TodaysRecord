package com.yunjung.todaysrecord.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yunjung.todaysrecord.ui.review.ReviewFragment

class ViewpagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    var pageFragments : ArrayList<Fragment> = ArrayList()

    // 페이지의 개수를 넘겨주는 메소드
    override fun getItemCount(): Int {
        return pageFragments.size
    }

    // 디스플레이되는 position에 맞는 페이지를 반환
    override fun createFragment(position: Int): Fragment {
        return pageFragments[position]
    }

    // page(Fragment)를 추가하는 메소드
    fun addFragment(fragment: Fragment) {
        pageFragments.add(fragment)
    }
}