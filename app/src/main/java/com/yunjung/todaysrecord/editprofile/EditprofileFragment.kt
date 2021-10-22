package com.yunjung.todaysrecord.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentEditprofileBinding

class EditprofileFragment : Fragment(){
    lateinit var binding : FragmentEditprofileBinding
    lateinit var viewModel: EditprofileViewModel

    companion object{
        fun newInstance() : EditprofileFragment {
            return EditprofileFragment()
        }
    }

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_editprofile, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(EditprofileViewModel::class.java)
        binding.viewModel = viewModel

        // 완료버튼 이벤트 설정
        binding.finishBtn.setOnClickListener {
            findNavController().popBackStack() // 뒤로감
        }
    }
}
