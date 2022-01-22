package com.yunjung.todaysrecord.recyclerview

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.databinding.ItemStudioBinding
import com.yunjung.todaysrecord.detail.DetailFragmentDirections
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.studio.StudioFragmentDirections

class PhotoStudioAdapter :
    ListAdapter<PhotoStudio, PhotoStudioAdapter.PhotoStudioViewHolder>(PhotoStudioDiff){

    // 뷰홀더 정의
    class PhotoStudioViewHolder(private val binding : ItemStudioBinding) :
        RecyclerView.ViewHolder(binding.root){

        // 초기화
        fun initBinding(photoStudio: PhotoStudio) {
            binding.item = photoStudio // photoStudio가 binding객체의 레이아웃의 item변수로 넘어감

            // cost필드 null일 때 처리
            if(photoStudio.cost == null) binding.costText.text = "가격 정보 없음"

            // 사진관 대표 이미지 처리
            if(photoStudio.image!!.isNotEmpty()) {
                var photoStudioImage : String = photoStudio.image!![0]
                Glide.with(binding.root.context).load(photoStudioImage).into(binding.studioMainImage)
            }
        }
    }

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoStudioViewHolder {
        // 연결할 레이아웃 설정
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemStudioBinding.inflate(layoutInflater)

        return PhotoStudioViewHolder(binding)
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: PhotoStudioViewHolder, position: Int) {
        holder.initBinding(getItem(position))
        holder.itemView.setOnClickListener {
            // 아이템 클릭시 DetailFragment 화면으로 이동
            val direction = DetailFragmentDirections.actionGlobalDetailFragment(getItem(position))
            it.findNavController().navigate(direction)
        }
    }
}

// 데이터가 변경되었을 때 실행
object PhotoStudioDiff : DiffUtil.ItemCallback<PhotoStudio>() {
    override fun areItemsTheSame(oldItem: PhotoStudio, newItem: PhotoStudio): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: PhotoStudio, newItem: PhotoStudio): Boolean {
        return oldItem == newItem
    }
}