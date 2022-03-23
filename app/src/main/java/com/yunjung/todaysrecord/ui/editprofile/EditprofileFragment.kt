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
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream

class EditprofileFragment : Fragment(){
    lateinit var binding : FragmentEditprofileBinding
    lateinit var viewModel: EditprofileViewModel

    // 사진 업로드 관련
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

    // 선택된 새로운 이미지
    private var newProfileImg : String? = null

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
        // 선택된 이미지 기존 이미지로 초기화
        newProfileImg = viewModel.user.value!!.profileImage.toString()

        // userProfile 이미지 디스플레이
        displayProfileImage()

        // finishBtn 클릭 이벤트 설정
        initFinishBtn()

        // 프로필 이미지 변경 버튼 클릭 이벤트 설정
        initChangeProfileImgBtn()
    }

    // 프로필 이미지 변경 버튼 클릭 이벤트 설정
    private fun initChangeProfileImgBtn(){
        binding.changeProfileImgBtn.setOnClickListener {
            getPicture() // 디바이스의 갤러리에서 이미지를 얻어옴
        }
    }

    private fun initActivityResultLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri : Uri? ->
            // 선택한 이미지의 bitmap 생성
            val inputStream : InputStream = requireActivity().contentResolver.openInputStream(uri!!)!!
            var bitmap : Bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            // bitmap 이미지 사이즈 지정
            bitmap = Bitmap.createScaledBitmap(bitmap,50,50,true)

            newProfileImg = bitmapToString(bitmap) // 선택된 이미지(Bitmap)을 문자열로 변환

            // 프로필 이미지뷰에 선택된 이미지 디스플레이
            displayProfileImage()
        }
    }

    private fun getPicture(){
        activityResultLauncher.launch("image/*") // 이미지를 얻어옴
    }

    // bitmap을 string으로 변환
    private fun bitmapToString(bitmap : Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // string을 bitmap으로 변환
    private fun stringToBitmap(encodedString : String) : Bitmap {
        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }

    // 프로필 이미지 디스플레이
    private fun displayProfileImage(){
        if(newProfileImg == null) return

        if(newProfileImg!!.substring(0, 5) == "https") { // 웹 url 이미지라면
            Glide.with(binding.root.context)
                .load(newProfileImg)
                .circleCrop()
                .into(binding.userProfile)
            return
        }else{ // bitmap string 이미지라면
            Glide.with(binding.root.context)
                .load(stringToBitmap(newProfileImg.toString()))
                .circleCrop()
                .into(binding.userProfile)
            return
        }
    }

    // 완료 버튼 클릭 이벤트 설정
    private fun initFinishBtn(){
        binding.finishBtn.setOnClickListener {
            // 입력된 nickname을 받아옴
            val newUserNickname : String = binding.editTextUserNickname.text.toString()

            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    RetrofitManager.service
                        .patchUserById(_id = viewModel.user.value!!._id,
                            profileImg = newProfileImg,
                            nickname = newUserNickname)
                }

                // 로그인된 유저 정보 업데이트
                (requireContext().applicationContext as MyApplication).user.value = response
                findNavController().navigateUp() // 뒤로감
            }
        }
    }
}
