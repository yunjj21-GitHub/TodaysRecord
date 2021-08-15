package com.yunjung.todays_record.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todays_record.R
import com.yunjung.todays_record.databinding.ActivityMainBinding
import com.yunjung.todays_record.databinding.ItemStudioBinding
import com.yunjung.todays_record.models.PhotoStudio
import com.yunjung.todays_record.studio.StudioViewModel
import javax.xml.validation.ValidatorHandler

class RecyclerAdapter : ListAdapter<PhotoStudio, RecyclerAdapter.PhotoStudioViewHolder>(PhotoStudioDiff){
    lateinit var binding : ItemStudioBinding
    lateinit var layoutInflater : LayoutInflater

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoStudioViewHolder {
        // 연결할 레이아웃 설정
        layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemStudioBinding.inflate(layoutInflater)
        return PhotoStudioViewHolder(binding)
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: PhotoStudioViewHolder, position: Int) {
        holder.initBinding(getItem(position))
    }

    class PhotoStudioViewHolder(
        private val binding : ItemStudioBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun initBinding(photoStudio: PhotoStudio) {
            binding.item = photoStudio
        }
    }
}

object PhotoStudioDiff : DiffUtil.ItemCallback<PhotoStudio>() {
    override fun areItemsTheSame(oldItem: PhotoStudio, newItem: PhotoStudio): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: PhotoStudio, newItem: PhotoStudio): Boolean {
        return oldItem == newItem
    }
}