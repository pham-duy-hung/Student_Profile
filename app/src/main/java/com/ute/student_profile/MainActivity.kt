package com.ute.student_profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.ute.student_profile.databinding.ActivityMainBinding
import com.ute.student_profile.model.Student
import com.ute.student_profile.utils.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val defaultStudent = Student(
        id = "22505120005",
        name = "Nguyễn Văn An",
        className = "22CT111",
        email = "an.nv@ute.udn.vn",
        gpa = 3.75
    )
    private var currentStudent = defaultStudent

    companion object {
        private const val KEY_STUDENT_DATA = "KEY_STUDENT_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.getSerializable(KEY_STUDENT_DATA)?.let {
            (it as? Student)?.let { restoredStudent ->
                currentStudent = restoredStudent
            }
        }

        bindStudentData(currentStudent)
        setupListeners()
    }

    private fun bindStudentData(student: Student) {
        with(binding) {
            tvStudentName.text = student.name
            tvStudentDetails.text = "MSSV: ${student.id} | Lớp: ${student.className}"
            tvStudentEmail.text = "Email: ${student.email}"

            // Cập nhật text và đổi màu Badge động (Thử thách 1)
            tvGpaBadge.text = "${student.gpa} GPA (${student.gpa.toAcademicRanking()})"
            tvGpaBadge.setTextColor(student.gpa.toRankingColor())

            edtGpaInput.setText(student.gpa.toString())
        }
    }

    private fun setupListeners() {
        binding.edtGpaInput.doOnTextChanged { text, _, _, _ ->
            val input = text?.toString()?.trim() ?: ""
            if (input.isNotEmpty()) {
                binding.edtGpaInput.error = null
                val tempScore = input.toDoubleOrNull()
                if (tempScore != null && tempScore in 0.0..4.0) {
                    binding.tvPreviewRanking.text = "Dự kiến: ${tempScore.toAcademicRanking()}"
                    binding.tvPreviewRanking.show()
                } else {
                    binding.tvPreviewRanking.gone()
                }
            } else {
                binding.tvPreviewRanking.gone()
            }
        }

        // Bắt sự kiện bấm Cập nhật GPA
        binding.btnUpdateGpa.setOnClickListener {
            val rawInput = binding.edtGpaInput.trimmedText()
            val newGpa = rawInput.toDoubleOrNull()

            if (newGpa == null || newGpa !in 0.0..4.0) {
                binding.edtGpaInput.error = "GPA phải từ 0.0 đến 4.0"
                binding.edtGpaInput.requestFocus()
                toast("Điểm số không hợp lệ, vui lòng kiểm tra lại!")
                return@setOnClickListener
            }

            binding.edtGpaInput.error = null
            currentStudent = currentStudent.copy(gpa = newGpa)
            bindStudentData(currentStudent)
            toast("Đã cập nhật GPA thành công!")
        }

        // Thử thách 2: Nút Khôi phục với AlertDialog
        binding.btnReset.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Xác nhận khôi phục")
                setMessage("Bạn có chắc chắn muốn đặt lại điểm GPA ban đầu (3.75) không?")
                setPositiveButton("Đồng ý") { _, _ ->
                    currentStudent = defaultStudent
                    bindStudentData(currentStudent)
                    toast("Đã khôi phục dữ liệu mặc định!")
                }
                setNegativeButton("Hủy", null)
            }.show()
        }

        binding.btnSendReport.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${currentStudent.email}")
                putExtra(Intent.EXTRA_SUBJECT, "[Báo cáo học tập] Sinh viên ${currentStudent.name} - MSSV ${currentStudent.id}")
                putExtra(
                    Intent.EXTRA_TEXT,
                    """
                    Họ và tên: ${currentStudent.name}
                    MSSV: ${currentStudent.id}
                    Lớp: ${currentStudent.className}
                    Điểm GPA: ${currentStudent.gpa}
                    Xếp loại: ${currentStudent.gpa.toAcademicRanking()}
                    """.trimIndent()
                )
            }
            runCatching {
                startActivity(emailIntent)
            }.onFailure {
                toast("Không tìm thấy ứng dụng Email trên thiết bị!")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_STUDENT_DATA, currentStudent)
    }
}