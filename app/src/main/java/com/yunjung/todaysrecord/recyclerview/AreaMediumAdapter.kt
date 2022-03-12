package com.yunjung.todaysrecord.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.databinding.ItemAreaMediumBinding
import com.yunjung.todaysrecord.models.AreaMedium
import com.yunjung.todaysrecord.ui.setlocation.SetlocationViewModel

class AreaMediumAdapter(val setLocationViewModel : SetlocationViewModel)
    : ListAdapter<AreaMedium, AreaMediumAdapter.AreaMediumViewHolder>(AreaMediumDiff){

    // 뷰홀더 정의
    class AreaMediumViewHolder(private val binding : ItemAreaMediumBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun initBinding(areaMedium: AreaMedium) {
            binding.item = areaMedium
        }
    }

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AreaMediumAdapter.AreaMediumViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAreaMediumBinding.inflate(layoutInflater)

        setMatchParentToRecyclerView(binding)

        return AreaMediumViewHolder(binding)
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: AreaMediumAdapter.AreaMediumViewHolder, position: Int) {
        holder.initBinding(getItem(position))

        // 아이템 클릭 이벤트 설정
        holder.itemView.setOnClickListener {
            var clickedArea = ""
            if(getItem(position).name != "전체") clickedArea = getItem(position).name!!

            // setLocationViewModel의 villageList 업데이트
            setLocationViewModel.updateVillageList(clickedArea)

            // SetLocationViewModel의 selectedArea 업데이트
            setLocationViewModel.updateTown(clickedArea)
            setLocationViewModel.updateVillage("")

            // 토스트 메세지 출력
            if(clickedArea == ""){
                Toast.makeText(holder.itemView.context, setLocationViewModel.city.value + " " + "전체", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(holder.itemView.context, setLocationViewModel.city.value + " " + setLocationViewModel.town.value, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 데이터가 변경되었을 때 실행
    object AreaMediumDiff : DiffUtil.ItemCallback<AreaMedium>() {
        override fun areItemsTheSame(oldItem: AreaMedium, newItem: AreaMedium): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: AreaMedium, newItem: AreaMedium): Boolean {
            return oldItem == newItem
        }
    }

    // 각 아이템이 리사이클러뷰를 가득 채우도록 함
    private fun setMatchParentToRecyclerView(binding : ItemAreaMediumBinding){
        val layoutParams =  RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams
    }
}