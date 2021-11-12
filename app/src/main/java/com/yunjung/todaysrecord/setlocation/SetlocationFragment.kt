package com.yunjung.todaysrecord.setlocation

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todaysrecord.MainViewModel
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentSetlocationBinding
import com.yunjung.todaysrecord.main.MainActivity
import com.yunjung.todaysrecord.models.AreaLarge
import com.yunjung.todaysrecord.models.AreaMedium
import com.yunjung.todaysrecord.models.AreaSmall
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.recyclerview.AreaLargeAdapter
import com.yunjung.todaysrecord.recyclerview.AreaMediumAdapter
import com.yunjung.todaysrecord.recyclerview.AreaSmallAdapter
import retrofit2.Call
import retrofit2.Response

class SetlocationFragment : Fragment(){
    lateinit var binding : FragmentSetlocationBinding
    lateinit var viewModel: SetlocationViewModel

    companion object{
        fun newInstance() : SetlocationFragment {
            return SetlocationFragment()
        }

        var areaLargeList = MutableLiveData<List<AreaLarge>>() // '시도' 지역 리스트를 저장
        var areaMediumList = MutableLiveData<List<AreaMedium>>() // '시군구' 지역 리스트를 저장
        var areaSmallList = MutableLiveData<List<AreaSmall>>() // '동읍면' 지역 리스트를 저장

        var selectedArea = mutableListOf("", "", "")
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setlocation, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SetlocationViewModel::class.java)
        binding.viewModel = viewModel


        // 화면 구성에 필요한 3개의 recyclerView를 모두 적용

        // '시도' 지역 리스트를 보여주는 recyclerView
        val call : Call<List<AreaLarge>>? = RetrofitManager.iRetrofit?.getAreaLarge()
        call?.enqueue(object : retrofit2.Callback<List<AreaLarge>>{
            // 응답 성공시
            override fun onResponse(
                call: Call<List<AreaLarge>>,
                response: Response<List<AreaLarge>>
            ) {
                areaLargeList.value = response.body() ?: listOf() // areaLargeList 업데이트

                // 리사이클러뷰 적용
                initRecycler("Large")
                subscribeList("Large")
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<AreaLarge>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })

        // '시군구' 지역 리스트를 보여주는 recyclerView
        initRecycler("Medium")
        subscribeList("Medium")

        // '동읍면' 지역 리스트를 보여주는 recyclerView
        initRecycler("Small")
        subscribeList("Small")

        // '완료' 버튼 클릭 이벤트 설정
        binding.finishBtn.setOnClickListener {
            if(selectedArea[0] == "" && selectedArea[1] == "" && selectedArea[2] == ""){
                Toast.makeText(context, "선택하신 지역이 없습니다.", Toast.LENGTH_LONG)
            }else{
                when {
                    selectedArea[2] != "" -> {
                        (requireActivity() as MainActivity).viewModel.updateUerArea(
                            selectedArea[2])
                    }
                    selectedArea[1] != "" -> {
                        (requireActivity() as MainActivity).viewModel.updateUerArea(
                            selectedArea[1]
                        )
                    }
                    selectedArea[0] != "" -> {
                        (requireActivity() as MainActivity).viewModel.updateUerArea(
                            selectedArea[0]
                        )
                    }
                }
            }

            Log.e(TAG, (requireActivity() as MainActivity).viewModel.userArea.value.toString())

            findNavController().navigateUp()
        }
    }

    private fun initRecycler(recyclerName : String){
        when (recyclerName) {
            "Large" -> {
                binding.recyclerAreaLarge.adapter = AreaLargeAdapter()
                binding.recyclerAreaLarge.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            }
            "Medium" -> {
                binding.recyclerAreaMedium.adapter = AreaMediumAdapter()
                binding.recyclerAreaMedium.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            }
            else -> { // recyclerName == "Small"
                binding.recyclerAreaSmall.adapter = AreaSmallAdapter()
                binding.recyclerAreaSmall.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            }
        }
    }

    private fun subscribeList(recyclerName: String){
        when (recyclerName) {
            "Large" -> {
                areaLargeList.observe(viewLifecycleOwner,{
                    (binding.recyclerAreaLarge.adapter as AreaLargeAdapter).submitList(it)
                })
            }
            "Medium" -> {
                areaMediumList.observe(viewLifecycleOwner,{
                    (binding.recyclerAreaMedium.adapter as AreaMediumAdapter).submitList(it)
                })
            }
            else -> { // recyclerName == "Small"
                areaSmallList.observe(viewLifecycleOwner,{
                    (binding.recyclerAreaSmall.adapter as AreaSmallAdapter).submitList(it)
                })
            }
        }
    }
}