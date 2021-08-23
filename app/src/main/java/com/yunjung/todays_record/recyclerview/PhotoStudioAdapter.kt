package com.yunjung.todays_record.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todays_record.R
import com.yunjung.todays_record.databinding.StudioItemBinding
import com.yunjung.todays_record.detail.DetailFragment
import com.yunjung.todays_record.main.MainActivity
import com.yunjung.todays_record.models.PhotoStudio

class PhotoStudioAdapter :
    ListAdapter<PhotoStudio, PhotoStudioAdapter.PhotoStudioViewHolder>(PhotoStudioDiff){
    lateinit var binding : StudioItemBinding // 카드뷰를 포함한 레이아웃의 바인딩 객체
    lateinit var layoutInflater : LayoutInflater

    // 뷰홀더 정의
    class PhotoStudioViewHolder(private val binding : StudioItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        // 초기화
        fun initBinding(photoStudio: PhotoStudio) {
            binding.item = photoStudio // photoStudio가 binding객체의 레이아웃의 item변수로 넘어감

        /* studioImageView 설정
            if(photoStudio.image != null){
                binding.studioImageView.setImageResource(photoStudio.image!!) // !!연산자 : Null이 값으로 들어오면 exception을 발생
            }else{
                binding.studioImageView.setImageResource(R.color.pointColor)
            }*/
        }
    }

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoStudioViewHolder {
        // 연결할 레이아웃 설정
        layoutInflater = LayoutInflater.from(parent.context)
        binding = StudioItemBinding.inflate(layoutInflater)

        return PhotoStudioViewHolder(binding)
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: PhotoStudioViewHolder, position: Int) {
        // position : 해당 뷰홀더가 리사이클러뷰에서 보여지는 위치 정보를 가지고 있음
        // getItem(position) : 위치에 해당하는 데이터를 가져옴
        holder.initBinding(getItem(position))
    }
}

// 데이터가 변경되었을 때 실행
object PhotoStudioDiff : DiffUtil.ItemCallback<PhotoStudio>() {
    // 데이터의 고유한 값 1개만 비교
    override fun areItemsTheSame(oldItem: PhotoStudio, newItem: PhotoStudio): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: PhotoStudio, newItem: PhotoStudio): Boolean {
        return oldItem == newItem
    }
}