package com.yunjung.todaysrecord.studio

import android.content.ContentValues
import android.graphics.Color
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
import com.yunjung.todaysrecord.databinding.FragmentStudioBinding
import com.yunjung.todaysrecord.models.AreaLarge
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.recyclerview.PhotoStudioAdapter
import retrofit2.Call
import retrofit2.Response

class StudioFragment : Fragment(){
    lateinit var binding : FragmentStudioBinding
    lateinit var viewModel : StudioViewModel

    companion object{
        fun newInstance() : StudioFragment{
            return StudioFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_studio, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(StudioViewModel::class.java)
        binding.viewModel = viewModel

        // 리사이클러뷰 적용
        initRecycler()
        getPhotoStudioByAreaAndType("망원동", "증명사진")

        // 상단 메뉴 버튼 이벤트 설정
        binding.IDPhotoStudioBtn.setOnClickListener {
            binding.IDPhotoStudioBtn.setBackgroundResource(R.drawable.studio_top_btn_clicked)
            binding.IDPhotoStudioBtn.setTextColor(Color.parseColor("#512771"))

            // 안눌린 버튼 처리
            binding.profilePhotoStudioBtn.setBackgroundResource(R.color.mainColor)
            binding.profilePhotoStudioBtn.setTextColor(Color.WHITE)

            binding.familyPhotoStudioBtn.setBackgroundResource(R.color.mainColor)
            binding.familyPhotoStudioBtn.setTextColor(Color.WHITE)

            binding.otherPhotoStudioBtn.setBackgroundResource(R.color.mainColor)
            binding.otherPhotoStudioBtn.setTextColor(Color.WHITE)

            getPhotoStudioByAreaAndType("망원동", "증명사진")
        }
        binding.profilePhotoStudioBtn.setOnClickListener {
            binding.profilePhotoStudioBtn.setBackgroundResource(R.drawable.studio_top_btn_clicked)
            binding.profilePhotoStudioBtn.setTextColor(Color.parseColor("#512771"))

            // 안눌린 버튼 처리
            binding.IDPhotoStudioBtn.setBackgroundResource(R.color.mainColor)
            binding.IDPhotoStudioBtn.setTextColor(Color.WHITE)

            binding.familyPhotoStudioBtn.setBackgroundResource(R.color.mainColor)
            binding.familyPhotoStudioBtn.setTextColor(Color.WHITE)

            binding.otherPhotoStudioBtn.setBackgroundResource(R.color.mainColor)
            binding.otherPhotoStudioBtn.setTextColor(Color.WHITE)

            getPhotoStudioByAreaAndType("망원동", "프로필사진")
        }
        binding.familyPhotoStudioBtn.setOnClickListener {
            binding.familyPhotoStudioBtn.setBackgroundResource(R.drawable.studio_top_btn_clicked)
            binding.familyPhotoStudioBtn.setTextColor(Color.parseColor("#512771"))

            // 안눌린 버튼 처리
            binding.IDPhotoStudioBtn.setBackgroundResource(R.color.mainColor)
            binding.IDPhotoStudioBtn.setTextColor(Color.WHITE)

            binding.profilePhotoStudioBtn.setBackgroundResource(R.color.mainColor)
            binding.profilePhotoStudioBtn.setTextColor(Color.WHITE)

            binding.otherPhotoStudioBtn.setBackgroundResource(R.color.mainColor)
            binding.otherPhotoStudioBtn.setTextColor(Color.WHITE)

            getPhotoStudioByAreaAndType("망원동", "가족 커플 우정 사진")
        }
        binding.otherPhotoStudioBtn.setOnClickListener {
            binding.otherPhotoStudioBtn.setBackgroundResource(R.drawable.studio_top_btn_clicked)
            binding.otherPhotoStudioBtn.setTextColor(Color.parseColor("#512771"))

            // 안눌린 버튼 처리
            binding.IDPhotoStudioBtn.setBackgroundResource(R.color.mainColor)
            binding.IDPhotoStudioBtn.setTextColor(Color.WHITE)

            binding.profilePhotoStudioBtn.setBackgroundResource(R.color.mainColor)
            binding.profilePhotoStudioBtn.setTextColor(Color.WHITE)

            binding.familyPhotoStudioBtn.setBackgroundResource(R.color.mainColor)
            binding.familyPhotoStudioBtn.setTextColor(Color.WHITE)

            getPhotoStudioByAreaAndType("망원동", "기타")
        }
    }

    // 리사이클러뷰 초기설정
    private fun initRecycler(){
        binding.recyclerViewPhotoStudio.adapter = PhotoStudioAdapter()
        binding.recyclerViewPhotoStudio.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    // 서버에서 눌린 상단 버튼에 따라 디스플레이 되어야 하는 사진관 목록 가져와서 recyclerViewPhotoStudio에 담기도록 함
    fun getPhotoStudioByAreaAndType(area : String, type : String){
        // 서버에서 필요한 사진관 리스트 가져오기
        val call : Call<List<PhotoStudio>>? = RetrofitManager.iRetrofit?.getPhotoStudioByAreaAndType(area = "망원동", type = type)
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
                    (binding.recyclerViewPhotoStudio.adapter as PhotoStudioAdapter).submitList(it)
                })
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<PhotoStudio>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }
}
