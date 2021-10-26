package com.yunjung.todaysrecord.writereivew

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentWriteReviewBinding
import com.yunjung.todaysrecord.detail.DetailFragmentArgs
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.network.RetrofitManager
import retrofit2.Call
import retrofit2.Response

class WriteReivewFragment : Fragment() {
    // DataBinding & ViewModel 관련 변수
    lateinit var binding : FragmentWriteReviewBinding
    lateinit var viewModel : WriteReviewViewModel

    // Navigaion component safe args 관련 변수
    val args : WriteReivewFragmentArgs by navArgs()
    private lateinit var psId: String

    companion object{
        fun  newInstance() : WriteReivewFragment{
            return WriteReivewFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_review, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WriteReviewViewModel::class.java)

        // Navigaion component safe args 관련
        psId = args.psId!!

        // '등록' 버튼 클릭 이벤트 설정 (해당 사진관에 리뷰 등록 & 뒤로 감)
        binding.finishBtn.setOnClickListener {
            val rating : Int = binding.ratingBar.numStars
            val userId : String = "616be2b08346b820364b82b1" // 임의 값 
            val content : String = binding.reviewContent.text.toString()
            val image : String = "https://search.pstatic.net/common/?autoRotate=true&quality=95&type=w750&src=https%3A%2F%2Fnaverbooking-phinf.pstatic.net%2F20210317_43%2F1615958660050OIuIJ_JPEG%2Fimage.jpg" // 임의 값

            // 서버에 전달
            val call : Call<Review>? = RetrofitManager.iRetrofit?.postReview(psId, userId, rating, content, image)
            call?.enqueue(object : retrofit2.Callback<Review>{
                // 응답 성공시
                override fun onResponse(call: Call<Review>, response: Response<Review>) {
                    Log.e(TAG, "응답 성공")
                }

                // 응답 실패시
                override fun onFailure(call: Call<Review>, t: Throwable) {
                    Log.e(TAG, "응답 실패")
                }
            })

            it.findNavController().navigateUp()
        }

        // '취소' 버튼 클릭 이벤트 설정 (뒤로 감)
        binding.cancelBtn.setOnClickListener {
            it.findNavController().navigateUp()
        }
    }
}