package com.yunjung.todaysrecord.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.databinding.ItemAreaSmallBinding
import com.yunjung.todaysrecord.models.AreaSmall
import com.yunjung.todaysrecord.ui.setlocation.SetlocationViewModel

class AreaSmallAdapter(val setLocationViewModel : SetlocationViewModel)
    : ListAdapter<AreaSmall, AreaSmallAdapter.AreaSmallViewHolder>(AreaSmallDiff){

    // 뷰홀더 정의
    class AreaSmallViewHolder(private val binding : ItemAreaSmallBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun initBinding(areaSmall: AreaSmall) {
            binding.item = areaSmall
        }
    }

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AreaSmallAdapter.AreaSmallViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAreaSmallBinding.inflate(layoutInflater)

        setMatchParentToRecyclerView(binding)

        return AreaSmallViewHolder(binding)
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: AreaSmallAdapter.AreaSmallViewHolder, position: Int) {
        holder.initBinding(getItem(position))

        // 아이템 클릭 이벤트 설정
        holder.itemView.setOnClickListener {
            var clickedArea = ""
            if(getItem(position).name != "전체") clickedArea = getItem(position).name!!

            // SetLocationViewModel의 village 업데이트
            setLocationViewModel.updateVillage(clickedArea)

            // 토스트 메세지 출력
            if(clickedArea == ""){
                Toast.makeText(holder.itemView.context, setLocationViewModel.city.value + " " + setLocationViewModel.town.value + " " + "전체", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(holder.itemView.context, setLocationViewModel.city.value + " " + setLocationViewModel.town.value + " " + setLocationViewModel.village.value, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 데이터가 변경되었을 때 실행
    object AreaSmallDiff : DiffUtil.ItemCallback<AreaSmall>() {
        override fun areItemsTheSame(oldItem: AreaSmall, newItem: AreaSmall): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: AreaSmall, newItem: AreaSmall): Boolean {
            return oldItem == newItem
        }
    }

    // 각 아이템이 리사이클러뷰를 가득 채우도록 함
    private fun setMatchParentToRecyclerView(binding : ItemAreaSmallBinding){
        val layoutParams =  RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams
    }
}