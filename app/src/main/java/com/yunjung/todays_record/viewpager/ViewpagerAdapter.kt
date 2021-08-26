package com.yunjung.todays_record.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewpagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    var fragments : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int {
        return fragments.size // fragments의 크기를 반환
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position] // 각 position에 해당하는 fragment를 반환
    }

    fun addFragment(fragment: Fragment){
        fragments.add(fragment)
        notifyItemInserted(fragments.size-1)
    }

    fun removeFragment(){
        fragments.removeLast()
        notifyItemInserted(fragments.size)
    }
}