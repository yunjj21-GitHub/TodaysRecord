package com.yunjung.todaysrecord.recyclerview

import com.yunjung.todaysrecord.models.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.databinding.ItemLoctionBinding
import com.yunjung.todaysrecord.location.SetlocationFragment

class LocationAdapter: ListAdapter<Location, LocationAdapter.LocationViewHolder>(LocationDiff){
    lateinit var binding : ItemLoctionBinding
    lateinit var layoutInflater: LayoutInflater

    // 뷰홀더 정의
    class LocationViewHolder(private val binding : ItemLoctionBinding) :
        RecyclerView.ViewHolder(binding.root){

        // 초기화
        fun initBinding(location: Location) {
            binding.item = location
        }
    }

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationAdapter.LocationViewHolder {
        // 연결할 레이아웃 설정
        layoutInflater = LayoutInflater.from(parent.context) // layoutInflater 초기화
        binding = ItemLoctionBinding.inflate(layoutInflater) // binding 초기화

        setMatchParentToRecyclerView()

        return LocationViewHolder(binding)
    }

    // 각 item이 recyclerView를 가득 채우도록 함
    private fun setMatchParentToRecyclerView(){
        val layoutParams =  RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: LocationAdapter.LocationViewHolder, position: Int) {
        // position : 해당 뷰홀더가 리사이클러뷰에서 보여지는 위치 정보를 가지고 있음
        // getItem(position) : 위치에 해당하는 데이터를 가져옴
        holder.initBinding(getItem(position))

        holder.itemView.setOnClickListener {
            
        }
    }

    // 데이터가 변경되었을 때 실행
    object LocationDiff : DiffUtil.ItemCallback<Location>() {
        // 데이터의 고유한 값 1개만 비교
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.location == newItem.location
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }
    }
}