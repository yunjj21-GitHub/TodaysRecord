package com.yunjung.todaysrecord.myreviews

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentMyreviewsBinding
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.recyclerview.ReviewAdapter
import retrofit2.Call
import retrofit2.Response

class MyreviewFragment : Fragment(){
    lateinit var binding : FragmentMyreviewsBinding
    lateinit var viewModel: MyreviewViewModel

    // var userId : String = (requireActivity() as MainActivity).viewModel.userId.value ?: "anonymous"
    var userId : String = "616be2b08346b820364b82b1" // 로그인된 userId

    companion object{
        fun newInstance() : MyreviewFragment {
            return MyreviewFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_myreviews, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MyreviewViewModel::class.java)
        binding.viewModel = viewModel

        // Review의 userId가 로그인된 userId와 일치하는 것을 가져옴
        val call : Call<List<Review>> = RetrofitManager.iRetrofit?.getReviewByUserId(userId = userId)
        call?.enqueue(object : retrofit2.Callback<List<Review>>{
            // 응답 성공시
            override fun onResponse(
                call: Call<List<Review>>,
                response: Response<List<Review>>
            ) {

                // viewModel 업데이트
                val result : List<Review> = response.body() ?: listOf()
                viewModel.updateReviewList(result)
                viewModel.updateReviewNum(result.size)

                // 리사이클러뷰 적용
                initRecycler()
                subscribeStudioList()

                // 레이아웃과 연결
                binding.viewModel = viewModel
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }

    // 리사이클러뷰 초기설정
    private fun initRecycler(){
        binding.recyclerView.adapter = ReviewAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    // 리사이클러뷰에 보여지는 데이터가 변경시 어댑터에게 알림
    private fun subscribeStudioList() {
        viewModel.reviewList.observe(viewLifecycleOwner, {
            (binding.recyclerView.adapter as ReviewAdapter).submitList(it)
        })
    }
}