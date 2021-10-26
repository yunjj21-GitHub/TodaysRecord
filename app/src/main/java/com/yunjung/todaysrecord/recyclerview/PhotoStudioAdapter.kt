package com.yunjung.todaysrecord.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.databinding.ItemStudioBinding
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.studio.StudioFragmentDirections

class PhotoStudioAdapter :
    ListAdapter<PhotoStudio, PhotoStudioAdapter.PhotoStudioViewHolder>(PhotoStudioDiff){
    lateinit var binding : ItemStudioBinding // 카드뷰를 포함한 레이아웃의 바인딩 객체
    lateinit var layoutInflater : LayoutInflater

    // 뷰홀더 정의
    class PhotoStudioViewHolder(private val binding : ItemStudioBinding) :
        RecyclerView.ViewHolder(binding.root){

        // 초기화
        fun initBinding(photoStudio: PhotoStudio) {
            binding.item = photoStudio // photoStudio가 binding객체의 레이아웃의 item변수로 넘어감
        }
    }

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoStudioViewHolder {
        // 연결할 레이아웃 설정
        layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemStudioBinding.inflate(layoutInflater)

        return PhotoStudioViewHolder(binding)
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: PhotoStudioViewHolder, position: Int) {
        // position : 해당 뷰홀더가 리사이클러뷰에서 보여지는 위치 정보를 가지고 있음
        // getItem(position) : 위치에 해당하는 데이터를 가져옴
        holder.initBinding(getItem(position))
        holder.itemView.setOnClickListener {
            // 아이템 클릭시 fragment_detail화면으로 이동
            // it.findNavController().navigate(R.id.action_studioFragment_to_detailFragment)
            val directions =  StudioFragmentDirections.actionStudioFragmentToDetailFragment(getItem(position))
            it.findNavController().navigate(directions)
        }

        // null data 처리
        if(getItem(position).cost == null) binding.costText.text = "가격 정보 없음"

        // URL 이미지 처리
        var photoStudioImage : String = getItem(position).image!![0]
        Glide.with(holder.itemView.context).load(photoStudioImage).into(binding.studioMainImage)
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