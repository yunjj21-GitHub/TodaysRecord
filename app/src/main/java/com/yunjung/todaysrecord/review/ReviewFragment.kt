package com.yunjung.todaysrecord.review

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.MyApplication
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

        // 로그인된 userId
        val userId : String = (requireContext().applicationContext as MyApplication).userId.value.toString()

        // 서버로부터 해당 사진관의 리뷰를 가져옴
        val call : Call<List<Review>> = RetrofitManager.iRetrofit.getReviewByPsId(photoStudio._id)
        call.enqueue(object : retrofit2.Callback<List<Review>> {
            // 응답 성공시
            override fun onResponse(
                call: Call<List<Review>>,
                response: Response<List<Review>>
            ) {
                /* viewModel에 필요한 값 전달 */
                val result : List<Review> = response.body() ?: listOf()
                viewModel.getReviewList(result)
                viewModel.getReviewNum(result.size)

                // reviewAvg & 각 별점의 비율을 계산
                var avg : Int = 0
                var fiveStar : Int = 0
                var fourStar : Int = 0
                var threeStar : Int = 0
                var twoStar : Int = 0
                var oneStar : Int = 0

                if(result.isNotEmpty()){
                    for(res in result){
                        avg += res.rating!!

                        when (res.rating) {
                            5 -> fiveStar++
                            4 -> fourStar++
                            3 -> threeStar++
                            2 -> twoStar++
                            1 -> oneStar++
                        }
                    }
                    viewModel.getReviewAvg(avg / result.size)

                    fiveStar = (fiveStar * 100) / result.size
                    fourStar = (fourStar * 100) / result.size
                    threeStar = (threeStar * 100) / result.size
                    twoStar = (twoStar * 100) / result.size
                    oneStar = (oneStar * 100) / result.size
                }
                viewModel.getRating(fiveStar, fourStar, threeStar, twoStar, oneStar)

                /* '리뷰 사진 모아보기'란의 이미지 처리 */
                // 해당 사진관의 사진 리뷰만 가져옴
                val call : Call<List<Review>>? = RetrofitManager.iRetrofit?.getImageReviewByPsId(photoStudio._id)
                call?.enqueue(object : retrofit2.Callback<List<Review>> {
                    // 응답 성공시
                    override fun onResponse(
                        call: Call<List<Review>>,
                        response: Response<List<Review>>
                    ) {
                        if(response.body()!!.isNotEmpty()){
                            val preImage1 = stringToBitmap(response.body()!![0].image.toString())
                            binding.preImageView1.setImageBitmap(preImage1)
                        }
                        if(response.body()!!.size >= 2){
                            val preImage2 = stringToBitmap(response.body()!![1].image.toString())
                            binding.preImageView2.setImageBitmap(preImage2)
                        }
                    }

                    // 응답 실패시
                    override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                        Log.e(ContentValues.TAG, t.localizedMessage)
                    }
                })

                /* 리사이클러뷰 적용 */
                initRecycler()
                subscribeStudioList()

                /* '사진 더 보기' 버튼 클릭 이벤트 설정 */
                binding.moreImageBtn.setOnClickListener {
                    val direction = MoreImageFragmentDirections.actionGlobalMoreimageFragment(
                        photoStudio)
                    findNavController().navigate(direction)
                }

                binding.viewModel = viewModel
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })

        /* '리뷰 작성하기' 버튼 클릭 이벤트 설정 */
        binding.writeReviewBtn.setOnClickListener {
            if(userId != "anonymous"){ // 로그인이 되어 있다면
                val directions = WriteReivewFragmentDirections.actionGlobalWriteReivewFragment(photoStudio._id.toString())
                it.findNavController().navigate(directions)
            }else{ // 로그인이 되어 있지 않다면
                Toast.makeText(requireContext(), "먼저 로그인을 해주세요", Toast.LENGTH_LONG).show()
            }
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

    // String을 Bitmap으로 변환
    fun stringToBitmap(encodedString : String) : Bitmap? {
        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}