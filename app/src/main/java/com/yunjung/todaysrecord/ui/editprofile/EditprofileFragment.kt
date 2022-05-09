package com.yunjung.todaysrecord.ui.editprofile

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.yunjung.todaysrecord.MyApplication
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentEditprofileBinding
import com.yunjung.todaysrecord.models.User
import com.yunjung.todaysrecord.network.RetrofitManager
import com.yunjung.todaysrecord.ui.writereivew.WriteReivewFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.io.ByteArrayOutputStream
import java.io.InputStream

class EditprofileFragment : Fragment(){
    lateinit var binding : FragmentEditprofileBinding
    lateinit var viewModel: EditprofileViewModel

    // 사진 업로드 관련
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

    // 선택된 새로운 이미지
    private var newProfileImageBitmap : Bitmap? = null

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

        initActivityResultLauncher() // 이미지를 얻어오는 화면의 결과를 처리하는 런처 초기화

        return binding.root
    }

    // 뷰가 완전히 생성 되었을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditprofileViewModel::class.java)
        binding.viewModel = viewModel

        // user 업데이트
        viewModel.updateUser((requireContext().applicationContext as MyApplication).user.value!!)
        // 기존 이미지로 프로필 이미지 디스플레이
        Glide.with(binding.root.context)
            .load(viewModel.user.value!!.profileImage)
            .circleCrop()
            .into(binding.userProfile)

        // 프로필 이미지 변경 버튼 클릭 이벤트 설정
        initChangeProfileImgBtn()

        // finishBtn 클릭 이벤트 설정
        initFinishBtn()
    }

    // 프로필 이미지 변경 버튼 클릭 이벤트 설정
    private fun initChangeProfileImgBtn(){
        binding.changeProfileImgBtn.setOnClickListener {
            getPicture() // 디바이스의 갤러리에서 이미지를 얻어옴
        }
    }

    private fun initActivityResultLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri : Uri? ->
            // 선택된 이미지의 bitmap 생성 
            val inputStream : InputStream = requireActivity().contentResolver.openInputStream(uri!!)!!
            var bitmap : Bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap = Bitmap.createScaledBitmap(bitmap,500,500,true) // bitmap 리사이즈

            newProfileImageBitmap = bitmap // 선택된 bitmap 갱신

            // 생성된 bitmap으로 프로필 이미지 디스플레이
            Glide.with(binding.root.context)
                .load(newProfileImageBitmap)
                .circleCrop()
                .into(binding.userProfile)
        }
    }

    private fun getPicture(){
        activityResultLauncher.launch("image/*") // 이미지를 얻어옴
    }

    // 완료 버튼 클릭 이벤트 설정
    private fun initFinishBtn(){
        binding.finishBtn.setOnClickListener {
            // 입력된 nickname을 받아옴
            val newUserNickname : String = binding.editTextUserNickname.text.toString()

            // 업로드할 이미지명
            val profileImageName = viewModel.user.value!!._id + System.currentTimeMillis().toString() + ".jpg"
            // bitmap으로 MultipartBody.Part 생성
            val bitmapRequestBody = newProfileImageBitmap.let { WriteReivewFragment.BitmapRequestBody(it!!) }
            val bitmapMultipartBody = MultipartBody.Part.createFormData("profileImage", profileImageName, bitmapRequestBody)

            lifecycleScope.launch {
                withContext(IO) {
                    // 서버에 선택된 이미지 업로드
                    RetrofitManager.service.profileImageUpload(bitmapMultipartBody)
                }

                val response = withContext(IO) {
                    // 로그인된 유저 정보 업데이트
                    RetrofitManager.service
                        .patchUserById(_id = viewModel.user.value!!._id,
                            profileImg = "http://192.168.0.11/$profileImageName",
                            nickname = newUserNickname)
                }

                (requireContext().applicationContext as MyApplication).user.value = response
                findNavController().navigateUp() // 뒤로감
            }
        }
    }
}
