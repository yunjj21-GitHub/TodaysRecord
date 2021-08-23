package com.yunjung.todays_record.studio

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yunjung.todays_record.R
import com.yunjung.todays_record.R.color.mainColor
import com.yunjung.todays_record.databinding.FragmentStudioBinding
import com.yunjung.todays_record.recyclerview.PhotoStudioAdapter

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
        subscribeStudioList(1)

        // 상단 버튼 이벤트 적용
        binding.idenBtn.setOnClickListener {
            binding.idenBtn.setBackgroundResource(R.drawable.studio_top_btn_clicked)
            binding.idenBtn.setTextColor(Color.parseColor("#512771"))

            binding.profileBtn.setBackgroundResource(R.color.mainColor)
            binding.profileBtn.setTextColor(Color.WHITE)

            binding.moreBtn.setBackgroundResource(R.color.mainColor)
            binding.moreBtn.setTextColor(Color.WHITE)

            binding.otherBtn.setBackgroundResource(R.color.mainColor)
            binding.otherBtn.setTextColor(Color.WHITE)

            subscribeStudioList(1)
        }
        binding.profileBtn.setOnClickListener {
            binding.profileBtn.setBackgroundResource(R.drawable.studio_top_btn_clicked)
            binding.profileBtn.setTextColor(Color.parseColor("#512771"))

            binding.idenBtn.setBackgroundResource(R.color.mainColor)
            binding.idenBtn.setTextColor(Color.WHITE)

            binding.moreBtn.setBackgroundResource(R.color.mainColor)
            binding.moreBtn.setTextColor(Color.WHITE)

            binding.otherBtn.setBackgroundResource(R.color.mainColor)
            binding.otherBtn.setTextColor(Color.WHITE)

            subscribeStudioList(2)
        }
        binding.moreBtn.setOnClickListener {
            binding.moreBtn.setBackgroundResource(R.drawable.studio_top_btn_clicked)
            binding.moreBtn.setTextColor(Color.parseColor("#512771"))

            binding.profileBtn.setBackgroundResource(R.color.mainColor)
            binding.profileBtn.setTextColor(Color.WHITE)

            binding.idenBtn.setBackgroundResource(R.color.mainColor)
            binding.idenBtn.setTextColor(Color.WHITE)

            binding.otherBtn.setBackgroundResource(R.color.mainColor)
            binding.otherBtn.setTextColor(Color.WHITE)

            subscribeStudioList(3)
        }
        binding.otherBtn.setOnClickListener {
            binding.otherBtn.setBackgroundResource(R.drawable.studio_top_btn_clicked)
            binding.otherBtn.setTextColor(Color.parseColor("#512771"))

            binding.profileBtn.setBackgroundResource(R.color.mainColor)
            binding.profileBtn.setTextColor(Color.WHITE)

            binding.moreBtn.setBackgroundResource(R.color.mainColor)
            binding.moreBtn.setTextColor(Color.WHITE)

            binding.idenBtn.setBackgroundResource(R.color.mainColor)
            binding.idenBtn.setTextColor(Color.WHITE)

            subscribeStudioList(4)
        }
    }

    // 리사이클러뷰 초기설정
    private fun initRecycler(){
        binding.recyclerViewStudio.adapter = PhotoStudioAdapter()
        binding.recyclerViewStudio.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    // 리사이클러뷰에 보여지는 데이터가 변경시 어댑터에게 알림
    private fun subscribeStudioList(target : Int) {
       if(target == 1){
           viewModel.idenStdioList.observe(viewLifecycleOwner, {
               (binding.recyclerViewStudio.adapter as PhotoStudioAdapter).submitList(it)
           })
       }else if(target == 2) {
           viewModel.profileStudioList.observe(viewLifecycleOwner, {
               (binding.recyclerViewStudio.adapter as PhotoStudioAdapter).submitList(it)
           })
       }else if(target == 3) {
           viewModel.moreStudioList.observe(viewLifecycleOwner, {
               (binding.recyclerViewStudio.adapter as PhotoStudioAdapter).submitList(it)
           })
       }else if(target == 4){
           viewModel.otherStudioList.observe(viewLifecycleOwner, {
               (binding.recyclerViewStudio.adapter as PhotoStudioAdapter).submitList(it)
           })
       }
    }
}
