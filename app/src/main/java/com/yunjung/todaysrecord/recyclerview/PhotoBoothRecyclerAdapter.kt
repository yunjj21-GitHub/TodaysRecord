package com.yunjung.todaysrecord.recyclerview

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.booth.BoothFragment
import com.yunjung.todaysrecord.booth.BoothViewModel
import com.yunjung.todaysrecord.databinding.ItemPhotoboothBinding
import com.yunjung.todaysrecord.models.PhotoBooth

// 어댑터
class PhotoBoothRecyclerAdapter(val boothFragment : BoothFragment) : ListAdapter<PhotoBooth, PhotoBoothViewHolder>(UserDataDiff){

    lateinit var binding : ItemPhotoboothBinding
    lateinit var layoutInflater : LayoutInflater

    // 새로운 뷰가 생성될 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoBoothViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemPhotoboothBinding.inflate(layoutInflater)
        return PhotoBoothViewHolder(binding)
    }

    // 뷰의 내용물이 대체될 때
    override fun onBindViewHolder(holder: PhotoBoothViewHolder, position: Int) {
        holder.initBinding(getItem(position))

        // 아이템이 클릭 되었을 때
        holder.itemView.setOnClickListener {

            // 지도의 카메라 위치를 해당 사진 부스의 위치로 변경
            val lat = getItem(position).location!![1]
            val log = getItem(position).location!![0]
            boothFragment.changeNaverCameraPosition(lat, log)
        }
    }
}

// 뷰홀더
class PhotoBoothViewHolder(
    private val binding : ItemPhotoboothBinding
): RecyclerView.ViewHolder(binding.root) {
    fun initBinding(data: PhotoBooth) {
        binding.item = data
    }
}

// 데이터가 달리지면 갱신히도록 함
object UserDataDiff : DiffUtil.ItemCallback<PhotoBooth>() {
    // 데이터의 고유의 아이디만 비교
    override fun areItemsTheSame(oldItem: PhotoBooth, newItem: PhotoBooth): Boolean {
        return oldItem._id == newItem._id
    }

    // 데이터 전체를 비교
    override fun areContentsTheSame(oldItem: PhotoBooth, newItem: PhotoBooth): Boolean {
        return oldItem == newItem
    }
}