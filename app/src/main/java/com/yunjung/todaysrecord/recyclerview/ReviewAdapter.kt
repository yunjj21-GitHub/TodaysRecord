package com.yunjung.todaysrecord.recyclerview

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.ItemReviewBinding
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.review.ReviewFragment
import retrofit2.Call
import retrofit2.Response

class ReviewAdapter :
    ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(ReviewDiff){

    // 뷰홀더 정의
    class ReviewViewHolder(private val binding : ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root){

        // 초기화
        fun initBinding(review: Review) {
            binding.item = review // review가 binding객체의 레이아웃의 item변수로 넘어감

            // Review의 UserId를 통해서 대응되는 User 객체를 얻어옴
            val call : Call<User> = RetrofitManager.iRetrofit?.getUserById(review.userId)
            call?.enqueue(object : retrofit2.Callback<User> {
                // 응답 성공시
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    // nickName 디스플레이
                    binding.userNickName.text = response.body()!!.nickname.toString()

                    // userProfile image 디스플레이
                    var profileImage : String? = response.body()!!.profileImage ?: null
                    Glide.with(binding.root.context)
                        .load(profileImage)
                        .fallback(R.drawable.ic_profile)
                        .into(binding.userProfile)
                }

                // 응답 실패시
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(ContentValues.TAG, t.localizedMessage)
                }
            })

            // URL 이미지 처리
            var reviewImage : String? = review.image
            Glide.with(binding.root.context).load(reviewImage).into(binding.reviewImage)
        }
    }

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewAdapter.ReviewViewHolder {
        // 연결할 레이아웃 설정
        val layoutInflater = LayoutInflater.from(parent.context) // layoutInflater 초기화
        val binding = ItemReviewBinding.inflate(layoutInflater) // binding 초기화

        return ReviewViewHolder(binding)
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: ReviewAdapter.ReviewViewHolder, position: Int) {
        // position : 해당 뷰홀더가 리사이클러뷰에서 보여지는 위치 정보를 가지고 있음
        // getItem(position) : 위치에 해당하는 데이터를 가져옴

        holder.initBinding(getItem(position))
    }

    // 데이터가 변경되었을 때 실행
    object ReviewDiff : DiffUtil.ItemCallback<Review>() {
        // 데이터의 고유한 값 1개만 비교
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
}