package com.yunjung.todaysrecord.recyclerview

import com.yunjung.todaysrecord.models.AreaLarge
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.databinding.ItemAreaLargeBinding
import com.yunjung.todaysrecord.setlocation.SetlocationFragment

class AreaLargeAdapter: ListAdapter<AreaLarge, AreaLargeAdapter.AreaLargeViewHolder>(AreaLargeDiff){
    // 뷰홀더 정의
    class AreaLargeViewHolder(private val binding : ItemAreaLargeBinding) :
        RecyclerView.ViewHolder(binding.root){

        // 초기화
        fun initBinding(areaLarge: AreaLarge) {
            binding.item = areaLarge
        }
    }

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AreaLargeAdapter.AreaLargeViewHolder {
        // 연결할 레이아웃 설정
        val layoutInflater = LayoutInflater.from(parent.context) // layoutInflater 초기화
        val binding = ItemAreaLargeBinding.inflate(layoutInflater) // binding 초기화

        setMatchParentToRecyclerView(binding)

        return AreaLargeViewHolder(binding)
    }

    // 각 item이 recyclerView를 가득 채우도록 함
    private fun setMatchParentToRecyclerView(binding : ItemAreaLargeBinding){
        val layoutParams =  RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: AreaLargeAdapter.AreaLargeViewHolder, position: Int) {
        // position : 해당 뷰홀더가 리사이클러뷰에서 보여지는 위치 정보를 가지고 있음
        // getItem(position) : 위치에 해당하는 데이터를 가져옴
        holder.initBinding(getItem(position))

        // 아이템 클릭 이벤트 설정
        holder.itemView.setOnClickListener {
            SetlocationFragment.userArea[0] = getItem(position).name

            if(getItem(position).name == "전국"){
                SetlocationFragment.userArea[1] = "전체"
                SetlocationFragment.userArea[2] = "전체"
            }
            else{
                SetlocationFragment.userArea[1] = null
                SetlocationFragment.userArea[2] = null
            }
        }
    }

    // 데이터가 변경되었을 때 실행
    object AreaLargeDiff : DiffUtil.ItemCallback<AreaLarge>() {
        // 데이터의 고유한 값 1개만 비교
        override fun areItemsTheSame(oldItem: AreaLarge, newItem: AreaLarge): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: AreaLarge, newItem: AreaLarge): Boolean {
            return oldItem == newItem
        }
    }
}