package com.yunjung.todaysrecord.editprofile

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentEditprofileBinding
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

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

        // user 업데이트
        viewModel.updateUser((requireContext().applicationContext as MyApplication).user.value!!)

        // userProfile 이미지 디스플레이
        displayProfileImage()

        // finishBtn 클릭 이벤트 설정
        initFinishBtn()
    }

    private fun displayProfileImage(){
        Glide.with(binding.root.context)
            .load(viewModel.user.value!!.profileImage)
            .fallback(R.drawable.ic_profile)
            .into(binding.userProfile)
    }

    private fun initFinishBtn(){
        binding.finishBtn.setOnClickListener {
            val newUserNickname : String = binding.editTextUserNickname.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                RetrofitManager.service
                    .patchUserNicknameById(_id = viewModel.user.value!!._id, nickname = newUserNickname)
            }
            findNavController().navigateUp() // 뒤로감
        }
    }
}
