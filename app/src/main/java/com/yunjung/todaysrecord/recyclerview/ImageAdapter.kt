package com.yunjung.todaysrecord.recyclerview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.databinding.ItemImageBinding
import com.yunjung.todaysrecord.models.Review

class ImageAdapter : ListAdapter<Review, ImageAdapter.ImageViewHolder>(ReviewDiff){
    // 뷰홀더 정의
    class ImageViewHolder(private val binding : ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root){

        // 초기화
        fun initBinding(review: Review) {
            binding.item = review

            val bitmap = stringToBitmap(review.image.toString())
            binding.imageView.setImageBitmap(bitmap)
        }

        // String을 Bitmap으로 변환
        fun stringToBitmap(encodedString : String) : Bitmap? {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        }
    }

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageAdapter.ImageViewHolder {
        // 연결할 레이아웃 설정
        val layoutInflater = LayoutInflater.from(parent.context) // layoutInflater 초기화
        val binding = ItemImageBinding.inflate(layoutInflater) // binding 초기화

        return ImageViewHolder(binding)
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: ImageAdapter.ImageViewHolder, position: Int) {
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