package com.yunjung.todaysrecord.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.databinding.ItemAreaSmallBinding
import com.yunjung.todaysrecord.models.AreaSmall
import com.yunjung.todaysrecord.setlocation.SetlocationFragment

class AreaSmallAdapter : ListAdapter<AreaSmall, AreaSmallAdapter.AreaSmallViewHolder>(AreaSmallDiff){
    // 뷰홀더 정의
    class AreaSmallViewHolder(private val binding : ItemAreaSmallBinding) :
        RecyclerView.ViewHolder(binding.root){

        // 초기화
        fun initBinding(areaSmall: AreaSmall) {
            binding.item = areaSmall
        }
    }

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AreaSmallAdapter.AreaSmallViewHolder {
        // 연결할 레이아웃 설정
        val layoutInflater = LayoutInflater.from(parent.context) // layoutInflater 초기화
        val binding = ItemAreaSmallBinding.inflate(layoutInflater) // binding 초기화

        setMatchParentToRecyclerView(binding)

        return AreaSmallViewHolder(binding)
    }

    // 각 item이 recyclerView를 가득 채우도록 함
    private fun setMatchParentToRecyclerView(binding : ItemAreaSmallBinding){
        val layoutParams =  RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: AreaSmallAdapter.AreaSmallViewHolder, position: Int) {
        // position : 해당 뷰홀더가 리사이클러뷰에서 보여지는 위치 정보를 가지고 있음
        // getItem(position) : 위치에 해당하는 데이터를 가져옴
        holder.initBinding(getItem(position))

        // 아이템 클릭 이벤트 설정
        holder.itemView.setOnClickListener {
            var clickedArea = ""
            if(getItem(position).name != "전체") clickedArea = getItem(position).name ?: ""

            // SetlocationFragment의 selectedArea 업데이트
            SetlocationFragment.selectedArea[2] = clickedArea
        }
    }

    // 데이터가 변경되었을 때 실행
    object AreaSmallDiff : DiffUtil.ItemCallback<AreaSmall>() {
        // 데이터의 고유한 값 1개만 비교
        override fun areItemsTheSame(oldItem: AreaSmall, newItem: AreaSmall): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: AreaSmall, newItem: AreaSmall): Boolean {
            return oldItem == newItem
        }
    }
}