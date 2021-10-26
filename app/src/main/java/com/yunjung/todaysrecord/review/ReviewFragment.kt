package com.yunjung.todaysrecord.review

import android.content.ContentValues
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentReviewBinding
import com.yunjung.todaysrecord.detail.DetailFragmentArgs
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.moreimage.MoreImageFragmentDirections
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.recyclerview.ReviewAdapter
import com.yunjung.todaysrecord.writereivew.WriteReivewFragmentDirections
import retrofit2.Call
import retrofit2.Response

class ReviewFragment : Fragment() {
    lateinit var binding: FragmentReviewBinding
    lateinit var viewModel: ReviewViewModel

    companion object {
        // Fragment가 생성될 때 DetailFragmentArgs 객체를 전달 받음
        lateinit var photoStudio: PhotoStudio
        fun newInstance(args : DetailFragmentArgs): ReviewFragment {
            photoStudio = args.photoStudio!!
            return ReviewFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
        binding.viewModel = viewModel

        // 서버로부터 해당 사진관의 리뷰를 가져옴
        val call : Call<List<Review>>? = RetrofitManager.iRetrofit?.getReviewByPsId(photoStudio._id)
        call?.enqueue(object : retrofit2.Callback<List<Review>> {
            // 응답 성공시
            override fun onResponse(
                call: Call<List<Review>>,
                response: Response<List<Review>>
            ) {
                val result : List<Review>? = response.body()
                viewModel.getReviewList(result) // viewModel에 받아온 값 적용

                // '리뷰 사진 모아보기'란의 URL 이미지 처리
                var preImage1 : String? = viewModel.reviewList.value!![0].image
                Glide.with(view).load(preImage1).into(binding.preImageView1)

                var preImage2 : String? = viewModel.reviewList.value!![1].image
                Glide.with(view).load(preImage2).into(binding.preImageView2)

                // 리사이클러뷰 적용
                initRecycler()
                subscribeStudioList()

                // '사진 더 보기' 버튼 클릭 이벤트 설정
                binding.moreImageBtn.setOnClickListener {
                    val direction = MoreImageFragmentDirections.actionGlobalMoreimageFragment(
                        photoStudio)
                    it.findNavController().navigate(direction)
                }
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })

        // '리뷰 작성하기' 버튼 클릭 이벤트 설정
        binding.writeReviewBtn.setOnClickListener {
            val directions = WriteReivewFragmentDirections.actionGlobalWriteReivewFragment(photoStudio._id.toString())
            it.findNavController().navigate(directions)
        }
    }

    // 리사이클러뷰 초기설정
    private fun initRecycler(){
        binding.recyclerViewReview.adapter = ReviewAdapter()
        binding.recyclerViewReview.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    // 리사이클러뷰에 보여지는 데이터가 변경시 어댑터에게 알림
    private fun subscribeStudioList() {
        viewModel.reviewList.observe(viewLifecycleOwner, {
            (binding.recyclerViewReview.adapter as ReviewAdapter).submitList(it)
        })
    }
}