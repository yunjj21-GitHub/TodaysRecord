package com.yunjung.todays_record.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todays_record.databinding.ReviewItemBinding
import com.yunjung.todays_record.models.Review

class ReviewAdapter :
    ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(ReviewDiff){
    lateinit var binding : ReviewItemBinding
    lateinit var layoutInflater: LayoutInflater

    // 뷰홀더 정의
    class ReviewViewHolder(private val binding : ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        // 초기화
        fun initBinding(review: Review) {
            binding.item = review // review가 binding객체의 레이아웃의 item변수로 넘어감
        }
    }

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewAdapter.ReviewViewHolder {
        // 연결할 레이아웃 설정
        layoutInflater = LayoutInflater.from(parent.context) // layoutInflater 초기화
        binding = ReviewItemBinding.inflate(layoutInflater) // binding 초기화

        return ReviewViewHolder(binding)
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: ReviewAdapter.ReviewViewHolder, position: Int) {
        // position : 해당 뷰홀더가 리사이클러뷰에서 보여지는 위치 정보를 가지고 있음
        // getItem(position) : 위치에 해당하는 데이터를 가져옴
        holder.initBinding(getItem(position))
    }

    // 데이터가 변경되었을 때 실행
    object ReviewDiff : DiffUtil.ItemCallback<Review>() {
        // 데이터의 고유한 값 1개만 비교
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
}