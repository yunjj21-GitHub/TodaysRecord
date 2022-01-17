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

    // 뷰가 완전히 생성 되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SetlocationViewModel::class.java)
        binding.viewModel = viewModel

        // cityList 업데이트
        viewModel.updateCityList()

        // 3개의 리사이클러뷰 초기 설정
        initRecycler()
        subscribeList()

        // finishBtn 클릭 이벤트 설정
        initFinishBtn()
    }

    private fun initFinishBtn(){
        binding.finishBtn.setOnClickListener {
            if(viewModel.city.value == "" &&
                viewModel.town.value == "" &&
                viewModel.village.value == ""){
                Toast.makeText(context, "선택하신 지역이 없습니다.", Toast.LENGTH_LONG)
            }else{
                // 사용자 지역 업데이트
                when {
                    viewModel.village.value != "" -> {
                        (requireActivity() as MainActivity).viewModel.updateUerArea(
                            viewModel.village.value.toString())
                    }
                    viewModel.town.value != "" -> {
                        (requireActivity() as MainActivity).viewModel.updateUerArea(
                            viewModel.town.value.toString()
                        )
                    }
                    viewModel.city.value != "" -> {
                        (requireActivity() as MainActivity).viewModel.updateUerArea(
                            viewModel.city.value.toString()
                        )
                    }
                }
            }
            findNavController().navigateUp() // 뒤로감
        }
    }

    private fun initRecycler(){
        binding.cityRecyclerView.adapter = AreaLargeAdapter(viewModel)
        binding.cityRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        binding.townRecyclerView.adapter = AreaMediumAdapter(viewModel)
        binding.townRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        binding.villageRecyclerView.adapter = AreaSmallAdapter(viewModel)
        binding.villageRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun subscribeList() {
        viewModel.cityList.observe(viewLifecycleOwner, {
            (binding.cityRecyclerView.adapter as AreaLargeAdapter).submitList(it)
        })
        viewModel.townList.observe(viewLifecycleOwner, {
            (binding.townRecyclerView.adapter as AreaMediumAdapter).submitList(it)
        })
        viewModel.villageList.observe(viewLifecycleOwner, {
            (binding.villageRecyclerView.adapter as AreaSmallAdapter).submitList(it)
        })
    }
}