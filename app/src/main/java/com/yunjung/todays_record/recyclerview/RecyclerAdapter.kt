package com.yunjung.todays_record.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todays_record.R
import com.yunjung.todays_record.databinding.ActivityMainBinding
import com.yunjung.todays_record.databinding.ItemStudioBinding
import com.yunjung.todays_record.studio.StudioViewModel
import javax.xml.validation.ValidatorHandler

class RecyclerAdapter : RecyclerView.Adapter<RecyclerViewHolder>(){
    lateinit var binding : ItemStudioBinding
    lateinit var layoutInflater : LayoutInflater

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // 연결할 레이아웃 설정
        layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return RecyclerViewHolder(binding)
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    // 목록의 아이템 개수
    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}