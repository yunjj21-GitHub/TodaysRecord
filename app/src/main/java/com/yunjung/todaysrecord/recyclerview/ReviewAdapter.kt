package com.yunjung.todaysrecord.recyclerview

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.ItemReviewBinding
import com.yunjung.todaysrecord.dialog.ReportDialogFragment
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewAdapter(private val fm: FragmentManager) :
    ListAdapter<Review, ReviewViewHolder>(ReviewDiff){

    // 뷰홀더가 생성 되었을 때 실행
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewViewHolder {
        // 연결할 레이아웃 설정
        val layoutInflater = LayoutInflater.from(parent.context) // layoutInflater 초기화
        val binding = ItemReviewBinding.inflate(layoutInflater) // binding 초기화

        return ReviewViewHolder(binding, fm)
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
class ReviewViewHolder(private val binding : ItemReviewBinding, private val fm : FragmentManager) :
    RecyclerView.ViewHolder(binding.root), PopupMenu.OnMenuItemClickListener {

    lateinit var review : Review

    // 초기화
    fun initBinding(review: Review) {
        // 전역 변수화
        this.review = review

        binding.item = review // review가 binding객체의 레이아웃의 item변수로 넘어감

        displayUserInfo(review.userId!!)

        displayReviewImage(review.image.toString())

        setMoreBtnClickEvent()
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
        Glide.with(binding.root.context)
            .load(reviewImage)
            .override(500, 500)
            .into(binding.reviewImage)
    }

    private fun setMoreBtnClickEvent() {
        binding.moreBtn.setOnClickListener {
            showReviewMorePopupMenu(it)
        }
    }

    // 리뷰 더보기 메뉴를 보여줌
    private fun showReviewMorePopupMenu(v: View) {
        PopupMenu(v.context, v).apply {
            setOnMenuItemClickListener(this@ReviewViewHolder)
            inflate(R.menu.review_more_menu)
            show()
        }
    }

    // 팝업 메뉴 아이템 클릭 이벤트 설정
    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.report -> { // '리뷰 신고하기' 클릭 시
                reviewReport()
                true
            }
            R.id.delete -> { // '삭제하기' 클릭 시
                reviewDelete()
                true
            }
            else -> false
        }
    }

    private fun reviewReport() {
        val reviewId = review._id!!
        val accuser = (binding.root.context.applicationContext as MyApplication).user.value!!._id

        var dialog = ReportDialogFragment(reviewId, accuser!!)
        dialog.show(fm, "report dialog fragment")
    }

    private fun reviewDelete(){
        // 현재 로그인된 계정의 아이디를 가져옴
        val loggedUserId = (binding.root.context.applicationContext as MyApplication).user.value!!._id
        // 로그인된 계정이 해당 리뷰를 삭제할 수 있는지 확인 후 삭제
        if(loggedUserId == review.userId){
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitManager.service.deleteReviewById(review._id)
            }
            Toast.makeText(binding.root.context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(binding.root.context, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}