package com.yunjung.todaysrecord.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.databinding.ItemSearchResultBinding
import com.yunjung.todaysrecord.databinding.ItemStudioBinding
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.ui.detail.DetailFragmentDirections

class SearchResultsAdapter  :
    ListAdapter<PhotoStudio , SearchResultsViewHolder>(PhotoStudioDiff){
    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): SearchResultsViewHolder {
        // 연결할 레이아웃 설정
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchResultBinding.inflate(layoutInflater)

        return SearchResultsViewHolder(binding)
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: SearchResultsViewHolder , position: Int) {
        holder.initBinding(getItem(position))
        holder.itemView.setOnClickListener {
            // 아이템 클릭시 DetailFragment 화면으로 이동
            val direction = DetailFragmentDirections.actionGlobalDetailFragment(getItem(position))
            it.findNavController().navigate(direction)
        }
    }
}

// 뷰홀더 정의
class SearchResultsViewHolder(private val binding : ItemSearchResultBinding) :
    RecyclerView.ViewHolder(binding.root){

    // 초기화
    fun initBinding(photoStudio: PhotoStudio) {
        binding.item = photoStudio // photoStudio가 binding객체의 레이아웃의 item변수로 넘어감

        // 사진관 대표 이미지 설정
        var photoStudioImage : String = photoStudio.image!![0]
        Glide.with(binding.root.context).load(photoStudioImage).into(binding.studioMainImage)
    }
}