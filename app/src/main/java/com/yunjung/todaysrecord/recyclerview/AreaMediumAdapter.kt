package com.yunjung.todaysrecord.recyclerview

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.databinding.ItemAreaMediumBinding
import com.yunjung.todaysrecord.models.AreaMedium
import com.yunjung.todaysrecord.models.AreaSmall
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.setlocation.SetlocationFragment
import com.yunjung.todaysrecord.setlocation.SetlocationViewModel
import retrofit2.Call
import retrofit2.Response

class AreaMediumAdapter(val setlocationViewModel : SetlocationViewModel)
    : ListAdapter<AreaMedium, AreaMediumAdapter.AreaMediumViewHolder>(AreaMediumDiff){
    // 뷰홀더 정의
    class AreaMediumViewHolder(private val binding : ItemAreaMediumBinding) :
        RecyclerView.ViewHolder(binding.root){

        // 초기화
        fun initBinding(areaMedium: AreaMedium) {
            binding.item = areaMedium
        }
    }

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AreaMediumAdapter.AreaMediumViewHolder {
        // 연결할 레이아웃 설정
        val layoutInflater = LayoutInflater.from(parent.context) // layoutInflater 초기화
        val binding = ItemAreaMediumBinding.inflate(layoutInflater) // binding 초기화

        setMatchParentToRecyclerView(binding)

        return AreaMediumViewHolder(binding)
    }

    // 각 item이 recyclerView를 가득 채우도록 함
    private fun setMatchParentToRecyclerView(binding : ItemAreaMediumBinding){
        val layoutParams =  RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: AreaMediumAdapter.AreaMediumViewHolder, position: Int) {
        // position : 해당 뷰홀더가 리사이클러뷰에서 보여지는 위치 정보를 가지고 있음
        // getItem(position) : 위치에 해당하는 데이터를 가져옴
        holder.initBinding(getItem(position))

        // 아이템 클릭 이벤트 설정
        holder.itemView.setOnClickListener {
            var clickedArea = ""
            if(getItem(position).name != "전체") clickedArea = getItem(position).name ?: ""

            val call : Call<List<AreaSmall>>? = RetrofitManager.iRetrofit?.getAreaSmallByBelong(clickedArea)
            call?.enqueue(object : retrofit2.Callback<List<AreaSmall>>{
                // 응답 성공시
                override fun onResponse(
                    call: Call<List<AreaSmall>>,
                    response: Response<List<AreaSmall>>
                ) {
                    // SetLocationViewModel의 areaSmallList 업데이트
                    setlocationViewModel.areaSmallList.value = response.body() ?: listOf()

                    // SetLocationViewModel의 selectedArea 업데이트
                    SetlocationFragment.selectedArea[1] = clickedArea
                    SetlocationFragment.selectedArea[2] = ""

                    // 토스트 메세지 출력
                    if(clickedArea == ""){
                        Toast.makeText(holder.itemView.context, SetlocationFragment.selectedArea[0] + " " + "전체", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(holder.itemView.context, SetlocationFragment.selectedArea[0] + " " + SetlocationFragment.selectedArea[1], Toast.LENGTH_SHORT).show()
                    }
                }

                // 응답 실패시
                override fun onFailure(call: Call<List<AreaSmall>>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.localizedMessage)
                }
            })
        }
    }

    // 데이터가 변경되었을 때 실행
    object AreaMediumDiff : DiffUtil.ItemCallback<AreaMedium>() {
        // 데이터의 고유한 값 1개만 비교
        override fun areItemsTheSame(oldItem: AreaMedium, newItem: AreaMedium): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: AreaMedium, newItem: AreaMedium): Boolean {
            return oldItem == newItem
        }
    }
}