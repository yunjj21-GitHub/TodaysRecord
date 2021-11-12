package com.yunjung.todaysrecord.recyclerview

import android.content.ContentValues
import android.util.Log
import com.yunjung.todaysrecord.models.AreaLarge
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.databinding.ItemAreaLargeBinding
import com.yunjung.todaysrecord.models.AreaMedium
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.setlocation.SetlocationFragment
import com.yunjung.todaysrecord.setlocation.SetlocationViewModel
import retrofit2.Call
import retrofit2.Response

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
            var clickedArea = ""
            if(getItem(position).name != "전체") clickedArea = getItem(position).name ?: ""

            val call : Call<List<AreaMedium>>? = RetrofitManager.iRetrofit?.getAreaMediumByBelong(clickedArea)
            call?.enqueue(object : retrofit2.Callback<List<AreaMedium>> {
                // 응답 성공시
                override fun onResponse(
                    call: Call<List<AreaMedium>>,
                    response: Response<List<AreaMedium>>
                ) {
                    // SetlocationFragment의 areaMediumList 업데이트
                    SetlocationFragment.areaMediumList.value = response.body() ?: listOf()

                    // SetlocationFragment의 areaSmallList 업데이트 (빈 리스트로 만듦)
                    SetlocationFragment.areaSmallList.value = listOf()

                    // SetlocationFragment의 selectedArea 업데이트
                    SetlocationFragment.selectedArea[0] = clickedArea
                    SetlocationFragment.selectedArea[1] = ""
                    SetlocationFragment.selectedArea[2] = ""
                }

                // 응답 실패시
                override fun onFailure(call: Call<List<AreaMedium>>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.localizedMessage)
                }
            })
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