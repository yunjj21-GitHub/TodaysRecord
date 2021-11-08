package com.yunjung.todaysrecord.moreimage

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentMoreImageBinding
import com.yunjung.todaysrecord.detail.DetailFragmentArgs
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.models.Review
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.recyclerview.ImageAdapter
import com.yunjung.todaysrecord.recyclerview.PhotoStudioAdapter
import com.yunjung.todaysrecord.recyclerview.ReviewAdapter
import com.yunjung.todaysrecord.review.ReviewFragment
import retrofit2.Call
import retrofit2.Response

class MoreImageFragment : Fragment(){
    // DataBinding & ViewModel 관련 변수
    lateinit var binding : FragmentMoreImageBinding
    lateinit var viewModel: MoreImageViewModel

    // Navigaion component safe args 관련 변수
    val args : MoreImageFragmentArgs by navArgs()
    private lateinit var photoStudio: PhotoStudio

    companion object{
        fun newInstance() : MoreImageFragment {
            return MoreImageFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more_image, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MoreImageViewModel::class.java)
        binding.viewModel = viewModel

        /* Navigaion component safe args 관련 */
        photoStudio = args.photoStudio!! // 이전 프래그먼트에서 보낸값을 받아옴

        // 서버로 부터 받아온 사진관 정보와 대응되는 사진을 포함한 리뷰 리스트를 가져옴
        val call : Call<List<Review>>? = RetrofitManager.iRetrofit?.getImageReviewByPsId(ReviewFragment.photoStudio._id)
        call?.enqueue(object : retrofit2.Callback<List<Review>> {
            // 응답 성공시
            override fun onResponse(
                call: Call<List<Review>>,
                response: Response<List<Review>>
            ) {
                val result : List<Review>? = response.body()
                viewModel.getReviewList(result) // viewModel에 가져온 리뷰리스트를 넘겨줌

                // 리사이클러뷰 적용
                initRecycler()
                subscribeStudioList()
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }

    // 리사이클러뷰 초기설정
    private fun initRecycler(){
        binding.recyclerImage.adapter = ImageAdapter()
        // binding.recyclerImage.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerImage.layoutManager = GridLayoutManager(requireContext(), 3)

    }

    // 리사이클러뷰에 보여지는 데이터가 변경시 어댑터에게 알림
    private fun subscribeStudioList() {
        viewModel.reviewList.observe(viewLifecycleOwner, {
            (binding.recyclerImage.adapter as ImageAdapter).submitList(it)
        })
    }
}