package com.yunjung.todaysrecord.dialog

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.yunjung.todaysrecord.R
import com.yunjung.todaysrecord.databinding.FragmentReportDialogBinding
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportDialogFragment(private val reviewId : String, private val accuser : String) : DialogFragment() {
    lateinit var binding : FragmentReportDialogBinding
    lateinit var viewModel : ReportDialogViewModel

    // 뷰가 생성될 때 실행
    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_dialog, container, false)
        return binding.root
    }

    // 뷰가 완전히 생성되었을 때 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ReportDialogViewModel::class.java)
        binding.viewModel = viewModel

        // 신고 버튼 클릭 이벤트 설정
        initReportBtn()
    }

    // 신고 버큰 클릭 이벤트 설정
    private fun initReportBtn(){
        binding.reportBtn.setOnClickListener {
            // 선택된 라디오 버튼의 아이디를 받아옴
            val selectedItemId = binding.radioGroup.checkedRadioButtonId
            val selectedItemText = binding.root.findViewById<RadioButton>(selectedItemId)

            // 선택된 라디오 버튼의 텍스트를 받아옴
            val reportType = selectedItemText.text.toString()

            // 신고 내용을 디비로 보냄
            lifecycleScope.launch(Dispatchers.IO) {
                RetrofitManager.service.postReviewReport(reviewId, accuser, reportType)
            }

            Toast.makeText(context, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()

            dismiss() // 다이얼로그 창을 닫음
        }
    }
}