package com.yunjung.todaysrecord.recyclerview

import com.yunjung.todaysrecord.models.AreaLarge
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.databinding.ItemAreaLargeBinding
import com.yunjung.todaysrecord.ui.setlocation.SetlocationViewModel

class AreaLargeAdapter(val setLocationViewModel : SetlocationViewModel)
    : ListAdapter<AreaLarge, AreaLargeAdapter.AreaLargeViewHolder>(AreaLargeDiff){

    // 뷰홀더 정의
    class AreaLargeViewHolder(private val binding : ItemAreaLargeBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun initBinding(areaLarge: AreaLarge) {
            binding.item = areaLarge
        }
    }

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AreaLargeAdapter.AreaLargeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAreaLargeBinding.inflate(layoutInflater)

        setMatchParentToRecyclerView(binding)

        return AreaLargeViewHolder(binding)
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: AreaLargeAdapter.AreaLargeViewHolder, position: Int) {
        holder.initBinding(getItem(position))

        // 아이템 클릭 이벤트 설정
        holder.itemView.setOnClickListener {
            val clickedArea = getItem(position).name

            // setLocationViewModel의 townList 업데이트
            setLocationViewModel.updateTownList(clickedArea!!)

            // SetLocationViewModel의 villageList를 빈 리스트로 업데이트
            setLocationViewModel.updateVillageList("초기화")

            // SetLocationViewModel의 selectedArea 업데이트
            setLocationViewModel.updateCity(clickedArea!!)
            setLocationViewModel.updateTown("")
            setLocationViewModel.updateVillage("")

            // 토스트 메세지 출력
            Toast.makeText(holder.itemView.context, setLocationViewModel.city.value, Toast.LENGTH_SHORT).show()
        }
    }

    // 데이터가 변경되었을 때 실행
    object AreaLargeDiff : DiffUtil.ItemCallback<AreaLarge>() {
        override fun areItemsTheSame(oldItem: AreaLarge, newItem: AreaLarge): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: AreaLarge, newItem: AreaLarge): Boolean {
            return oldItem == newItem
        }
    }

    // 각 아이템이 리사이클러뷰를 가득 채우도록 함
    private fun setMatchParentToRecyclerView(binding : ItemAreaLargeBinding){
        val layoutParams =  RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams
    }
}