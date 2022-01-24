package com.yunjung.todaysrecord.recyclerview

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.ItemReviewBinding
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class ReviewAdapter :
    ListAdapter<Review, ReviewViewHolder>(ReviewDiff){

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewViewHolder {
        // 연결할 레이아웃 설정
        val layoutInflater = LayoutInflater.from(parent.context) // layoutInflater 초기화
        val binding = ItemReviewBinding.inflate(layoutInflater) // binding 초기화

        return ReviewViewHolder(binding)
    }

    // 뷰와 뷰홀더가 묶였을 때 실행
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
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

// 뷰홀더 정의
class ReviewViewHolder(private val binding : ItemReviewBinding) :
    RecyclerView.ViewHolder(binding.root){

    // 초기화
    fun initBinding(review: Review) {
        binding.item = review // review가 binding객체의 레이아웃의 item변수로 넘어감

        displayUserInfo(review.userId!!)

        displayReviewImage(review.image.toString())

        setRemoveBtnClickEvent(review)
    }

    private fun displayUserInfo(userId : String){
        CoroutineScope(Dispatchers.Main).launch {
            val response = withContext(Dispatchers.IO){
                RetrofitManager.service.getUserById(userId)
            }

            // nickName 디스플레이
            binding.userNickName.text = response.nickname.toString()

            // userProfile image 디스플레이
            var profileImage : String? = response.profileImage ?: null
            Glide.with(binding.root.context)
                .load(profileImage)
                .circleCrop()
                .fallback(R.drawable.ic_profile)
                .into(binding.userProfile)
        }
    }

    private fun displayReviewImage(reviewImage : String){
        val encodeByte = Base64.decode(reviewImage, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)

        binding.reviewImage.setImageBitmap(bitmap)
    }

    private fun setRemoveBtnClickEvent(review : Review) {
        binding.removeBtn.setOnClickListener {
            val loggedUser = (binding.root.context.applicationContext as MyApplication).user.value!!._id
            if(loggedUser == review.userId){
                CoroutineScope(Dispatchers.Main).launch {
                    val response = withContext(Dispatchers.IO){
                        RetrofitManager.service.deleteReviewById(review._id)
                    }
                    Toast.makeText(binding.root.context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(binding.root.context, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}