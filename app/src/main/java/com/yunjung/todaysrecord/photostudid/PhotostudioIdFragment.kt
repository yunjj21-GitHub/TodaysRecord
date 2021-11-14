package com.yunjung.todaysrecord.photostudid

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.MainViewModel
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentPhotostudioIdBinding
import com.yunjung.todaysrecord.main.MainActivity
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.recyclerview.PhotoStudioAdapter
import retrofit2.Call
import retrofit2.Response

class PhotostudioIdFragment : Fragment(){
    lateinit var binding : FragmentPhotostudioIdBinding
    lateinit var viewModel : PhotostudioIdViewModel

    companion object{
        fun newInstance() : PhotostudioIdFragment{
            return PhotostudioIdFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photostudio_id, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(PhotostudioIdViewModel::class.java)
        binding.viewModel = viewModel

        // 리사이클러뷰 적용
        initRecycler()
        getPhotoStudioByAreaAndType((requireActivity() as MainActivity).viewModel.userArea.value ?: "전국", "증명사진")
    }

    // 리사이클러뷰 초기설정
    private fun initRecycler(){
        binding.recyclerView.adapter = PhotoStudioAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    // 적절한 사진관 리스트를 서버에서 가져오는 메소드
    fun getPhotoStudioByAreaAndType(area : String, type : String){
        // 서버에서 필요한 사진관 리스트 가져오기
        val call : Call<List<PhotoStudio>>? = RetrofitManager.iRetrofit?.getPhotoStudioByAreaAndType(area = area, type = type)
        call?.enqueue(object : retrofit2.Callback<List<PhotoStudio>>{
            // 응답 성공시
            override fun onResponse(
                call: Call<List<PhotoStudio>>,
                response: Response<List<PhotoStudio>>
            ) {
                val result : List<PhotoStudio>? = response.body()
                viewModel.updatePhotoStudioList(result)

                // 리사이클러뷰에 보여지는 데이터가 변경시 어댑터에게 알림
                viewModel.photoStudioList.observe(viewLifecycleOwner, {
                    (binding.recyclerView.adapter as PhotoStudioAdapter).submitList(it)
                })
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<PhotoStudio>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }
}