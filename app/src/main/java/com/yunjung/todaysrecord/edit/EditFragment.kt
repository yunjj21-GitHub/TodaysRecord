package com.yunjung.todaysrecord.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentEditBinding

class EditFragment : Fragment(){
    lateinit var binding : FragmentEditBinding
    lateinit var viewModel: EditViewModel

    companion object{
        fun newInstance() : EditFragment {
            return EditFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(EditViewModel::class.java)
        binding.viewModel = viewModel

        // 완료버튼 이벤트 설정
        binding.finishBtn.setOnClickListener {
            findNavController().popBackStack() // 뒤로감
        }
    }
}
