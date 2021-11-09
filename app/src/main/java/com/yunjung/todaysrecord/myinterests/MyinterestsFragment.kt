package com.yunjung.todaysrecord.myinterests

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
import com.yunjung.todaysrecord.databinding.FragmentMyinterestsBinding
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.recyclerview.PhotoStudioAdapter
import com.yunjung.todaysrecord.recyclerview.ReviewAdapter
import retrofit2.Call
import retrofit2.Response

class MyinterestsFragment: Fragment(){
    lateinit var binding : FragmentMyinterestsBinding
    lateinit var viewModel: MyinterestsViewModel

    // var userId : String = (requireActivity() as MainActivity).viewModel.userId.value ?: "anonymous"
    var userId : String = "616be2b08346b820364b82b1" // 로그인된 userId

    companion object{
        fun newInstance() : MyinterestsFragment {
            return MyinterestsFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_myinterests, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MyinterestsViewModel::class.java)
        binding.viewModel = viewModel

        // 해당 user가 찜한 사진관의 리스트를 가져옴
        val call : Call<List<PhotoStudio>> = RetrofitManager.iRetrofit?.getPhotostudioListByUserId(userId = userId)
        call?.enqueue(object : retrofit2.Callback<List<PhotoStudio>>{
            // 응답 성공시
            override fun onResponse(
                call: Call<List<PhotoStudio>>,
                response: Response<List<PhotoStudio>>
            ) {
                // viewModel 업데이트
                val result : List<PhotoStudio> = response.body() ?: listOf()
                viewModel.updateInterestsList(result)
                viewModel.updateInterestsNum(result.size)

                // 리사이클러뷰 적용
                initRecycler()
                subscribeStudioList()

                // 레이아웃과 연결
                binding.viewModel = viewModel
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<PhotoStudio>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }

    // 리사이클러뷰 초기설정
    private fun initRecycler(){
        binding.recyclerView.adapter = PhotoStudioAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    // 리사이클러뷰에 보여지는 데이터가 변경시 어댑터에게 알림
    private fun subscribeStudioList() {
        viewModel.interestsList.observe(viewLifecycleOwner, {
            (binding.recyclerView.adapter as PhotoStudioAdapter).submitList(it)
        })
    }
}