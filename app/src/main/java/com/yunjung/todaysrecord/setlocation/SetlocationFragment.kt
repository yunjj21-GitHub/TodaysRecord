package com.yunjung.todaysrecord.setlocation

import android.content.ContentValues
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
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentSetlocationBinding
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

        // Fragment를 생성하며, RecyclerViewAreaLarge에 담을 데이터를 가져와 해당 뷰에 적용
        val call : Call<List<AreaLarge>>? = RetrofitManager.iRetrofit?.getAreaLarge()
        call?.enqueue(object : retrofit2.Callback<List<AreaLarge>>{
            // 응답 성공시
            override fun onResponse(
                call: Call<List<AreaLarge>>,
                response: Response<List<AreaLarge>>
            ) {
                val result : List<AreaLarge>? = response.body()
                viewModel.getAreaLargeValue(result)

                // 리사이클러뷰 적용
                initRecycler("Large")
                subscribeList("Large")
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<AreaLarge>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })

        // TEST
        setRecyclerAreaMedium()
        setRecyclerAreaSmall()

        // '완료' 버튼 동작 설정
        binding.finishBtn.setOnClickListener {
            it.findNavController().navigateUp() // 뒤로가기 동작
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
                viewModel.areaLargeList.observe(viewLifecycleOwner,{
                    (binding.recyclerAreaLarge.adapter as AreaLargeAdapter).submitList(it)
                })
            }
            "Medium" -> {
                viewModel.areaMediumList.observe(viewLifecycleOwner,{
                    (binding.recyclerAreaMedium.adapter as AreaMediumAdapter).submitList(it)
                })
            }
            else -> { // recyclerName == "Small"
                viewModel.areaSmallList.observe(viewLifecycleOwner,{
                    (binding.recyclerAreaSmall.adapter as AreaSmallAdapter).submitList(it)
                })
            }
        }
    }

    // RecyclerViewAreaMedium에 담을 데이터를 가져와 해당 뷰에 적용 (AreaLargeAdapter에서 호출)
    fun setRecyclerAreaMedium(){
        val call : Call<List<AreaMedium>>? = RetrofitManager.iRetrofit?.getAreaMediumByBelong("서울")
        call?.enqueue(object : retrofit2.Callback<List<AreaMedium>>{
            // 응답 성공시
            override fun onResponse(
                call: Call<List<AreaMedium>>,
                response: Response<List<AreaMedium>>
            ) {
                val result : List<AreaMedium>? = response.body()
                viewModel.getAreaMediumValue(result)

                // 리사이클러뷰 적용
                initRecycler("Medium")
                subscribeList("Medium")
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<AreaMedium>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }

    // RecyclerViewSmall에 담을 데이터를 가져와 해당 뷰에 적용 (AreaMediumAdapter에서 호출)
    fun setRecyclerAreaSmall(){
        val call : Call<List<AreaSmall>>? = RetrofitManager.iRetrofit?.getAreaSmallByBelong("마포구")
        call?.enqueue(object : retrofit2.Callback<List<AreaSmall>>{
            // 응답 성공시
            override fun onResponse(
                call: Call<List<AreaSmall>>,
                response: Response<List<AreaSmall>>
            ) {
                val result : List<AreaSmall>? = response.body()
                viewModel.getAreaSmallValue(result)

                // 리사이클러뷰 적용
                initRecycler("Small")
                subscribeList("Small")
            }

            // 응답 실패시
            override fun onFailure(call: Call<List<AreaSmall>>, t: Throwable) {
                Log.e(ContentValues.TAG, t.localizedMessage)
            }
        })
    }
}